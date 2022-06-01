package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove18;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(InvMove18.MOD_ID)
public class InvMoveForge {
    public InvMoveForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imProcess);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(InvMoveForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> InvMoveForgeClient::finishInit);
    }
}
