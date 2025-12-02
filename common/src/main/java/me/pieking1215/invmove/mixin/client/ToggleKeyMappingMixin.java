package me.pieking1215.invmove.mixin.client;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >1.21.9 {
@Mixin(ToggleKeyMapping.class)
public class ToggleKeyMappingMixin extends KeyMapping {
    public ToggleKeyMappingMixin(String string, int i, Category category) {
        super(string, i, category);
    }

    @Inject(
            method = "release",
            at = @At("HEAD"),
            cancellable = true
    )
    void injectRelease(CallbackInfo ci) {
        // 1.21.9+ has a thing where toggle keys get untoggled when a screen opens, but we don't want that
        if (InvMoveConfig.GENERAL.ENABLED.get() && InvMoveConfig.MOVEMENT.ENABLED.get() && InvMove.instance().allowKey(this)) {
            super.release();
            ci.cancel();
        }
    }
}
//?} else {
/*// not needed on <1.21.9
@Mixin(ToggleKeyMapping.class)
public class ToggleKeyMappingMixin { }
*///?}
