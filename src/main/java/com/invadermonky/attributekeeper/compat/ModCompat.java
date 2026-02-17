package com.invadermonky.attributekeeper.compat;

import com.invadermonky.attributekeeper.compat.mods.CompatSD;
import com.invadermonky.attributekeeper.compat.mods.CompatTAN;
import com.invadermonky.attributekeeper.utils.ICompatModule;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class ModCompat {
    private static final List<ICompatModule> COMPAT_MODULES = new ArrayList<>();

    public static void registerModCompat(ICompatModule module) {
        if(module != null)
            COMPAT_MODULES.add(module);
    }

    public static List<ICompatModule> getCompatModules() {
        return COMPAT_MODULES;
    }

    public static void initCompat() {
        if(Loader.isModLoaded("simpledifficulty"))  registerModCompat(new CompatSD());
        if(Loader.isModLoaded("toughasnails"))      registerModCompat(new CompatTAN());
    }
}
