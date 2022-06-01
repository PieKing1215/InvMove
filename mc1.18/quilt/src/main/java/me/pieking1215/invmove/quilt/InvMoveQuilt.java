package me.pieking1215.invmove.quilt;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMove18;
import me.pieking1215.invmove.fabric_like.InvMoveInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

public class InvMoveQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer thisMod) {
        InvMove.instance = new InvMove18() {
            @Override
            public Optional<String> modidFromClass(Class<?> c) {
                for (ModContainer mod : QuiltLoader.getAllMods()) {
                    if (Files.exists(mod.getPath('/' + c.getName().replace('.', '/') + ".class"))) {
                        return Optional.of(mod.metadata().id());
                    }
                }

                return Optional.empty();
            }

            @Override
            public String modNameFromModid(String modid) {
                return QuiltLoader.getModContainer(modid).map(con -> con.metadata().name()).orElse(modid);
            }

            @Override
            public File configDir() {
                return QuiltLoader.getConfigDir().toFile();
            }

            @Override
            protected void registerKeybind(KeyMapping key) {
                // TODO: switch to qsl when this merges: https://github.com/QuiltMC/quilt-standard-libraries/pull/59
                KeyBindingHelper.registerKeyBinding(key);
            }
        };

        QuiltLoader.getEntrypointContainers("invmove", InvMoveInitializer.class).forEach(entrypoint -> entrypoint.getEntrypoint().init());

        InvMove.instance.finishInit();
    }
}
