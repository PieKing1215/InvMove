package me.pieking1215.invmove.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import me.pieking1215.invmove.fabriclike.ExampleModFabricLike;

public final class ExampleModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        ExampleModFabricLike.init();
    }
}
