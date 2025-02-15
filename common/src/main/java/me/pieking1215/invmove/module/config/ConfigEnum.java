package me.pieking1215.invmove.module.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pieking1215.invmove.InvMove;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.locale.Language;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class ConfigEnum<T extends Enum<T>> extends ConfigEntry<T> {

    // TODO: refactor this
    Function<JsonElement, Optional<T>> migrator = null;

    public ConfigEnum(@NotNull T defaultValue) {
        super(defaultValue);
    }

    public ConfigEnum<T> setMigrator(Function<JsonElement, Optional<T>> migrator) {
        this.migrator = migrator;
        return this;
    }

    @Override
    void addTo(ConfigCategory category, ConfigEntryBuilder eb, String id) {
        //noinspection unchecked
        EnumSelectorBuilder<T> esb = eb.startEnumSelector(InvMove.instance().translatableComponent(id), (Class<T>) this.defaultValue.getClass(), this.value)
            .setDefaultValue(this.getDefault())
            .setSaveConsumer(this::set)
            .setTooltip(InvMove.instance().translatableComponent("tooltip." + id));

        category.addEntry(esb.build());
    }

    @Override
    void addTo(SubCategoryBuilder category, ConfigEntryBuilder eb, String id) {
        //noinspection unchecked
        EnumSelectorBuilder<T> esb = eb.startEnumSelector(InvMove.instance().translatableComponent(id), (Class<T>) this.defaultValue.getClass(), this.value)
                .setDefaultValue(this.getDefault())
                .setSaveConsumer(this::set)
                .setTooltip(InvMove.instance().translatableComponent("tooltip." + id));
        if (Language.getInstance().has("tooltip." + id)) {
            esb.setTooltip(InvMove.instance().translatableComponent("tooltip." + id));
        }
        category.add(esb.build());
    }

    @Override
    void write(JsonObject json, String id) {
        json.addProperty(id, this.value.toString());
    }

    @Override
    void read(JsonObject json, String id) {
        Optional<T> migrate = this.migrator == null ? Optional.empty() : this.migrator.apply(json.get(id));
        if (migrate.isPresent()) {
            this.value = migrate.get();
        } else if (GsonHelper.isStringValue(json, id)) {
            try {
                //noinspection unchecked
                this.value = Enum.valueOf((Class<T>) this.defaultValue.getClass(), json.get(id).getAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
