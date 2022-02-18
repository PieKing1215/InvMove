package me.pieking1215.invmove.module.config;

import com.mojang.datafixers.util.Pair;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ModuleConfig {
    String id;
    List<Pair<ConfigEntry<?>, ConfigEntryMeta>> entries;

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

    public record ConfigEntryMeta(String id, String display) {}
}
