package me.pieking1215.invmove.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class BackgroundMixin {
    //? >=26 {
    @Inject(
            method = {
                    "extractTransparentBackground"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fillGradient(IIIIII)V"),
            cancellable = true,
            require = 0
    )
    @Group(min = 1)
    private void onRenderTransparentBackground(CallbackInfo ci) {
        //noinspection ConstantConditions
        if (InvMove.instance().shouldDisableScreenBackground((Screen) (Object) this)) {
            ci.cancel();
        }
    }
    //? } else if >=1.19 {
    /*@Inject(
            method = {
                    "renderTransparentBackground"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"),
            cancellable = true,
            require = 0
    )
    @Group(min = 1)
    private void onRenderTransparentBackground(CallbackInfo ci) {
        //noinspection ConstantConditions
        if (InvMove.instance().shouldDisableScreenBackground((Screen) (Object) this)) {
            ci.cancel();
        }
    }
    *///?}

    @Inject(
        method = {
            //? if >=26 {
            "extractBackground*"
            //? } else
            //"renderBackground*"
        },
        at = @At(value = "HEAD"),
        cancellable = true,
        require = 0
    )
    @Group(min = 1)
    private void onRenderBackground(CallbackInfo ci){
        //noinspection ConstantConditions
        if(InvMove.instance().shouldDisableScreenBackground((Screen)(Object)this)) {
            ci.cancel();
        }
    }
}
