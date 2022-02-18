package me.pieking1215.invmove.fabric;

import me.pieking1215.invmove.InvMove;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class InvMoveFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        InvMove.init();

        HudRenderCallback.EVENT.register((ms, v) -> {
            InvMove.drawDebugOverlay();
        });
    }
}
