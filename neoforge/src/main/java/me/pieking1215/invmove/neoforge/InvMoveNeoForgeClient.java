package me.pieking1215.invmove.neoforge;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLPaths;
//? if >=1.20.5 {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else
/*import net.neoforged.neoforge.client.ConfigScreenHandler;*/
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.security.CodeSource;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InvMoveNeoForgeClient {

    static void finishInit() {
        InvMove.instance().finishInit();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGUIDrawPost(ScreenEvent.Render.Post event){
        InvMove.instance().drawDebugOverlay();
    }

    static void clientSetup(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new InvMoveNeoForgeClient());

        // yikes, but neoforge removed (I think) all methods of getting the @Mod class for a given mod in https://github.com/neoforged/FancyModLoader/pull/126
        Field modClassesField = null;
        try {
            modClassesField = FMLModContainer.class.getDeclaredField("modClasses");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        final Field modClassesFieldFinal = modClassesField;

        InvMove.setInstance(new InvMove() {
            @Override
            protected Optional<String> modidFromClassInternal(Class<?> c) {
                if (c.getPackage().getName().startsWith("net.minecraft.")) {
                    return Optional.of("minecraft");
                }

                return ModList.get().applyForEachModContainer(mod -> {
                    if (mod instanceof FMLModContainer fmlMod && modClassesFieldFinal != null) {
                        CodeSource src1 = c.getProtectionDomain().getCodeSource();
                        CodeSource src2 = null;
                        try {
                            src2 = ((List<Class<?>>) modClassesFieldFinal.get(fmlMod)).get(0).getProtectionDomain().getCodeSource();
                        } catch (IllegalAccessException | NoSuchElementException | IndexOutOfBoundsException e) {
                            return Optional.<String>empty();
                        }
                        boolean eq = src1 != null && src2 != null && src1.getLocation().equals(src2.getLocation());

                        if (eq) {
                            return Optional.of(mod.getModId());
                        }
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
                // TODO: you're supposed to use RegisterKeyMappingsEvent now but it would probably require
                Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, key);
            }
        });

        //? if >=1.20.5 {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mc, screen) -> InvMoveConfig.setupCloth(screen));
        //?} else
        /*ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> InvMoveConfig.setupCloth(screen)));*/
    }
}
