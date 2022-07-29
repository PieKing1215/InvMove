package me.pieking1215.invmove;

import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.VanillaModule19;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class InvMove19 extends InvMove {

    public InvMove19() {
        super();
    }

    @Override
    public Module getVanillaModule() {
        return new VanillaModule19();
    }

    @Override
    public MutableComponent translatableComponent(String key) {
        return Component.translatable(key);
    }

    @Override
    public MutableComponent literalComponent(String text) {
        return Component.literal(text);
    }

    @Override
    public boolean optionToggleCrouch() {
        return Minecraft.getInstance().options.toggleCrouch().get();
    }

    @Override
    public void setOptionToggleCrouch(boolean toggleCrouch) {
        Minecraft.getInstance().options.toggleCrouch().set(toggleCrouch);
    }
}
