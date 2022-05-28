package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.security.CodeSource;
import java.util.Optional;

public class InvMoveForgeClient {

    static void init() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> "", (a, b) -> true));
        MinecraftForge.EVENT_BUS.register(new InvMoveForgeClient());
        InvMove.modidFromClass = cl -> {
            if (cl.getPackage().getName().startsWith("net.minecraft.")) {
                return Optional.of("minecraft");
            }

            return ModList.get().applyForEachModContainer(mod -> {
                CodeSource src1 = cl.getProtectionDomain().getCodeSource();
                CodeSource src2 = mod.getMod().getClass().getProtectionDomain().getCodeSource();
                boolean eq = src1 != null && src2 != null && src1.getLocation().equals(src2.getLocation());

                if (eq) {
                    return Optional.of(mod.getModId());
                }
                return Optional.<String>empty();
            }).filter(Optional::isPresent).map(Optional::get).findFirst();
        };
        InvMove.modNameFromModid = modid -> ModList.get().getModContainerById(modid).map(con -> con.getModInfo().getDisplayName()).orElse(modid);
        InvMove.getConfigDir = () -> FMLPaths.CONFIGDIR.get().toFile();
        InvMove.init();

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> InvMoveConfig.setupCloth(screen));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGUIDrawPost(GuiScreenEvent.DrawScreenEvent.Post event){
        InvMove.drawDebugOverlay();
    }

    static void clientSetup(final FMLClientSetupEvent event) {
        InvMove.TOGGLE_MOVEMENT_KEY.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
        ClientRegistry.registerKeyBinding(InvMove.TOGGLE_MOVEMENT_KEY);
    }
}
