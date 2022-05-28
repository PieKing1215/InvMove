package me.pieking1215.invmove.fabric.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MovementMixin {
    @Inject(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V")
    )
    private void onInput(CallbackInfo info) {
        //noinspection ConstantConditions
        InvMove.onInputUpdate(((LocalPlayer)(Object)this).input);
    }
}
