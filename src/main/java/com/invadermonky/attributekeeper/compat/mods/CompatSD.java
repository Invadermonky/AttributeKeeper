package com.invadermonky.attributekeeper.compat.mods;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.temperature.ITemperatureCapability;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.charles445.simpledifficulty.config.ModConfig;
import com.invadermonky.attributekeeper.config.ConfigHandlerAK;
import com.invadermonky.attributekeeper.config.ConfigHandlerAK.CategoryModCompat.CategoryNeedsMod.CategoryThirst;
import com.invadermonky.attributekeeper.utils.ICompatModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class CompatSD implements ICompatModule {
    @Override
    public void copyStats(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
        this.copyTemperature(oldPlayer, newPlayer);
        this.copyThirst(oldPlayer, newPlayer);
    }

    private void copyTemperature(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
        if(!ModConfig.server.temperatureEnabled || !ConfigHandlerAK.mod_compat.simpledifficulty.temperature_keeper)
            return;

        ITemperatureCapability oldTemp = SDCapabilities.getTemperatureData(oldPlayer);
        ITemperatureCapability newTemp = SDCapabilities.getTemperatureData(newPlayer);

        newTemp.setTemperatureLevel(oldTemp.getTemperatureLevel());
    }

    private void copyThirst(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
        if(!ModConfig.server.thirstEnabled || !ConfigHandlerAK.mod_compat.simpledifficulty.thirst_keeper.enable)
            return;

        IThirstCapability oldThirst = SDCapabilities.getThirstData(oldPlayer);
        IThirstCapability newThirst = SDCapabilities.getThirstData(newPlayer);

        CategoryThirst config = ConfigHandlerAK.mod_compat.simpledifficulty.thirst_keeper;

        if(config.persistentThirst) {
            newThirst.setThirstLevel(oldThirst.getThirstLevel());
        } else {
            double thirst = oldThirst.getThirstLevel();
            double thirstLoss = thirst * config.thirstLossPercentage;
            thirstLoss = Math.max(thirstLoss, config.thirstLossFlat);
            double min = config.thirstMinLevel;
            double max = Math.max(min, config.thirstMaxLevel);
            thirst = MathHelper.clamp(thirst - thirstLoss, min, max);
            newThirst.setThirstLevel((int) thirst);
        }

        if(config.persistentHydration) {
            newThirst.setThirstExhaustion(oldThirst.getThirstExhaustion());
        } else {
            double hydration = oldThirst.getThirstExhaustion();
            double hyudrationLoss = hydration * config.hydrationLossPercentage;
            hyudrationLoss = Math.max(hyudrationLoss, config.hydrationLossFlat);
            double min = config.hydrationMinLevel;
            double max = Math.max(min, config.hydrationMaxLevel);
            hydration = MathHelper.clamp(hydration - hyudrationLoss, min, max);
            newThirst.setThirstExhaustion((int) hydration);
        }
    }
}
