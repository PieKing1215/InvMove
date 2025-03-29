package me.pieking1215.invmove.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(KeyMapping.class)
public class KeyMappingCompareMixin {
    @WrapOperation(
            method = "compareTo(Lnet/minecraft/client/KeyMapping;)I",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private Object safeCompare(Map<String, Integer> instance, Object key, Operation<Integer> original) {
        // the vanilla compareTo function doesn't check that CATEGORY_SORT_ORDER actually contains the category
        // which can result in NPE in Integer.compareTo when we sort the keys stream in InvMoveConfig
        // (see https://github.com/PieKing1215/InvMove/issues/68#issuecomment-2593932606)
        Object order = original.call(instance, key);
        if (order == null) {
            instance.put((String)key, instance.values().stream().max(Integer::compareTo).orElse(0) + 1);
        }
        return original.call(instance, key);
    }
}
