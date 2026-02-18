package com.invadermonky.attributekeeper.handlers;

import com.invadermonky.attributekeeper.AttributeKeeper;
import com.invadermonky.attributekeeper.compat.ModCompat;
import com.invadermonky.attributekeeper.config.ConfigHandlerAK;
import com.invadermonky.attributekeeper.config.FileHandlerAK;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = AttributeKeeper.MOD_ID)
public class CloneEventHandler {
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        EntityPlayer oldPlayer = event.getOriginal();
        EntityPlayer newPlayer = event.getEntityPlayer();

        //Preserve Hunger
        preserveHunger(oldPlayer, newPlayer, event.isWasDeath());
        //Preserve XP
        preserveExperience(oldPlayer, newPlayer, event.isWasDeath());
        //Mod Compat
        ModCompat.getCompatModules().forEach(module -> module.copyStats(oldPlayer, newPlayer, event.isWasDeath()));
        //Transfering Attributes
        AbstractAttributeMap attributeMap = oldPlayer.getAttributeMap();
        for(IAttributeInstance instance : attributeMap.getAllAttributes()) {
            FileHandlerAK.getAttributeHolders().stream()
                    .filter(holder -> holder.matches(instance))
                    .forEach(holder -> holder.preserveAttribute(newPlayer, instance, event.isWasDeath()));
        }
    }

    private static void preserveHunger(EntityPlayer oldPlayer, EntityPlayer newPlayer, boolean isDeath) {
        if(!isDeath || !ConfigHandlerAK.food_keeper.enable)
            return;

        FoodStats oldStats = oldPlayer.getFoodStats();
        FoodStats newStats = newPlayer.getFoodStats();

        if(ConfigHandlerAK.food_keeper.persistentFood) {
            newStats.setFoodLevel(oldStats.getFoodLevel());
        } else {
            int foodLevel = oldStats.getFoodLevel();
            int foodLoss = (int) (foodLevel * ConfigHandlerAK.food_keeper.foodLossPercentage);
            foodLoss = Math.max(foodLoss, ConfigHandlerAK.food_keeper.foodLossFlat);
            int min = ConfigHandlerAK.food_keeper.foodMinLevel;
            int max = Math.max(min, ConfigHandlerAK.food_keeper.foodMaxLevel);
            foodLevel = MathHelper.clamp(foodLevel - foodLoss, min, max);
            newStats.setFoodLevel(foodLevel);
        }

        if(ConfigHandlerAK.food_keeper.persistentSaturation) {
            newStats.setFoodLevel(oldStats.getFoodLevel());
        } else {
            double saturationLevel = oldStats.getSaturationLevel();
            double saturationLoss = saturationLevel * ConfigHandlerAK.food_keeper.saturationLossPercentage;
            saturationLoss = Math.max(saturationLoss, ConfigHandlerAK.food_keeper.saturationLossFlat);
            double min = ConfigHandlerAK.food_keeper.saturationMinLevel;
            double max = Math.max(min, ConfigHandlerAK.food_keeper.saturationMaxLevel);
            saturationLevel = MathHelper.clamp(saturationLevel - saturationLoss, min, max);
            newStats.setFoodSaturationLevel((float) saturationLevel);
        }
    }

    private static void preserveExperience(EntityPlayer oldPlayer, EntityPlayer newPlayer, boolean isDeath) {
        if(!isDeath || !ConfigHandlerAK.experience_keeper.enable || oldPlayer.experienceTotal == newPlayer.experienceTotal)
            return;

        if(ConfigHandlerAK.experience_keeper.persistent) {
            newPlayer.experienceTotal = oldPlayer.experienceTotal;
            newPlayer.experienceLevel = oldPlayer.experienceLevel;
            newPlayer.experience = oldPlayer.experience;
        } else {
            int oldLevels = oldPlayer.experienceLevel;
            int levelsLost = (int) (oldLevels * ConfigHandlerAK.experience_keeper.modifier);
            levelsLost = Math.max(levelsLost, ConfigHandlerAK.experience_keeper.flatLoss);
            int levels = oldLevels - levelsLost;
            if(ConfigHandlerAK.experience_keeper.maxLevels >= 0) {
                levels = Math.min(levels, ConfigHandlerAK.experience_keeper.maxLevels);
            }
            newPlayer.addExperienceLevel(-(oldLevels + 1));
            newPlayer.experienceLevel = levels;
            newPlayer.experience = oldPlayer.experience;
        }
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer && (!ConfigHandlerAK.experience_keeper.dropExperience || ConfigHandlerAK.experience_keeper.persistent)) {
            event.setCanceled(true);
        }
    }
}
