package me.pieking1215.invmove.module;

import java.util.ArrayList;
import java.util.List;

public class Modules {
    public static List<Module> modules = new ArrayList<>();

    public static void init() {
        List<Module> oldModules = modules;
        modules = new ArrayList<>();
        modules.add(new VanillaModule());
        modules.addAll(oldModules);
    }

}
