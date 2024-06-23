package me.pieking1215.invmove.module;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static me.pieking1215.invmove.InvMove.getDeclaredFieldsSuper;

public class VanillaModule16 extends ModuleImpl {

    @Override
    public String getId() {
        return "vanilla";
    }

    public VanillaModule16() {
        super();
        register(
            DeathScreen.class,
            OptionsScreen.class,
            OptionsSubScreen.class,
            ShareToLanScreen.class,
            StatsScreen.class,
            WinScreen.class,
            ProgressScreen.class,
            LevelLoadingScreen.class,
            ChatScreen.class,
            CommandBlockEditScreen.class,
            MinecartCommandBlockEditScreen.class,
            StructureBlockEditScreen.class,
            JigsawBlockEditScreen.class,
            SignEditScreen.class
        ).movement(Movement.SUGGEST_DISABLE).background(Background.SUGGEST_SHOW);

        register(BookEditScreen.class).movement(Movement.SUGGEST_DISABLE);

        register(GameModeSwitcherScreen.class).movement(Movement.SUGGEST_ENABLE);

        register(InventoryScreen.class)                     .cfg("inventory").display("container.inventory").submit();
        register(HorseInventoryScreen.class)                .cfg("horseInventory").submit();
        register(CreativeModeInventoryScreen.class)         .cfg("creative").display("key.categories.creative").submit();
        register(CraftingScreen.class)                      .cfg("crafting").display("block.minecraft.crafting_table").submit();
        register(ContainerScreen.class)                     .cfg("chest").display("container.chest").submit();
        register(ShulkerBoxScreen.class)                    .cfg("shulker").display("container.shulkerBox").submit();
        register(DispenserScreen.class)                     .cfg("dispenser").display("container.dispenser").submit();
        register(HopperScreen.class)                        .cfg("hopper").display("container.hopper").submit();
        register(EnchantmentScreen.class)                   .cfg("enchantment").display("block.minecraft.enchanting_table").submit();
        register(AnvilScreen.class)                         .cfg("anvil").display("block.minecraft.anvil").submit();
        register(BeaconScreen.class)                        .cfg("beacon").display("container.beacon").submit();
        register(BrewingStandScreen.class)                  .cfg("brewing").display("container.brewing").submit();
        register(FurnaceScreen.class)                       .cfg("furnace").display("container.furnace").submit();
        register(BlastFurnaceScreen.class)                  .cfg("blastFurnace").display("container.blast_furnace").submit();
        register(SmokerScreen.class)                        .cfg("smoker").display("container.smoker").submit();
        register(LoomScreen.class)                          .cfg("loom").display("container.loom").submit();
        register(CartographyTableScreen.class)              .cfg("cartography").display("container.cartography_table").submit();
        register(GrindstoneScreen.class)                    .cfg("grindstone").display("block.minecraft.grindstone").submit();
        register(StonecutterScreen.class)                   .cfg("stonecutter").display("container.stonecutter").submit();
        register(MerchantScreen.class)                      .cfg("villager").display("entity.minecraft.villager").submit();
        register(BookViewScreen.class, BookEditScreen.class).cfg("book").display("item.minecraft.book").submit();
        register(AdvancementsScreen.class)                  .cfg("advancements").display("gui.advancements").submit();
    }

    @Override
    public Movement shouldAllowMovement(Screen screen) {
        if(screen.getTitle() != null && screen.getTitle().equals(InvMove.instance().translatableComponent("sign.edit"))) return Movement.SUGGEST_DISABLE;

        if(InvMoveConfig.MOVEMENT.TEXT_FIELD_DISABLES.get()) {
            // don't allow movement when focused on an active textfield

            // search all fields and superclass fields for a EditBox
            try {
                Field[] fs = getDeclaredFieldsSuper(screen.getClass());

                for (Field f : fs) {
                    f.setAccessible(true);
                    if (EditBox.class.isAssignableFrom(f.getType())) {
                        EditBox tfw = (EditBox) f.get(screen);
                        if (tfw != null && tfw.isVisible() && tfw.active && tfw.canConsumeInput()) return Movement.SUGGEST_DISABLE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (screen instanceof RecipeUpdateListener) {
                try {
                    RecipeBookComponent wid = ((RecipeUpdateListener) screen).getRecipeBookComponent();
                    Field searchField = Stream.of(RecipeBookComponent.class.getDeclaredFields()).filter(f -> f.getType() == EditBox.class).findFirst().orElse(null);
                    if(searchField != null) {
                        searchField.setAccessible(true);
                        EditBox searchBar = (EditBox) searchField.get(wid);
                        if (searchBar != null && searchBar.isVisible() && searchBar.active && searchBar.canConsumeInput()) return Movement.SUGGEST_DISABLE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return super.shouldAllowMovement(screen);
    }

    @Override
    public Background shouldHideBackground(Screen screen) {

        if(screen.getTitle() != null && screen.getTitle().equals(InvMove.instance().translatableComponent("sign.edit"))) return Background.SUGGEST_SHOW;

        return super.shouldHideBackground(screen);
    }
}
