package me.pieking1215.invmove.fabric;

import me.pieking1215.invmove.InvMove;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.KeyMapping;

import java.io.File;
import java.util.Optional;

public class InvMoveFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InvMove.setInstance(new InvMove() {
            @Override
            protected Optional<String> modidFromClassInternal(Class<?> c) {
                if (c.getPackage().getName().startsWith("net.minecraft.")) {
                    return Optional.of("minecraft");
                }

                for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                    if (mod.findPath('/' + c.getName().replace('.', '/') + ".class").isPresent()) {
                        return Optional.of(mod.getMetadata().getId());
                    }
                }

                return Optional.empty();
            }

            @Override
            public String modNameFromModid(String modid) {
                return FabricLoader.getInstance().getModContainer(modid).map(con -> con.getMetadata().getName()).orElse(modid);
            }

            @Override
            public boolean hasMod(String modid) {
                return FabricLoader.getInstance().isModLoaded(modid);
            }

            @Override
            public File configDir() {
                return FabricLoader.getInstance().getConfigDir().toFile();
            }

            @Override
            protected void registerKeybind(KeyMapping key) {
                KeyBindingHelper.registerKeyBinding(key);
            }
        });

        FabricLoader.getInstance().getEntrypointContainers("invmove", InvMoveInitializer.class).forEach(entrypoint -> entrypoint.getEntrypoint().init());

        InvMove.instance().finishInit();
    }
}
