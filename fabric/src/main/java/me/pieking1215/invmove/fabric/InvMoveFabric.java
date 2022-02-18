package me.pieking1215.invmove.fabric;

import me.pieking1215.invmove.InvMove;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.Optional;

public class InvMoveFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        InvMove.init();
        InvMove.modidFromClass = cl -> {
            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                if (mod.findPath('/' + cl.getName().replace('.', '/') + ".class").isPresent()) {
                    return Optional.of(mod.getMetadata().getId());
                }
            }

            return Optional.empty();
        };
    }
}
