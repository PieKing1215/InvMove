package me.pieking1215.invmove;

import me.pieking1215.invmove.module.Module;
import me.pieking1215.invmove.module.VanillaModule18;

public abstract class InvMove18 extends InvMove {

    public InvMove18() {
        super();
    }

    @Override
    public Module getVanillaModule() {
        return new VanillaModule18();
    }
}
