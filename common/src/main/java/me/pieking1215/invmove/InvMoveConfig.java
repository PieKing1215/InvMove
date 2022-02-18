package me.pieking1215.invmove;

import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.Modules;
import me.pieking1215.invmove.module.config.ConfigBool;
import me.pieking1215.invmove.module.config.ModuleConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.function.Function;

public class InvMoveConfig {

    public static General GENERAL = new General();
    public static Movement MOVEMENT = new Movement();
    public static Background BACKGROUND = new Background();

    public static final Function<Boolean, Component> MOVEMENT_YES_NO_TEXT = b -> new TextComponent(b ? ChatFormatting.GREEN + "Allow Movement" : ChatFormatting.RED + "Disallow Movement");
    public static final Function<Boolean, Component> BACKGROUND_YES_NO_TEXT = b -> new TextComponent(b ? ChatFormatting.GREEN + "Hide Background" : ChatFormatting.RED + "Show Background");

    public static class General {
        public ModuleConfig cfg = new ModuleConfig("general");
        public final ConfigBool ENABLED = cfg.bool("config.invmove.enable", "enable", true);
        public final ConfigBool DEBUG_DISPLAY = cfg.bool("config.invmove.debugDisplay", "debugDisplay", false);
    }

    public static class Movement {
        public ModuleConfig cfg = new ModuleConfig("movement");
        public final ConfigBool ENABLED = cfg.bool("config.invmove.movement.enable", "enable", true);
        public final ConfigBool JUMP = cfg.bool("config.invmove.movement.jump", "jump", true);
        public final ConfigBool SNEAK = cfg.bool("config.invmove.movement.sneak", "sneak", false);
        public final ConfigBool DISMOUNT = cfg.bool("config.invmove.movement.dismount", "dismount", false);
        public final ConfigBool TEXT_FIELD_DISABLES = cfg.bool("config.invmove.movement.textFieldDisables", "textFieldDisables", true);
    }

    public static class Background {
        public ModuleConfig cfg = new ModuleConfig("background");
        public final ConfigBool BACKGROUND_HIDE = cfg.bool("config.invmove.movement.enable", "enable", true);
    }

    public static Screen setupCloth(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableComponent("config.invmove.title"));
        builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/spruce_planks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("key.invmove.category.general"));
        GENERAL.cfg.addTo(general, eb, "config.invmove");

        // movement
        
        ConfigCategory movement = builder.getOrCreateCategory(new TranslatableComponent("key.invmove.category.movement"));
        MOVEMENT.cfg.addTo(movement, eb, "config.invmove");

        for (Module module : Modules.modules) {
            SubCategoryBuilder cat = eb.startSubCategory(new TranslatableComponent("key.invmove.module." + module.getId()));
            module.getMovementConfig().addTo(cat, eb, "config.invmove." + module.getId() + "");
            movement.addEntry(cat.build());
        }

        // background
        
        ConfigCategory background = builder.getOrCreateCategory(new TranslatableComponent("key.invmove.category.background"));
        BACKGROUND.cfg.addTo(background, eb, "config.invmove");

        for (Module module : Modules.modules) {
            SubCategoryBuilder cat = eb.startSubCategory(new TranslatableComponent("key.invmove.module." + module.getId()));
            module.getBackgroundConfig().addTo(cat, eb, "config.invmove." + module.getId() + "");
            background.addEntry(cat.build());
        }


        return builder.build();
    }
}
