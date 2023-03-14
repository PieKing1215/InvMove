package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMove16;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.KeyMapping;
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

import java.io.File;
import java.net.URL;
import java.util.Optional;

public class InvMoveForgeClient {

    static void finishInit() {
        InvMove.instance().finishInit();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGUIDrawPost(GuiScreenEvent.DrawScreenEvent.Post event){
        InvMove.instance().drawDebugOverlay();
    }

    static String modidFromModJarURL(URL url) {
        String path = url.toString(); // modjar://invmove/me/pieking1215/invmove/forge/
        path = path.substring("modjar://".length()); // invmove/me/pieking1215/invmove/forge/
        path = path.split("/")[0]; // invmove
        return path;
    }

    static void clientSetup(final FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> "", (a, b) -> true));
        MinecraftForge.EVENT_BUS.register(new InvMoveForgeClient());

        InvMove.setInstance(new InvMove16() {
            @Override
            protected Optional<String> modidFromClassInternal(Class<?> c) {
                if (c.getPackage().getName().startsWith("net.minecraft.")) {
                    return Optional.of("minecraft");
                }

                return ModList.get().applyForEachModContainer(mod -> {
                    URL src1 = c.getResource(".");
                    URL src2 = mod.getMod().getClass().getResource(".");
                    boolean eq = src1 != null && src2 != null && modidFromModJarURL(src1).equals(modidFromModJarURL(src2));

                    if (eq) {
                        return Optional.of(mod.getModId());
                    }
                    return Optional.<String>empty();
                }).filter(Optional::isPresent).map(Optional::get).findFirst();
            }

            @Override
            public String modNameFromModid(String modid) {
                return ModList.get().getModContainerById(modid).map(con -> con.getModInfo().getDisplayName()).orElse(modid);
            }

            @Override
            public boolean hasMod(String modid) {
                return ModList.get().isLoaded(modid);
            }

            @Override
            public File configDir() {
                return FMLPaths.CONFIGDIR.get().toFile();
            }

            @Override
            protected void registerKeybind(KeyMapping key) {
                key.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                ClientRegistry.registerKeyBinding(key);
            }
        });

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> InvMoveConfig.setupCloth(screen));

    }
}
