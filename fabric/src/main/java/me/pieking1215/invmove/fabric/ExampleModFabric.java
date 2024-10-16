package me.pieking1215.invmove.fabric;

import net.fabricmc.api.ModInitializer;

import me.pieking1215.invmove.ExampleMod;

public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
    }
}
