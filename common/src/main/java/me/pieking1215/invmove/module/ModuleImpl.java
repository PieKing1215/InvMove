package me.pieking1215.invmove.module;

import me.pieking1215.invmove.InvMoveConfig;
import me.pieking1215.invmove.module.config.ConfigBool;
import me.pieking1215.invmove.module.config.ModuleConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Basic implementation helper for Modules.
 * See {@link VanillaModule} for a usage example
 */
public abstract class ModuleImpl implements Module {

    protected final ModuleConfig m_config = new ModuleConfig("movement");
    protected final Map<Class<?>, ConfigBool> classMovementConfig = new HashMap<>();
    protected final Map<Class<?>, Movement> classMovementHardcoded = new HashMap<>();

    protected final ModuleConfig bg_config = new ModuleConfig("backgroundHide");
    protected final Map<Class<?>, ConfigBool> classBackgroundHideConfig = new HashMap<>();
    protected final Map<Class<?>, Background> classBackgroundHideHardcoded = new HashMap<>();

    protected Registering register(Class<?>... clazz) {
        return new Registering(clazz);
    }

    @SuppressWarnings("unused")
    protected class Registering {
        final Class<?>[] clazz;

        protected Registering(Class<?>... clazz) {
            this.clazz = clazz;
        }

        public Registering movement(Movement movement) {
            for (Class<?> aClass : clazz) {
                classMovementHardcoded.put(aClass, movement);
            }
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Registering background(Background background) {
            for (Class<?> aClass : clazz) {
                classBackgroundHideHardcoded.put(aClass, background);
            }
            return this;
        }

        public ConfigMovement cfgMovement(String id) {
            return new ConfigMovement(this, id);
        }

        public ConfigBackgroundHide cfgBackground(String id) {
            return new ConfigBackgroundHide(this, id);
        }

        public ConfigBoth cfg(String id) {
            return new ConfigBoth(this, id);
        }
    }

    @SuppressWarnings("unused")
    protected class ConfigMovement {
        final Registering reg;
        final String id;
        boolean def = true;
        Function<Boolean, Component> textFn = InvMoveConfig.MOVEMENT_YES_NO_TEXT;
        String display = null;

        protected ConfigMovement(Registering reg, String id) {
            this.reg = reg;
            this.id = id;
        }

        public Registering submit() {
            if (this.display != null) {
                ConfigBool b = m_config.bool(this.display, this.id, this.def).textFn(textFn);
                for (Class<?> clazz : reg.clazz) {
                    classMovementConfig.put(clazz, b);
                }
            } else {
                ConfigBool b = m_config.bool(this.id, this.def).textFn(textFn);
                for (Class<?> clazz : reg.clazz) {
                    classMovementConfig.put(clazz, b);
                }
            }
            return this.reg;
        }

        public ConfigMovement display(String display) {
            this.display = display;
            return this;
        }

        public ConfigMovement setDefault(boolean def) {
            this.def = def;
            return this;
        }

        public ConfigMovement textFn(Function<Boolean, Component> textFn) {
            this.textFn = textFn;
            return this;
        }
    }

    @SuppressWarnings("unused")
    protected class ConfigBackgroundHide {
        final Registering reg;
        final String id;
        boolean def = true;
        Function<Boolean, Component> textFn = InvMoveConfig.BACKGROUND_YES_NO_TEXT;
        String display = null;

        protected ConfigBackgroundHide(Registering reg, String id) {
            this.reg = reg;
            this.id = id;
        }

        public Registering submit() {
            if (this.display != null) {
                ConfigBool b = m_config.bool(this.display, this.id, this.def).textFn(textFn);
                for (Class<?> clazz : reg.clazz) {
                    classBackgroundHideConfig.put(clazz, b);
                }
            } else {
                ConfigBool b = m_config.bool(this.id, this.def).textFn(textFn);
                for (Class<?> clazz : reg.clazz) {
                    classBackgroundHideConfig.put(clazz, b);
                }
            }
            return this.reg;
        }

        public ConfigBackgroundHide display(String display) {
            this.display = display;
            return this;
        }

        public ConfigBackgroundHide setDefault(boolean def) {
            this.def = def;
            return this;
        }

        public ConfigBackgroundHide textFn(Function<Boolean, Component> textFn) {
            this.textFn = textFn;
            return this;
        }
    }

    @SuppressWarnings("unused")
    protected class ConfigBoth {
        final Registering reg;
        final String id;
        boolean defMv = true;
        boolean defBg = true;
        Function<Boolean, Component> textFnMv = InvMoveConfig.MOVEMENT_YES_NO_TEXT;
        Function<Boolean, Component> textFnBg = InvMoveConfig.BACKGROUND_YES_NO_TEXT;
        String display = null;

        protected ConfigBoth(Registering reg, String id) {
            this.reg = reg;
            this.id = id;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Registering submit() {
            if (this.display != null) {
                ConfigBool mv = m_config.bool(this.display, this.id, this.defMv).textFn(textFnMv);
                ConfigBool bg = bg_config.bool(this.display, this.id, this.defBg).textFn(textFnBg);
                for (Class<?> clazz : reg.clazz) {
                    classMovementConfig.put(clazz, mv);
                    classBackgroundHideConfig.put(clazz, bg);
                }
            } else {
                ConfigBool mv = m_config.bool(this.id, this.defMv).textFn(textFnMv);
                ConfigBool bg = bg_config.bool(this.id, this.defBg).textFn(textFnBg);
                for (Class<?> clazz : reg.clazz) {
                    classMovementConfig.put(clazz, mv);
                    classBackgroundHideConfig.put(clazz, bg);
                }
            }
            return this.reg;
        }

        public ConfigBoth display(String display) {
            this.display = display;
            return this;
        }

        public ConfigBoth setDefaults(boolean defMv, boolean defBg) {
            this.defMv = defMv;
            this.defBg = defBg;
            return this;
        }

        public ConfigBoth textFns(Function<Boolean, Component> textFnMv, Function<Boolean, Component> textFnBg) {
            this.textFnMv = textFnMv;
            this.textFnBg = textFnBg;
            return this;
        }
    }

    @Override
    public Movement shouldAllowMovement(Screen screen) {
        Optional<Movement> hard = classMovementHardcoded.entrySet().stream().filter(e -> e.getKey().isInstance(screen)).map(Map.Entry::getValue).findFirst();
        if (hard.isPresent()) {
            return hard.get();
        }

        Optional<ConfigBool> config = classMovementConfig.entrySet().stream().filter(e -> e.getKey().isInstance(screen)).map(Map.Entry::getValue).findFirst();
        //noinspection OptionalIsPresent
        if (config.isPresent()) {
            return config.get().get() ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        }

        return Movement.PASS;
    }

    @Override
    public Background shouldHideBackground(Screen screen) {
        Optional<Background> hard = classBackgroundHideHardcoded.entrySet().stream().filter(e -> e.getKey().isInstance(screen)).map(Map.Entry::getValue).findFirst();
        if (hard.isPresent()) {
            return hard.get();
        }

        Optional<ConfigBool> config = classBackgroundHideConfig.entrySet().stream().filter(e -> e.getKey().isInstance(screen)).map(Map.Entry::getValue).findFirst();
        //noinspection OptionalIsPresent
        if (config.isPresent()) {
            return config.get().get() ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        }

        return Background.PASS;
    }

    @Override
    public ModuleConfig getMovementConfig() {
        return m_config;
    }

    @Override
    public ModuleConfig getBackgroundConfig() {
        return bg_config;
    }
}
