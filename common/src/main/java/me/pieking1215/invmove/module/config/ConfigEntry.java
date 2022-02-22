package me.pieking1215.invmove.module.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;

import java.util.Optional;
import java.util.function.Function;

public abstract class ConfigEntry<T> {

    T defaultValue;
    T value;

    public ConfigEntry(T defaultValue) {
        this.value = this.defaultValue = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T v) {
        this.value = v;
    }

    T getDefault() {
        return defaultValue;
    }

    abstract void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id);
    abstract void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id);
    abstract void write(JsonObject json, String id);
    abstract void read(JsonObject json, String id);
}
