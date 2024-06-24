package me.pieking1215.invmove;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.VanillaModule21;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class InvMove21 extends InvMove {

    public InvMove21() {

        super();
    }

    @Override
    public Module getVanillaModule() {
        return new VanillaModule21();
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

    @Override
    protected void drawShadow(Font font, PoseStack poseStack, String string, float x, float y, int col){
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(new ByteBufferBuilder(786432));
        font.drawInBatch(string, x, y, col, true, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
    }

    @Override
    public ResourceLocation parseResource(String path){
        return ResourceLocation.parse(path);
    }
}
