package me.pieking1215.invmove.forge.mixin.client;

import me.pieking1215.invmove.InvMove;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.extensions.IForgeKeyMapping;
import org.spongepowered.asm.mixin.Mixin;

// normally I would just @Inject into the head of IForgeKeyMapping::isConflictContextAndModifierActive
// but only Fabric mixin supports injecting into default fns in interfaces
@Mixin(KeyMapping.class)
public abstract class KeyMappingIsDownMixin implements IForgeKeyMapping {
    @Override
    public boolean isConflictContextAndModifierActive() {
        // (neo)forge does extra checks that make some keys not work in inventories
        // I don't really know/care how it works since Fabric doesn't have this
        // so when the mod needs raw key down, skip the extra check
        if (InvMove.instance().shouldForceRawKeyDown()) {
            return true;
        } else {
            return IForgeKeyMapping.super.isConflictContextAndModifierActive();
        }
    }
}
