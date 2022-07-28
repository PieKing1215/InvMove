package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMove19;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Optional;

public class InvMoveForgeClient {

    static void finishInit() {
        InvMove.instance().finishInit();

        // this is horrible but the class name was changed between 1.19 to 1.19.1 and I want to support both
        @SuppressWarnings("unchecked")
        Optional<Class<? extends Event>> opt_event = Arrays.stream(ScreenEvent.class.getDeclaredClasses())
                .filter(clazz ->
                        clazz.getCanonicalName().equals("net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post")
                     || clazz.getCanonicalName().equals("net.minecraftforge.client.event.ScreenEvent.Render.Post"))
                .findFirst()
                .map(c -> (Class<? extends Event>) c);

        if (opt_event.isPresent()) {
            MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.LOWEST,
                    false,
                    opt_event.get(),
                    e -> InvMove.instance().drawDebugOverlay());
        } else {
            // TODO: use actual logger
            System.err.println("InvMove could not find net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post or net.minecraftforge.client.event.ScreenEvent.Render.Post to hook");
        }
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public void onGUIDrawPost(ScreenEvent.DrawScreenEvent.Post event){
//        InvMove.instance().drawDebugOverlay();
//    }

    static void clientSetup(final FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        MinecraftForge.EVENT_BUS.register(new InvMoveForgeClient());

        InvMove.setInstance(new InvMove19() {
            @Override
            public Optional<String> modidFromClass(Class<?> c) {
                if (c.getPackage().getName().startsWith("net.minecraft.")) {
                    return Optional.of("minecraft");
                }

                return ModList.get().applyForEachModContainer(mod -> {
                    CodeSource src1 = c.getProtectionDomain().getCodeSource();
                    CodeSource src2 = mod.getMod().getClass().getProtectionDomain().getCodeSource();
                    boolean eq = src1 != null && src2 != null && src1.getLocation().equals(src2.getLocation());

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
            public File configDir() {
                return FMLPaths.CONFIGDIR.get().toFile();
            }

            @Override
            protected void registerKeybind(KeyMapping key) {
                key.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                ClientRegistry.registerKeyBinding(key);
            }
        });

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> InvMoveConfig.setupCloth(screen)));
    }
}
