package me.pieking1215.invmove.module;

import me.pieking1215.invmove.InvMoveConfig;
import me.pieking1215.invmove.module.config.ConfigBool;
import me.pieking1215.invmove.module.config.ModuleConfig;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
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
import net.minecraft.network.chat.TranslatableComponent;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static me.pieking1215.invmove.InvMove.getDeclaredFieldsSuper;

public class VanillaModule implements Module {

    ModuleConfig m_config = new ModuleConfig("movement");
    ConfigBool m_inventory      = m_config.bool("container.inventory", "inventory", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_horseInventory = m_config.bool("horseInventory", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_creative       = m_config.bool("key.categories.creative", "creative", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_crafting       = m_config.bool("block.minecraft.crafting_table", "crafting", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_chest          = m_config.bool("container.chest", "chest", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_shulker        = m_config.bool("container.shulkerBox", "shulker", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_dispenser      = m_config.bool("container.dispenser", "dispenser", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_hopper         = m_config.bool("container.hopper", "hopper", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_enchantment    = m_config.bool("block.minecraft.enchanting_table", "enchantment", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_anvil          = m_config.bool("block.minecraft.anvil", "anvil", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_beacon         = m_config.bool("container.beacon", "beacon", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_brewing        = m_config.bool("container.brewing", "brewing", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_furnace        = m_config.bool("container.furnace", "furnace", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_blastFurnace   = m_config.bool("container.blast_furnace", "blastFurnace", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_smoker         = m_config.bool("container.smoker", "smoker", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_loom           = m_config.bool("container.loom", "loom", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_cartography    = m_config.bool("container.cartography_table", "cartography", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_grindstone     = m_config.bool("block.minecraft.grindstone", "grindstone", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_stonecutter    = m_config.bool("container.stonecutter", "stonecutter", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_villager       = m_config.bool("entity.minecraft.villager", "villager", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_book           = m_config.bool("item.minecraft.book", "book", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);
    ConfigBool m_advancements   = m_config.bool("gui.advancements", "advancements", true).textFn(InvMoveConfig.MOVEMENT_YES_NO_TEXT);

    ModuleConfig bg_config = new ModuleConfig("backgroundHide");
    ConfigBool b_inventory      = bg_config.bool("container.inventory", "inventory", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_horseInventory = bg_config.bool("horseInventory", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_creative       = bg_config.bool("key.categories.creative", "creative", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_crafting       = bg_config.bool("block.minecraft.crafting_table", "crafting", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_chest          = bg_config.bool("container.chest", "chest", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_shulker        = bg_config.bool("container.shulkerBox", "shulker", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_dispenser      = bg_config.bool("container.dispenser", "dispenser", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_hopper         = bg_config.bool("container.hopper", "hopper", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_enchantment    = bg_config.bool("block.minecraft.enchanting_table", "enchantment", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_anvil          = bg_config.bool("block.minecraft.anvil", "anvil", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_beacon         = bg_config.bool("container.beacon", "beacon", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_brewing        = bg_config.bool("container.brewing", "brewing", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_furnace        = bg_config.bool("container.furnace", "furnace", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_blastFurnace   = bg_config.bool("container.blast_furnace", "blastFurnace", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_smoker         = bg_config.bool("container.smoker", "smoker", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_loom           = bg_config.bool("container.loom", "loom", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_cartography    = bg_config.bool("container.cartography_table", "cartography", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_grindstone     = bg_config.bool("block.minecraft.grindstone", "grindstone", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_stonecutter    = bg_config.bool("container.stonecutter", "stonecutter", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_villager       = bg_config.bool("entity.minecraft.villager", "villager", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_book           = bg_config.bool("item.minecraft.book", "book", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);
    ConfigBool b_advancements   = bg_config.bool("gui.advancements", "advancements", true).textFn(InvMoveConfig.BACKGROUND_YES_NO_TEXT);

    @Override
    public String getId() {
        return "vanilla";
    }

    @Override
    public Movement shouldAllowMovement(Screen screen) {
        if(screen instanceof GenericDirtMessageScreen)       return Movement.SUGGEST_DISABLE;
        if(screen instanceof DeathScreen)                    return Movement.SUGGEST_DISABLE;
        if(screen instanceof OptionsScreen)                  return Movement.SUGGEST_DISABLE;
        if(screen instanceof OptionsSubScreen)               return Movement.SUGGEST_DISABLE;
        if(screen instanceof ShareToLanScreen)               return Movement.SUGGEST_DISABLE;
        if(screen instanceof StatsScreen)                    return Movement.SUGGEST_DISABLE;
        if(screen instanceof WinScreen)                      return Movement.SUGGEST_DISABLE;
        if(screen instanceof ProgressScreen)                 return Movement.SUGGEST_DISABLE;
        if(screen instanceof LevelLoadingScreen)             return Movement.SUGGEST_DISABLE;
        if(screen instanceof ChatScreen)                     return Movement.SUGGEST_DISABLE;
        if(screen instanceof CommandBlockEditScreen)         return Movement.SUGGEST_DISABLE;
        if(screen instanceof BookEditScreen)                 return Movement.SUGGEST_DISABLE;
        if(screen instanceof MinecartCommandBlockEditScreen) return Movement.SUGGEST_DISABLE;
        if(screen instanceof StructureBlockEditScreen)       return Movement.SUGGEST_DISABLE;
        if(screen instanceof JigsawBlockEditScreen)          return Movement.SUGGEST_DISABLE;
        if(screen instanceof SignEditScreen)                 return Movement.SUGGEST_DISABLE;
        if(screen.getTitle().equals(new TranslatableComponent("sign.edit", new Object[0]))) return Movement.SUGGEST_DISABLE;

        if(InvMoveConfig.MOVEMENT.TEXT_FIELD_DISABLES.get()) {
            // don't allow movement when focused on an active textfield

            // search all fields and superclass fields for a EditBox
            try {
                Field[] fs = getDeclaredFieldsSuper(screen.getClass());

                for (Field f : fs) {
                    f.setAccessible(true);
                    if (EditBox.class.isAssignableFrom(f.getType())) {
                        EditBox tfw = (EditBox) f.get(screen);
                        if (tfw != null && tfw.isActive() && tfw.canConsumeInput()) return Movement.SUGGEST_DISABLE;
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
                        if (searchBar != null && searchBar.isActive() && searchBar.canConsumeInput()) return Movement.SUGGEST_DISABLE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(screen instanceof GameModeSwitcherScreen)      return Movement.SUGGEST_ENABLE;

        if(screen instanceof InventoryScreen)             return m_inventory.get()      ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof HorseInventoryScreen)   	  return m_horseInventory.get() ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof CreativeModeInventoryScreen) return m_creative.get()       ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof CraftingScreen)              return m_crafting.get()       ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof ContainerScreen)             return m_chest.get()          ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof ShulkerBoxScreen)            return m_shulker.get()        ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof DispenserScreen)             return m_dispenser.get()      ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof HopperScreen)                return m_hopper.get()         ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof EnchantmentScreen)           return m_enchantment.get()    ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof AnvilScreen)                 return m_anvil.get()          ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof BeaconScreen)                return m_beacon.get()         ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof BrewingStandScreen)          return m_brewing.get()        ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof FurnaceScreen)               return m_furnace.get()        ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof BlastFurnaceScreen)          return m_blastFurnace.get()   ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof SmokerScreen)                return m_smoker.get()         ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof LoomScreen)                  return m_loom.get()           ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof CartographyTableScreen)      return m_cartography.get()    ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof GrindstoneScreen)            return m_grindstone.get()     ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof StonecutterScreen)           return m_stonecutter.get()    ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof MerchantScreen)              return m_villager.get()       ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof BookViewScreen)              return m_book.get()           ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;
        if(screen instanceof AdvancementsScreen)          return m_advancements.get()   ? Movement.SUGGEST_ENABLE : Movement.SUGGEST_DISABLE;

        return Movement.PASS;
    }

    @Override
    public Background shouldHideBackground(Screen screen) {

        if(screen instanceof GenericDirtMessageScreen)       return Background.SUGGEST_SHOW;
        if(screen instanceof DeathScreen)                    return Background.SUGGEST_SHOW;
        if(screen instanceof OptionsScreen)                  return Background.SUGGEST_SHOW;
        if(screen instanceof OptionsSubScreen)               return Background.SUGGEST_SHOW;
        if(screen instanceof ShareToLanScreen)               return Background.SUGGEST_SHOW;
        if(screen instanceof StatsScreen)                    return Background.SUGGEST_SHOW;
        if(screen instanceof WinScreen)                      return Background.SUGGEST_SHOW;
        if(screen instanceof ProgressScreen)                 return Background.SUGGEST_SHOW;
        if(screen instanceof LevelLoadingScreen)             return Background.SUGGEST_SHOW;
        if(screen instanceof ChatScreen)                     return Background.SUGGEST_SHOW;
        if(screen instanceof CommandBlockEditScreen)         return Background.SUGGEST_SHOW;
        if(screen instanceof MinecartCommandBlockEditScreen) return Background.SUGGEST_SHOW;
        if(screen instanceof StructureBlockEditScreen)       return Background.SUGGEST_SHOW;
        if(screen instanceof JigsawBlockEditScreen)          return Background.SUGGEST_SHOW;
        if(screen instanceof SignEditScreen)                 return Background.SUGGEST_SHOW;
        if(screen.getTitle().equals(new TranslatableComponent("sign.edit", new Object[0]))) return Background.SUGGEST_SHOW;

        if(screen instanceof InventoryScreen)             return b_inventory.get()      ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof HorseInventoryScreen)        return b_horseInventory.get() ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof CreativeModeInventoryScreen) return b_creative.get()       ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof CraftingScreen)              return b_crafting.get()       ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof ContainerScreen)             return b_chest.get()          ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof ShulkerBoxScreen)            return b_shulker.get()        ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof DispenserScreen)             return b_dispenser.get()      ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof HopperScreen)                return b_hopper.get()         ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof EnchantmentScreen)           return b_enchantment.get()    ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof AnvilScreen)                 return b_anvil.get()          ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof BeaconScreen)                return b_beacon.get()         ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof BrewingStandScreen)          return b_brewing.get()        ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof FurnaceScreen)               return b_furnace.get()        ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof BlastFurnaceScreen)          return b_blastFurnace.get()   ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof SmokerScreen)                return b_smoker.get()         ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof LoomScreen)                  return b_loom.get()           ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof CartographyTableScreen)      return b_cartography.get()    ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof GrindstoneScreen)            return b_grindstone.get()     ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof StonecutterScreen)           return b_stonecutter.get()    ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof MerchantScreen)              return b_villager.get()       ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof BookViewScreen)              return b_book.get()           ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof BookEditScreen)              return b_book.get()           ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;
        if(screen instanceof AdvancementsScreen)          return b_advancements.get()   ? Background.SUGGEST_HIDE : Background.SUGGEST_SHOW;

        
        return Background.PASS;
    }

    @Override
    public ModuleConfig getMovementConfig() {
        return m_config;
    }

    @Override
    public ModuleConfig getBackgroundConfig() {
        return bg_config;
    }
}
