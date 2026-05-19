package me.pieking1215.invmove.fabric.mixin.client;

import me.pieking1215.invmove.InvMove;
//? if >=1.20 && <26
//import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class OverlayMixin {
    // temp
    //? if <26 {
    /*@Inject(method = "render", at = @At("TAIL"))
    public void render(/^? if >= 1.20 {^/GuiGraphics guiGraphics, int i, int j, float f, /^?}^/CallbackInfo ci) {
        InvMove.instance().drawDebugOverlay(/^? if >= 1.20 {^/guiGraphics/^?}^/);
    }
    *///? }
}
