package me.pieking1215.invmove;

public final class ExampleMod {
    public static final String MOD_ID = "invmove";

    public static void init() {
        #if MC_1_18_2
        System.out.println("This is Minecraft version 1.18.2!");
        #elif MC_1_19_2
        System.out.println("This is Minecraft version 1.19.2!");
        #elif MC_1_20_1
        System.out.println("This is Minecraft version 1.20.1!");
        #elif MC_1_21
        System.out.println("This is Minecraft version 1.21!");
        #elif MC_1_21_1
        System.out.println("This is Minecraft version 1.21.1!");
        #endif
    }
}
