package me.pieking1215.invmove.quilt;

import me.pieking1215.invmove.InvMove;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.nio.file.Files;
import java.util.Optional;

public class InvMoveQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer thisMod) {
        InvMove.modidFromClass = cl -> {
            for (ModContainer mod : QuiltLoader.getAllMods()) {
                if (Files.exists(mod.getPath('/' + cl.getName().replace('.', '/') + ".class"))) {
                    return Optional.of(mod.metadata().id());
                }
            }

            return Optional.empty();
        };
        InvMove.modNameFromModid = modid -> QuiltLoader.getModContainer(modid).map(con -> con.metadata().name()).orElse(modid);
        InvMove.getConfigDir = () -> QuiltLoader.getConfigDir().toFile();

        QuiltLoader.getEntrypointContainers("invmove", InvMoveInitializer.class).forEach(entrypoint -> {
            entrypoint.getEntrypoint().init();
        });

        InvMove.init();

        // TODO: switch to qsl when this merges: https://github.com/QuiltMC/quilt-standard-libraries/pull/59
        KeyBindingHelper.registerKeyBinding(InvMove.TOGGLE_MOVEMENT_KEY);

    }
}
