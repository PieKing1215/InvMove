package me.pieking1215.invmove.fabric;

import me.pieking1215.invmove.InvMove;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class InvMoveFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InvMove.modidFromClass = cl -> {
            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                if (mod.findPath('/' + cl.getName().replace('.', '/') + ".class").isPresent()) {
                    return Optional.of(mod.getMetadata().getId());
                }
            }

            return Optional.empty();
        };
        InvMove.modNameFromModid = modid -> FabricLoader.getInstance().getModContainer(modid).map(con -> con.getMetadata().getName()).orElse(modid);
        InvMove.getConfigDir = () -> FabricLoader.getInstance().getConfigDir().toFile();
        InvMove.init();
    }
}
