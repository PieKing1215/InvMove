package me.pieking1215.invmove.module.config;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import me.pieking1215.invmove.InvMoveConfig;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public class ModuleConfig {
    String id;
    List<Pair<ConfigEntry<?>, ConfigEntryMeta>> entries;

    public record ConfigEntryMeta(String id, String display) {}

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

    public void label(String id) {
        class None {};
        this.entries.add(Pair.of(new ConfigEntry<>(new None()) {
            @Override
            void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id) {
                category.addEntry(eb.startTextDescription(new TranslatableComponent(id)).build());
            }

            @Override
            void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id) {
                category.add(eb.startTextDescription(new TranslatableComponent(id)).build());
            }

            @Override
            void write(JsonObject json, String id) {}

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
