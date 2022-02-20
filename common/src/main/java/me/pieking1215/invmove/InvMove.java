package me.pieking1215.invmove;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.Modules;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class InvMove {
    public static final String MOD_ID = "invmove";

    public static Function<Class<?>, Optional<String>> modidFromClass = c -> Optional.empty();
    public static Function<String, String> modNameFromModid = s -> s;
    public static Supplier<File> getConfigDir = () -> null;

    public static void init() {
        Modules.init();
        InvMoveConfig.load();
    }

    public static void onInputUpdate(Input input){
        if(Minecraft.getInstance().player == null) return;
        if(allowMovementInScreen(Minecraft.getInstance().screen)){

            // tick keybinds (since opening the ui unpresses all keys)
            KeyMapping.setAll();

            // this is needed for compatibility with ItemPhysic
            Minecraft.getInstance().options.keyDrop.setDown(false);

            // tick movement
            manualTickMovement(input, Minecraft.getInstance().player.isMovingSlowly(), Minecraft.getInstance().player.isSpectator());

            // set sprinting using raw keybind data
            if(!Minecraft.getInstance().player.isSprinting()) {
                Minecraft.getInstance().player.setSprinting(rawIsKeyDown(Minecraft.getInstance().options.keySprint));
            }

        }else if(Minecraft.getInstance().screen != null){
            KeyMapping.releaseAll();
        }
    }

    public static boolean allowMovementInScreen(Screen screen) {
        if(screen == null) return false;

        if(Minecraft.getInstance().player == null) return false;

        if(!InvMoveConfig.GENERAL.ENABLED.get()) return false;
        if(!InvMoveConfig.MOVEMENT.ENABLED.get()) return false;

        if(screen.isPauseScreen() && Minecraft.getInstance().hasSingleplayerServer()){
            if(Minecraft.getInstance().getSingleplayerServer() != null) {
                if (!Minecraft.getInstance().getSingleplayerServer().isPublished()) return false;
            } else {
                return false;
            }
        }

        Optional<Boolean> movement = Optional.empty();
        modules: for (Module mod : Modules.modules) {
            Module.Movement res = mod.shouldAllowMovement(screen);
            switch (res) {
                case PASS -> {}
                case FORCE_ENABLE -> {
                    movement = Optional.of(true);
                    break modules;
                }
                case FORCE_DISABLE -> {
                    movement = Optional.of(false);
                    break modules;
                }
                case SUGGEST_ENABLE -> movement = Optional.of(true);
                case SUGGEST_DISABLE -> movement = Optional.of(false);
            }
        }

        if (movement.isEmpty()) {
            Class<? extends Screen> cl = screen.getClass();
            String modid = modidFromClass.apply(cl).orElse("?unknown");
            InvMoveConfig.MOVEMENT.unrecognizedScreensAllowMovement.putIfAbsent(modid, new HashMap<>());
            HashMap<Class<? extends Screen>, Boolean> hm = InvMoveConfig.MOVEMENT.unrecognizedScreensAllowMovement.get(modid);

            if (!hm.containsKey(cl)) {
                hm.put(cl, true);
                InvMoveConfig.save();
            }

            return hm.get(cl);
        } else {
            return movement.get();
        }
    }

    public static Field[] getDeclaredFieldsSuper(Class<?> aClass) {
        List<Field> fs = new ArrayList<>();

        do{
            fs.addAll(Arrays.asList(aClass.getDeclaredFields()));
        }while((aClass = aClass.getSuperclass()) != null);

        return fs.toArray(new Field[0]);
    }

    /**
     * Clone of Input.tick but uses raw keybind data
     */
    public static void manualTickMovement(Input input, boolean slow, boolean noDampening) {

        input.up = rawIsKeyDown(Minecraft.getInstance().options.keyUp);
        input.down = rawIsKeyDown(Minecraft.getInstance().options.keyDown);
        input.left = rawIsKeyDown(Minecraft.getInstance().options.keyLeft);
        input.right = rawIsKeyDown(Minecraft.getInstance().options.keyRight);
        input.forwardImpulse = input.up == input.down ? 0.0F : (float)(input.up ? 1 : -1);
        input.leftImpulse = input.left == input.right ? 0.0F : (float)(input.left ? 1 : -1);
        input.jumping = rawIsKeyDown(Minecraft.getInstance().options.keyJump) && InvMoveConfig.MOVEMENT.JUMP.get();

        boolean allowSneak = InvMoveConfig.MOVEMENT.SNEAK.get();
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isPassenger()){
            allowSneak = InvMoveConfig.MOVEMENT.DISMOUNT.get();
        }
        input.shiftKeyDown = rawIsKeyDown(Minecraft.getInstance().options.keyShift) && allowSneak;
        if (!noDampening && (input.shiftKeyDown || slow)) {
            input.leftImpulse = (float)((double)input.leftImpulse * 0.3D);
            input.forwardImpulse = (float)((double)input.forwardImpulse * 0.3D);
        }
    }

    public static boolean rawIsKeyDown(KeyMapping key){
        // this field is accesswidened
        // can't use the method because it has extra conditions in forge
        return key.isDown;
    }

    public static boolean shouldDisableScreenBackground(Screen screen) {

        if(Minecraft.getInstance().player == null) return false;

        if(!InvMoveConfig.GENERAL.ENABLED.get()) return false;

        if(!InvMoveConfig.BACKGROUND.BACKGROUND_HIDE.get()) return false;

        if(screen == null) return false;

        if(screen.isPauseScreen() && Minecraft.getInstance().hasSingleplayerServer()){
            if(Minecraft.getInstance().getSingleplayerServer() != null) {
                if (!Minecraft.getInstance().getSingleplayerServer().isPublished()) return false;
            } else {
                return false;
            }
        }

        Optional<Boolean> show = Optional.empty();
        modules: for (Module mod : Modules.modules) {
            Module.Background res = mod.shouldHideBackground(screen);
            switch (res) {
                case PASS -> {}
                case FORCE_SHOW -> {
                    show = Optional.of(true);
                    break modules;
                }
                case FORCE_HIDE -> {
                    show = Optional.of(false);
                    break modules;
                }
                case SUGGEST_SHOW -> show = Optional.of(true);
                case SUGGEST_HIDE -> show = Optional.of(false);
            }
        }

        //		Class<? extends Screen> scr = screen.getClass();
        //		if(Config.UI_BACKGROUND.seenScreens.containsKey(scr.getName())){
        //			return !Config.UI_BACKGROUND.seenScreens.get(scr.getName());
        //		}else{
        //			Config.UI_BACKGROUND.seenScreens.put(scr.getName(), true);
        //		}


        if (show.isEmpty()) {
            Class<? extends Screen> cl = screen.getClass();
            String modid = modidFromClass.apply(cl).orElse("?unknown");
            InvMoveConfig.BACKGROUND.unrecognizedScreensHideBG.putIfAbsent(modid, new HashMap<>());
            HashMap<Class<? extends Screen>, Boolean> hm = InvMoveConfig.BACKGROUND.unrecognizedScreensHideBG.get(modid);

            if (!hm.containsKey(cl)) {
                hm.put(cl, true);
                InvMoveConfig.save();
            }

            return hm.get(cl);
        } else {
            return !show.get();
        }
    }

    public static void drawDebugOverlay(Function<Class<?>, String> classNameFn) {
        if(InvMoveConfig.GENERAL.DEBUG_DISPLAY.get()) {
            Screen screen = Minecraft.getInstance().screen;
            if(screen == null) return;

            int i = 0;
            Class<?> cl = screen.getClass();
            while (cl.getSuperclass() != null) {
                String className = classNameFn.apply(cl);
                Optional<String> modid = modidFromClass.apply(cl);
                if (modid.isPresent()) {
                    className = "[" + modid.get() + "] " + className;
                }
                Minecraft.getInstance().font.drawShadow(new PoseStack(), className, 4, 4 + 10 * i, 0xffffffff);

                i++;
                cl = cl.getSuperclass();
            }
        }
    }

}
