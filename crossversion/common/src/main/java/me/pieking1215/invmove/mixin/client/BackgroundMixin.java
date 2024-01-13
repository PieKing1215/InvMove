package me.pieking1215.invmove.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pieking1215.invmove.InvMove;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class BackgroundMixin {
    @Group
    @Inject(
        method = {
            "renderBackground*"
        },
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void onRenderBackground(CallbackInfo ci){
        //noinspection ConstantConditions
        if(InvMove.instance().shouldDisableScreenBackground((Screen)(Object)this)) {
            ci.cancel();
        }
    }
}
