package me.pieking1215.invmove;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.config.ConfigBool;
import me.pieking1215.invmove.module.config.ConfigEnum;
import me.pieking1215.invmove.module.config.ModuleConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class InvMoveConfig {

    public static final Function<Boolean, Component> MOVEMENT_YES_NO_TEXT = b -> InvMove.instance().literalComponent(b ? ChatFormatting.GREEN + "Allow Movement" : ChatFormatting.RED + "Disallow Movement");
    public static final Function<Boolean, Component> BACKGROUND_YES_NO_TEXT = b -> InvMove.instance().literalComponent(b ? ChatFormatting.GREEN + "Hide Background" : ChatFormatting.RED + "Show Background");

    public static final General GENERAL = new General();
    public static final Movement MOVEMENT = new Movement();
    public static final Background BACKGROUND = new Background();

    public static class General {
        public final ModuleConfig cfg = new ModuleConfig("general");
        public final ConfigBool ENABLED = cfg.bool("config.invmove.enable", "enable", true);
        public final ConfigBool DEBUG_DISPLAY = cfg.bool("config.invmove.debugDisplay", "debugDisplay", false);
    }

    public static class Movement {
        public final ModuleConfig cfg = new ModuleConfig("movement");

        public final ConfigBool ENABLED = cfg.bool("enable", true);

        public final ConfigBool JUMP = cfg.bool("jump", true);

        public enum SneakMode {
            Off, Maintain, Pressed
        }
        public final ConfigEnum<SneakMode> SNEAK = cfg.addEnum("sneak", SneakMode.Maintain).setMigrator(element -> GsonHelperFix.isBooleanValue(element) ? Optional.of(element.getAsBoolean() ? SneakMode.Pressed : SneakMode.Maintain) : Optional.empty());

        public final ConfigBool DISMOUNT = cfg.bool("dismount", false);

        public final ConfigBool TEXT_FIELD_DISABLES = cfg.bool("textFieldDisables", true);

        public final ConfigBool UNRECOGNIZED_SCREEN_DEFAULT = cfg.bool("unrecognizedScreenDefault", true).textFn(MOVEMENT_YES_NO_TEXT);

        public final HashMap<String, HashMap<Class<? extends Screen>, Boolean>> unrecognizedScreensAllowMovement = new HashMap<>();
    }

    public static class Background {
        public final ModuleConfig cfg = new ModuleConfig("background");

        public final ConfigBool BACKGROUND_HIDE = cfg.bool("enable", true);

        public enum PauseScreenMode {
            Show, ShowSP, AllowHide
        }
        public final ConfigEnum<PauseScreenMode> HIDE_ON_PAUSE = cfg.addEnum("hideOnPause", PauseScreenMode.Show);

        public final ConfigBool UNRECOGNIZED_SCREEN_DEFAULT = cfg.bool("unrecognizedScreenDefault", true).textFn(BACKGROUND_YES_NO_TEXT);

        public final HashMap<String, HashMap<Class<? extends Screen>, Boolean>> unrecognizedScreensHideBG = new HashMap<>();
    }

    public static Screen setupCloth(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(InvMove.instance().translatableComponent("config.invmove.title"));
        builder.setDefaultBackgroundTexture(InvMove.instance().parseResource("minecraft:textures/block/spruce_planks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(InvMove.instance().translatableComponent("key.invmove.category.general"));
        GENERAL.cfg.addTo(general, eb, "config.invmove");

        // movement
        
        ConfigCategory movement = builder.getOrCreateCategory(InvMove.instance().translatableComponent("key.invmove.category.movement"));
        MOVEMENT.cfg.addTo(movement, eb, "config.invmove");

        for (Module module : InvMove.instance().modules) {
            SubCategoryBuilder cat = eb.startSubCategory(InvMove.instance().fromCV(module.getTitle()));
            module.getMovementConfig().addTo(cat, eb, "config.invmove." + module.getId() + "");
            movement.addEntry(cat.build());
        }

        // unrecognized

        movement.addEntry(eb.startTextDescription(InvMove.instance().translatableComponent("key.invmove.unrecognized").withStyle(ChatFormatting.UNDERLINE)).build());
        if (MOVEMENT.unrecognizedScreensAllowMovement.isEmpty())
            movement.addEntry(eb.startTextDescription(InvMove.instance().translatableComponent("key.invmove.unrecognized.desc").withStyle(ChatFormatting.GRAY)).build());

        for (String modid : MOVEMENT.unrecognizedScreensAllowMovement.keySet()) {
            HashMap<Class<? extends Screen>, Boolean> screens = MOVEMENT.unrecognizedScreensAllowMovement.get(modid);

            SubCategoryBuilder cat = eb.startSubCategory(modid.equals("?unknown") ? InvMove.instance().translatableComponent("key.invmove.unrecognized.nomod") : InvMove.instance().literalComponent(InvMove.instance().modNameFromModid(modid)));
            cat.setTooltip(InvMove.instance().literalComponent(ChatFormatting.GRAY + "modid: " + modid));

            for (Class<? extends Screen> cl : screens.keySet()) {
                cat.add(eb.startBooleanToggle(
                        InvMove.instance().literalComponent(cl.getSimpleName()),
                        screens.get(cl)
                    )
                    .setTooltip(InvMove.instance().literalComponent(ChatFormatting.GRAY + cl.getName()))
                    .setYesNoTextSupplier(MOVEMENT_YES_NO_TEXT)
                    .setSaveConsumer(v -> screens.put(cl, v))
                .build());
            }

            movement.addEntry(cat.build());
        }

        // background
        
        ConfigCategory background = builder.getOrCreateCategory(InvMove.instance().translatableComponent("key.invmove.category.background"));
        BACKGROUND.cfg.addTo(background, eb, "config.invmove");

        for (Module module : InvMove.instance().modules) {
            SubCategoryBuilder cat = eb.startSubCategory(InvMove.instance().fromCV(module.getTitle()));
            module.getBackgroundConfig().addTo(cat, eb, "config.invmove." + module.getId() + "");
            background.addEntry(cat.build());
        }

        // unrecognized

        background.addEntry(eb.startTextDescription(InvMove.instance().translatableComponent("key.invmove.unrecognized").withStyle(ChatFormatting.UNDERLINE)).build());
        if (BACKGROUND.unrecognizedScreensHideBG.isEmpty())
            background.addEntry(eb.startTextDescription(InvMove.instance().translatableComponent("key.invmove.unrecognized.desc").withStyle(ChatFormatting.GRAY)).build());

        for (String modid : BACKGROUND.unrecognizedScreensHideBG.keySet()) {
            HashMap<Class<? extends Screen>, Boolean> screens = BACKGROUND.unrecognizedScreensHideBG.get(modid);

            SubCategoryBuilder cat = eb.startSubCategory(modid.equals("?unknown") ? InvMove.instance().translatableComponent("key.invmove.unrecognized.nomod") : InvMove.instance().literalComponent(InvMove.instance().modNameFromModid(modid)));
            cat.setTooltip(InvMove.instance().literalComponent(ChatFormatting.GRAY + "modid: " + modid));

            for (Class<? extends Screen> cl : screens.keySet()) {
                cat.add(eb.startBooleanToggle(
                        InvMove.instance().literalComponent(cl.getSimpleName()),
                        screens.get(cl)
                    )
                    .setTooltip(InvMove.instance().literalComponent(ChatFormatting.GRAY + cl.getName()))
                    .setSaveConsumer(v -> screens.put(cl, v))
                    .setYesNoTextSupplier(BACKGROUND_YES_NO_TEXT)
                    .build());
            }

            background.addEntry(cat.build());
        }

        // save

        return builder.setSavingRunnable(InvMoveConfig::save).build();
    }

    /**
     * Appends "_Old" to any config files/folders from the old version of InvMove.
     * Such files include "invMove/" (new version uses "invmove/"), invmove-client.toml, or invmove.json if it has old keys
     */
    private static void moveOldConfig() {
        try {
            File configDir = InvMove.instance().configDir();
            if (configDir != null) {
                File invmoveDir = new File(configDir, "invMove/");
                if (invmoveDir.exists()) {
                    // check capitalization since `new File` is case insensitive on some platforms
                    if (invmoveDir.getCanonicalPath().endsWith("invMove")) {
                        //noinspection ResultOfMethodCallIgnored
                        invmoveDir.renameTo(new File(invmoveDir.getParent(), "invMove_Old/"));
                    }
                }

                File invmoveConfOldForge = new File(configDir, "invmove-client.toml");
                if (invmoveConfOldForge.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    invmoveConfOldForge.renameTo(new File(configDir, "invmove-client_Old.toml"));
                }

                File invmoveConf = new File(configDir, "invmove.json");
                if (invmoveConf.exists()) {
                    JsonReader jr = new JsonReader(new FileReader(invmoveConf));
                    JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
                    jr.close();
                    if (jsonEl != null) {
                        JsonObject json = jsonEl.getAsJsonObject();

                        // we don't store this in the root any more
                        if (json.has("uiBackground")) {
                            //noinspection ResultOfMethodCallIgnored
                            invmoveConf.renameTo(new File(configDir, "invmove_Old.json"));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            File configDir = InvMove.instance().configDir();
            if (configDir != null) {

                moveOldConfig();

                // main config

                File configFile = new File(configDir, "invmove.json");

                if (!configFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();
                }

                if (configFile.exists()) {
                    writeMainConfig(configFile);
                }

                // module configs

                for (Module module : InvMove.instance().modules) {
                    File modFile = new File(configDir, "invmove/" + module.getId() + ".json");
                    if (!modFile.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        modFile.getParentFile().mkdirs();
                        //noinspection ResultOfMethodCallIgnored
                        modFile.createNewFile();
                    }

                    if (modFile.exists()) {
                        writeModuleConfig(module, modFile);
                    }
                }

                // unrecognized screens config

                File unrecognizedFile = new File(configDir, "invmove/unrecognized.json");
                if (!unrecognizedFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    unrecognizedFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    unrecognizedFile.createNewFile();
                }

                if (unrecognizedFile.exists()) {
                    writeUnrecognizedConfig(unrecognizedFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            File configDir = InvMove.instance().configDir();
            if (configDir != null) {

                moveOldConfig();

                // main config

                File configFile = new File(configDir, "invmove.json");

                if (!configFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();

                    if (configFile.exists()) {
                        writeMainConfig(configFile);
                    }
                }

                if (configFile.exists()) {
                    readMainConfig(configFile);
                }

                // module configs

                for (Module module : InvMove.instance().modules) {
                    File modFile = new File(configDir, "invmove/" + module.getId() + ".json");
                    if (!modFile.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        modFile.getParentFile().mkdirs();
                        //noinspection ResultOfMethodCallIgnored
                        modFile.createNewFile();

                        if (modFile.exists()) {
                            writeModuleConfig(module, modFile);
                        }
                    }

                    if (modFile.exists()) {
                        readModuleConfig(module, modFile);
                    }
                }

                // unrecognized screens config

                File unrecognizedFile = new File(configDir, "invmove/unrecognized.json");
                if (!unrecognizedFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    unrecognizedFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    unrecognizedFile.createNewFile();

                    if (unrecognizedFile.exists()) {
                        writeUnrecognizedConfig(unrecognizedFile);
                    }
                }

                if (unrecognizedFile.exists()) {
                    readUnrecognizedConfig(unrecognizedFile);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeMainConfig(File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        JsonObject json = jsonEl == null ? new JsonObject() : jsonEl.getAsJsonObject();

        GENERAL.cfg.write(json);
        MOVEMENT.cfg.write(json);
        BACKGROUND.cfg.write(json);

        JsonWriter jw = new JsonWriter(new FileWriter(file));
        jw.setIndent("  ");
        new Gson().toJson(json, jw);
        jw.close();
    }

    private static void readMainConfig(File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        if (jsonEl != null) {
            JsonObject json = jsonEl.getAsJsonObject();

            GENERAL.cfg.read(json);
            MOVEMENT.cfg.read(json);
            BACKGROUND.cfg.read(json);
        }
    }

    private static void writeModuleConfig(Module mod, File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        JsonObject json = jsonEl == null ? new JsonObject() : jsonEl.getAsJsonObject();

        mod.getMovementConfig().write(json);
        mod.getBackgroundConfig().write(json);

        JsonWriter jw = new JsonWriter(new FileWriter(file));
        jw.setIndent("  ");
        new Gson().toJson(json, jw);
        jw.close();
    }

    private static void readModuleConfig(Module mod, File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        if (jsonEl != null) {
            JsonObject json = jsonEl.getAsJsonObject();

            mod.getMovementConfig().read(json);
            mod.getBackgroundConfig().read(json);
        }
    }

    private static void writeUnrecognizedConfig(File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        JsonObject json = jsonEl == null ? new JsonObject() : jsonEl.getAsJsonObject();

        JsonObject movement = getOrPutJsonObject(json, "allowMovement");
        for (String modid : MOVEMENT.unrecognizedScreensAllowMovement.keySet()) {
            HashMap<Class<? extends Screen>, Boolean> v = MOVEMENT.unrecognizedScreensAllowMovement.get(modid);
            for (Class<? extends Screen> cl : v.keySet()) {
                movement.addProperty(cl.getName(), v.get(cl));
            }
        }

        JsonObject background = getOrPutJsonObject(json, "hideBackground");
        for (String modid : BACKGROUND.unrecognizedScreensHideBG.keySet()) {
            HashMap<Class<? extends Screen>, Boolean> v = BACKGROUND.unrecognizedScreensHideBG.get(modid);
            for (Class<? extends Screen> cl : v.keySet()) {
                background.addProperty(cl.getName(), v.get(cl));
            }
        }

        JsonWriter jw = new JsonWriter(new FileWriter(file));
        jw.setIndent("  ");
        new Gson().toJson(json, jw);
        jw.close();
    }

    private static void readUnrecognizedConfig(File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        if (jsonEl != null) {
            JsonObject json = jsonEl.getAsJsonObject();

            JsonObject movement = json.getAsJsonObject("allowMovement");
            if (movement != null) {
                for (Map.Entry<String, JsonElement> entry : movement.entrySet()) {
                    if (GsonHelperFix.isBooleanValue(entry.getValue())) {
                        try {
                            // important that we don't initialize the class here because it causes crashes with mods
                            //   that try to interact with MC classes in static initializers, etc
                            // TODO: do this a less hacky way
                            Class<?> cl = Class.forName(entry.getKey(), false, InvMoveConfig.class.getClassLoader());
                            if (Screen.class.isAssignableFrom(cl)) {
                                String modid = InvMove.instance().modidFromClass(cl).orElse("?unknown");
                                InvMoveConfig.MOVEMENT.unrecognizedScreensAllowMovement.putIfAbsent(modid, new HashMap<>());
                                HashMap<Class<? extends Screen>, Boolean> hm = InvMoveConfig.MOVEMENT.unrecognizedScreensAllowMovement.get(modid);
                                //noinspection unchecked
                                hm.putIfAbsent((Class<? extends Screen>) cl, entry.getValue().getAsBoolean());
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}
                    }
                }
            }

            JsonObject background = json.getAsJsonObject("hideBackground");
            if (background != null) {
                for (Map.Entry<String, JsonElement> entry : background.entrySet()) {
                    if (GsonHelperFix.isBooleanValue(entry.getValue())) {
                        try {
                            // important that we don't initialize the class here because it causes crashes with mods
                            //   that try to interact with MC classes in static initializers, etc
                            // TODO: do this a less hacky way
                            Class<?> cl = Class.forName(entry.getKey(), false, InvMoveConfig.class.getClassLoader());
                            if (Screen.class.isAssignableFrom(cl)) {
                                String modid = InvMove.instance().modidFromClass(cl).orElse("?unknown");
                                InvMoveConfig.BACKGROUND.unrecognizedScreensHideBG.putIfAbsent(modid, new HashMap<>());
                                HashMap<Class<? extends Screen>, Boolean> hm = InvMoveConfig.BACKGROUND.unrecognizedScreensHideBG.get(modid);
                                //noinspection unchecked
                                hm.putIfAbsent((Class<? extends Screen>) cl, entry.getValue().getAsBoolean());
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}
                    }
                }
            }
        }
    }

    public static JsonObject getOrPutJsonObject(JsonObject parent, String id) {
        if (!GsonHelperFix.isObjectNode(parent, id)) {
            parent.add(id, new JsonObject());
        }

        return parent.getAsJsonObject(id);
    }
}
