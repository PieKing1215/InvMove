package me.pieking1215.invmove.mixin.client;

//? if >=1.21.2 {

import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractRecipeBookScreen.class)
public interface AbstractRecipeBookScreenAccessor {
    @Accessor
    RecipeBookComponent<?> getRecipeBookComponent();
}

//?}