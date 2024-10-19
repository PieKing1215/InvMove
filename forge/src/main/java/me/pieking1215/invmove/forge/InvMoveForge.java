package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(InvMove.MOD_ID)
public class InvMoveForge {
    public InvMoveForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imProcess);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(InvMoveForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            InvMoveForgeClient.finishInit();
        }
    }
}
