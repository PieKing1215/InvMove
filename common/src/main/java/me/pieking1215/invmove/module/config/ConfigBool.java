package me.pieking1215.invmove.module.config;

import com.google.gson.JsonObject;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.GsonHelper;

import java.util.function.Function;

public class ConfigBool extends ConfigEntry<Boolean> {

    Function<Boolean, Component> textFn = null;

    public ConfigBool(boolean defaultVal) {
        super(defaultVal);
    }

    public ConfigBool textFn(Function<Boolean, Component> textFn) {
        this.textFn = textFn;
        return this;
    }

    @Override
    public void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id) {
        BooleanToggleBuilder b = eb.startBooleanToggle(new TranslatableComponent(id), get()).setDefaultValue(getDefault()).setSaveConsumer(this::set).setTooltip(new TranslatableComponent("tooltip." + id));
        if (this.textFn != null) {
            b.setYesNoTextSupplier(this.textFn);
        }
        category.addEntry(b.build());
    }

    @Override
    public void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id) {
        BooleanToggleBuilder b = eb.startBooleanToggle(new TranslatableComponent(id), get()).setDefaultValue(getDefault()).setSaveConsumer(this::set);
        if (Language.getInstance().has("tooltip." + id)) {
            b.setTooltip(new TranslatableComponent("tooltip." + id));
        }
        if (this.textFn != null) {
            b.setYesNoTextSupplier(this.textFn);
        }
        category.add(b.build());
    }

    @Override
    void write(JsonObject json, String id) {
        json.addProperty(id, this.value);
    }

    @Override
    void read(JsonObject json, String id) {
        if (GsonHelper.isBooleanValue(json, id)) {
            this.value = json.get(id).getAsBoolean();
        }
    }
}
