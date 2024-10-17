package me.pieking1215.invmove;

import net.minecraft.client.KeyMapping;

import java.io.File;
import java.util.Optional;

public class InvMoveNoOp extends InvMove {
    @Override
    protected Optional<String> modidFromClassInternal(Class<?> c) {
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
}
