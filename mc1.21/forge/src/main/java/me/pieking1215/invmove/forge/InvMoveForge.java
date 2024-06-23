package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove21;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(InvMove21.MOD_ID)
public class InvMoveForge {
    public InvMoveForge() {
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(this::imProcess);
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(InvMoveForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            InvMoveForgeClient.finishInit();
        }
    }
}
