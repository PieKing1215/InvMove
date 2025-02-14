package me.pieking1215.invmove.neoforge;

import me.pieking1215.invmove.InvMove;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(InvMove.MOD_ID)
public class InvMoveNeoForge {
    public InvMoveNeoForge() {
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(this::imProcess);
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(InvMoveNeoForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            InvMoveNeoForgeClient.finishInit();
        }
    }
}
