package me.pieking1215.invmove.forge.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = {KeyMapping.class, ToggleKeyMapping.class}
)
public class KeyMappingIsDownMixin {
    @Inject(
            method = "isDown",
            at = @At("HEAD"),
            cancellable = true
    )
    private void overrideIsDown(CallbackInfoReturnable<Boolean> cir) {
        if (InvMove.instance().shouldForceRawKeyDown()) {
            cir.setReturnValue(((KeyMapping)(Object)this).isDown);
        }
    }
}
