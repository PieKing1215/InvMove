package me.pieking1215.invmove.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pieking1215.invmove.InvMove;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LocalPlayer.class)
public class MovementMixin {
    @WrapOperation(
            method = "aiStep",
            //? if >=1.17 {
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V")
            //?} else
            /*at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(Z)V")*/
    )
    private void onTick(Input instance, boolean sneaking/*? if >=1.17 {*/, float sneakSpeed/*?}*/, Operation<Void> original) {
        original.call(instance, sneaking/*? if >=1.17 {*/, sneakSpeed/*?}*/);

        //noinspection ConstantConditions
        InvMove.instance().onInputUpdate(instance, sneaking/*? if >=1.17 {*/, sneakSpeed/*?}*/);
    }
}
