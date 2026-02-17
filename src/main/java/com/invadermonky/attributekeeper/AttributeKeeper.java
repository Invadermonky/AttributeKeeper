package com.invadermonky.attributekeeper;

import com.invadermonky.attributekeeper.compat.ModCompat;
import com.invadermonky.attributekeeper.config.FileHandlerAK;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = AttributeKeeper.MOD_ID,
        name = AttributeKeeper.MOD_NAME,
        version = AttributeKeeper.MOD_VERSION,
        acceptedMinecraftVersions = AttributeKeeper.MC_VERSIONS
)
public class AttributeKeeper {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;
    public static final String MC_VERSIONS = "[1.12.2]";

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public static File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), MOD_ID);
        if(!configDir.exists()) {
            configDir.mkdir();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FileHandlerAK.syncConfig();
        ModCompat.initCompat();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        //TODO: Talk to AC and Ski about adding the attribute command to this mod.
    }
}
