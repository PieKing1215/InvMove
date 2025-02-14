package me.pieking1215.invmove.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pieking1215.invmove.InvMove;
import net.minecraft.client.player./*$ Input {*/ClientInput/*$}*/;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class MovementMixin {
    @WrapOperation(
            method = "aiStep",
            //? if >=1.21.4 {
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V")
            //?} else if >=1.21.2 {
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick(ZF)V")
            *///?} else if >=1.19 {
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V")
            *///?} else
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(Z)V")*/
    )
    private void onTick(
            /*$ Input {*/ClientInput/*$}*/ instance,

            //? if >=1.21.4 {
            //?} else if >=1.19 {
            /*boolean sneaking, float sneakSpeed,
            *///?} else
            /*boolean sneaking,*/

            Operation<Void> original) {

        //? if >=1.21.4 {
        original.call(instance);
        InvMove.instance().onInputUpdate(instance);
        //?} else if >=1.19 {
        /*original.call(instance, sneaking, sneakSpeed);
        InvMove.instance().onInputUpdate(instance, sneaking, sneakSpeed);
        *///?} else {
        /*original.call(instance, sneaking);
        InvMove.instance().onInputUpdate(instance, sneaking);
        *///?}
    }
}