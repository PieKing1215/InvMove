package me.pieking1215.invmove;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.MutableComponent;

import java.io.File;
import java.util.Optional;

public class InvMoveNoOp extends InvMove {
    @Override
    public Optional<String> modidFromClass(Class<?> c) {
        return Optional.empty();
    }

    @Override
    public String modNameFromModid(String modid) {
        return "???";
    }

    @Override
    public boolean hasMod(String modid) {
        return false;
    }

    @Override
    public File configDir() {
        return null;
    }

    @Override
    protected void registerKeybind(KeyMapping key) {

    }

    @Override
    public MutableComponent translatableComponent(String key) {
        return null;
    }

    @Override
    public MutableComponent literalComponent(String text) {
        return null;
    }

    @Override
    public boolean optionToggleCrouch() {
        return false;
    }

    @Override
    public void setOptionToggleCrouch(boolean toggleCrouch) {

    }
}
