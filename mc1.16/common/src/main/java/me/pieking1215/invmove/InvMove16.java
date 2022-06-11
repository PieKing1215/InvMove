package me.pieking1215.invmove;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class InvMove16 extends InvMove {
    @Override
    public MutableComponent translatableComponent(String key) {
        return new TranslatableComponent(key);
    }

    @Override
    public MutableComponent literalComponent(String text) {
        return new TextComponent(text);
    }

    @Override
    public boolean optionToggleCrouch() {
        return Minecraft.getInstance().options.toggleCrouch;
    }

    @Override
    public void setOptionToggleCrouch(boolean toggleCrouch) {
        Minecraft.getInstance().options.toggleCrouch = toggleCrouch;
    }
}
