package me.pieking1215.invmove.fabric_like.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class OverlayMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(CallbackInfo callbackInfo) {
        InvMove.instance.drawDebugOverlay();
    }
}
