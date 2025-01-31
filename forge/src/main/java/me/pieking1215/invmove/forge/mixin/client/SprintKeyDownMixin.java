package me.pieking1215.invmove.forge.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class SprintKeyDownMixin {
    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z")
    )
    private boolean isDown(KeyMapping self) {
        // (neo)forge does extra checks that make it not work in inventories
        return InvMove.instance().withRawKeyDown(self::isDown);
    }
}
