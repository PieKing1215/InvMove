package me.pieking1215.invmove.fabric_like.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pieking1215.invmove.InvMove;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class BackgroundMixin {
    @Inject(
            method = "renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;I)V"),
            cancellable = true
    )
    private void onRenderBackground(PoseStack poseStack, CallbackInfo ci){
        //noinspection ConstantConditions
        if(InvMove.shouldDisableScreenBackground((Screen)(Object)this)) {
            ci.cancel();
        }
    }
}
