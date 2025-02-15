package me.pieking1215.invmove.module;

import me.pieking1215.invmove.InvMove;
import me.pieking1215.invmove.InvMoveConfig;
//? if >=1.21.2
import me.pieking1215.invmove.mixin.client.AbstractRecipeBookScreenAccessor;
import me.pieking1215.invmove.mixin.client.RecipeBookComponentAccessor;
import me.pieking1215.invmove.module.config.ConfigBool;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import static me.pieking1215.invmove.InvMove.getDeclaredFieldsSuper;

public class VanillaModule extends ModuleImpl {

    protected ConfigBool recipeBookAllowMovement;

    @Override
    public String getId() {
        return "vanilla";
    }

    public VanillaModule() {
        super();
        register(
            DeathScreen.class,
            ShareToLanScreen.class,
            StatsScreen.class,
            WinScreen.class,
            ProgressScreen.class,
            LevelLoadingScreen.class,
            ReceivingLevelScreen.class,
            //? if >=1.20.5 {
            GenericMessageScreen.class,
            //?} else
            /*GenericDirtMessageScreen.class,*/
            ChatScreen.class,
            // (technically 1.19.3 but required experimental 1.20 pack)
            //? if >=1.20 {
            AbstractSignEditScreen.class,
            //?} else
            /*SignEditScreen.class,*/
            CommandBlockEditScreen.class,
            MinecartCommandBlockEditScreen.class,
            StructureBlockEditScreen.class,
            JigsawBlockEditScreen.class
        ).movement(Movement.SUGGEST_DISABLE).background(Background.SUGGEST_SHOW);

        register(BookEditScreen.class).movement(Movement.SUGGEST_DISABLE);

        register(GameModeSwitcherScreen.class).movement(Movement.SUGGEST_ENABLE);

        recipeBookAllowMovement = m_config.bool("Recipe Book", "recipeBook", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
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
        register(SmithingScreen.class)                      .cfg("smithing").display("block.minecraft.smithing_table").submit();
        register(MerchantScreen.class)                      .cfg("villager").display("entity.minecraft.villager").submit();
        register(BookViewScreen.class, BookEditScreen.class).cfg("book").display("item.minecraft.book").submit();
        register(AdvancementsScreen.class)                  .cfg("advancements").display("gui.advancements").submit();
    }

    @Override
    public Movement shouldAllowMovement(Screen screen) {
        if(screen.getTitle() != null && screen.getTitle().equals(InvMove.instance().translatableComponent("sign.edit"))) return Movement.SUGGEST_DISABLE;

        if (!recipeBookAllowMovement.get()) {
            //? if >=1.21.2 {
            if (screen instanceof AbstractRecipeBookScreen<?>) {
                //?} else
                /*if (screen instanceof RecipeUpdateListener) {*/
                try {
                    //? if >=1.21.2 {
                    RecipeBookComponent<?> cmp = ((AbstractRecipeBookScreenAccessor) screen).getRecipeBookComponent();
                    //?} else
                    /*RecipeBookComponent cmp = ((RecipeUpdateListener) screen).getRecipeBookComponent();*/
                    if (cmp.isVisible())
                        return Movement.SUGGEST_DISABLE;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(InvMoveConfig.MOVEMENT.TEXT_FIELD_DISABLES.get() && recipeBookAllowMovement.get()) {
            // don't allow movement when focused on an active textfield

            // search all fields and superclass fields for a EditBox
            // TODO: cache this
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

            //? if >=1.21.2 {
            if (screen instanceof AbstractRecipeBookScreen<?>) {
            //?} else
            /*if (screen instanceof RecipeUpdateListener) {*/
                try {
                    //? if >=1.21.2 {
                    RecipeBookComponent<?> cmp = ((AbstractRecipeBookScreenAccessor) screen).getRecipeBookComponent();
                    //?} else
                    /*RecipeBookComponent cmp = ((RecipeUpdateListener) screen).getRecipeBookComponent();*/
                    EditBox searchBar = ((RecipeBookComponentAccessor)cmp).getSearchBox();
                    if (searchBar != null && searchBar.isVisible() && searchBar.active && searchBar.canConsumeInput())
                        return Movement.SUGGEST_DISABLE;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return super.shouldAllowMovement(screen);
    }

    @Override
    public Optional<Boolean> allowKeyDefault(KeyMapping key) {
        if (Stream.of(
            Minecraft.getInstance().options.keyUp,
            Minecraft.getInstance().options.keyDown,
            Minecraft.getInstance().options.keyLeft,
            Minecraft.getInstance().options.keyRight,
            Minecraft.getInstance().options.keyJump,
            Minecraft.getInstance().options.keyShift,
            Minecraft.getInstance().options.keySprint
        ).anyMatch(k -> k == key)) {
            return Optional.of(true);
        }

        return Optional.empty();
    }

    @Override
    public Background shouldHideBackground(Screen screen) {

        if(screen.getTitle() != null && screen.getTitle().equals(InvMove.instance().translatableComponent("sign.edit"))) return Background.SUGGEST_SHOW;

        return super.shouldHideBackground(screen);
    }
}
