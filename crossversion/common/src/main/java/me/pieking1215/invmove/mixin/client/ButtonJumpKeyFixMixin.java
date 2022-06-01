package me.pieking1215.invmove.mixin.client;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractButton.class)
public class ButtonJumpKeyFixMixin {

    @Inject(
            method = "keyPressed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void keyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        // if button recieved space key it normally clicks the button
        // but if the player can jump in this inventory we cancel that
        // (fix for https://github.com/PieKing1215/InvMove/issues/2)
        if (i == 32 && InvMoveConfig.GENERAL.ENABLED.get() && InvMoveConfig.MOVEMENT.ENABLED.get() && InvMoveConfig.MOVEMENT.JUMP.get()) {
            if (InvMove.instance.allowMovementInScreen(Minecraft.getInstance().screen)) {
                cir.setReturnValue(false);
            }
        }
    }

}
