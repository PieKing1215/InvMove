package me.pieking1215.invmove.module.config;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import me.pieking1215.invmove.InvMoveConfig;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public class ModuleConfig {
    final String id;
    final List<Pair<ConfigEntry<?>, ConfigEntryMeta>> entries;

    public static class ConfigEntryMeta {
        public String id;
        public String display;

        public ConfigEntryMeta(final String id, final String display) {
            this.id = id;
            this.display = display;
        }
    }

    public ModuleConfig(String id) {
        this.id = id;
        this.entries = new ArrayList<>();
    }

    public ConfigBool bool(String display, String id, boolean defaultVal) {
        ConfigBool bool = new ConfigBool(defaultVal);
        this.entries.add(Pair.of(bool, new ConfigEntryMeta(id, display)));
        return bool;
    }

    public ConfigBool bool(String id, boolean defaultVal) {
        ConfigBool bool = new ConfigBool(defaultVal);
        this.entries.add(Pair.of(bool, new ConfigEntryMeta(id, null)));
        return bool;
    }

    public <T extends Enum<T>> ConfigEnum<T> addEnum(String display, String id, T defaultVal) {
        ConfigEnum<T> entry = new ConfigEnum<>(defaultVal);
        this.entries.add(Pair.of(entry, new ConfigEntryMeta(id, display)));
        return entry;
    }

    public <T extends Enum<T>> ConfigEnum<T> addEnum(String id, T defaultVal) {
        ConfigEnum<T> entry = new ConfigEnum<>(defaultVal);
        this.entries.add(Pair.of(entry, new ConfigEntryMeta(id, null)));
        return entry;
    }

    public void label(String id) {
        class None {}

        //noinspection EmptyMethod
        this.entries.add(Pair.of(new ConfigEntry<None>(new None()) {
            @Override
            void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id) {
                category.addEntry(eb.startTextDescription(new TranslatableComponent(id)).build());
            }

            @Override
            void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id) {
                category.add(eb.startTextDescription(new TranslatableComponent(id)).build());
            }

            @SuppressWarnings("EmptyMethod")
            @Override
            void write(JsonObject json, String id) {}

            @SuppressWarnings("EmptyMethod")
            @Override
            void read(JsonObject json, String id) {}
        }, new ConfigEntryMeta(id, null)));
    }

    public void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id) {
        for (Pair<ConfigEntry<?>, ConfigEntryMeta> pair : entries) {
            ConfigEntry<?> entry = pair.getFirst();
            ConfigEntryMeta meta = pair.getSecond();
            entry.addTo(category, eb, (id.isEmpty() ? "" : (id + ".")) + this.id + "." + meta.id);
        }
        if (entries.isEmpty()) {
            category.addEntry(eb.startTextDescription(new TranslatableComponent("key.invmove.module.nooptions").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)).build());
        }
    }

    public void addTo(ConfigCategory category, ConfigEntryBuilder eb) {
        this.addTo(category, eb, "");
    }

    public void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id) {
        for (Pair<ConfigEntry<?>, ConfigEntryMeta> pair : entries) {
            ConfigEntry<?> entry = pair.getFirst();
            ConfigEntryMeta meta = pair.getSecond();
            entry.addTo(category, eb, meta.display == null ? ((id.isEmpty() ? "" : (id + ".")) + this.id + "." + meta.id) : meta.display);
        }
        if (entries.isEmpty()) {
            category.add(eb.startTextDescription(new TranslatableComponent("key.invmove.module.nooptions").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)).build());
        }
    }

    public void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb) {
        this.addTo(category, eb, "");
    }

    public void write(JsonObject json) {
        JsonObject myObj = InvMoveConfig.getOrPutJsonObject(json, this.id);

        for (Pair<ConfigEntry<?>, ConfigEntryMeta> p : entries) {
            p.getFirst().write(myObj, p.getSecond().id);
        }
    }

    public void read(JsonObject json) {
        JsonObject myObj = json.getAsJsonObject(this.id);

        if (myObj != null) {
            for (Pair<ConfigEntry<?>, ConfigEntryMeta> p : entries) {
                p.getFirst().read(myObj, p.getSecond().id);
            }
        }
    }

}
