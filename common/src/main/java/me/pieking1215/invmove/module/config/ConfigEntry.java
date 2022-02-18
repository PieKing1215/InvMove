package me.pieking1215.invmove.module.config;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

public abstract class ConfigEntry<T> {

    T defaultValue;
    T value;

    public ConfigEntry(T defaultValue) {
        this.value = this.defaultValue = defaultValue;
    }

    public T get() {
        return value;
    }

    void set(T v) {
        this.value = v;
    }

    T getDefault() {
        return defaultValue;
    }

    abstract void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id);
    abstract void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id);
}
