package me.pieking1215.invmove;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;

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

    @Override
    protected void drawShadow(Font font, PoseStack poseStack, String string, float x, float y, int col) {

    }

    @Override
    public ResourceLocation parseResource(String path){
        return null;
    }
}
