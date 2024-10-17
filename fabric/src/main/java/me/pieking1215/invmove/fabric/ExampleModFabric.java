package me.pieking1215.invmove.fabric;

import net.fabricmc.api.ModInitializer;

import me.pieking1215.invmove.ExampleMod;

public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();

        //? if <1.21.1 {
        /*System.out.println("Fabric <1.21.1!");
        *///?} else
        System.out.println("Fabric >=1.21.1!");
    }
}
