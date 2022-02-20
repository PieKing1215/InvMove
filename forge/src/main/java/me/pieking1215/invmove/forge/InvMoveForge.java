package me.pieking1215.invmove.forge;

import me.pieking1215.invmove.InvMove;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(InvMove.MOD_ID)
public class InvMoveForge {
    public InvMoveForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> InvMoveForgeClient::init);
    }
}
