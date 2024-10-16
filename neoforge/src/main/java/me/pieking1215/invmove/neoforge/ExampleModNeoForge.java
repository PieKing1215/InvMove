package me.pieking1215.invmove.neoforge;

import net.neoforged.fml.common.Mod;

import me.pieking1215.invmove.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        ExampleMod.init();

        //? if <1.21.1 {
        System.out.println("NeoForge <1.21.1!");
        //?} else
        /*System.out.println("NeoForge >=1.21.1!");*/
    }
}
