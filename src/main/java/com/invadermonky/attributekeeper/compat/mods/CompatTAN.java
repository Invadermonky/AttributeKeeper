package com.invadermonky.attributekeeper.compat.mods;

import com.invadermonky.attributekeeper.config.ConfigHandlerAK;
import com.invadermonky.attributekeeper.config.ConfigHandlerAK.CategoryModCompat.CategoryNeedsMod.CategoryThirst;
import com.invadermonky.attributekeeper.utils.ICompatModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.stat.StatHandlerBase;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.handler.PacketHandler;

public class CompatTAN implements ICompatModule {
    public CompatTAN() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
        this.sendTemperatureUpdateMessage(event.player);
        this.sendThirstUpdateMessage(event.player);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        this.sendThirstUpdateMessage(event.player);
    }

    private void sendTemperatureUpdateMessage(EntityPlayer player) {
        if(!player.world.isRemote) {
            ITemperature temperature = TemperatureHelper.getTemperatureData(player);
            if(temperature instanceof StatHandlerBase && player instanceof EntityPlayerMP) {
                PacketHandler.instance.sendTo(temperature.createUpdateMessage(), (EntityPlayerMP) player);
            }
        }
    }

    private void sendThirstUpdateMessage(EntityPlayer player) {
        if(!player.world.isRemote) {
            IThirst thirst = ThirstHelper.getThirstData(player);
            if(thirst instanceof StatHandlerBase && player instanceof EntityPlayerMP) {
                PacketHandler.instance.sendTo(thirst.createUpdateMessage(), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public void copyStats(EntityPlayer oldPlayer, EntityPlayer newPlayer, boolean isDeath) {
        if(isDeath) {
            this.copyTemperature(oldPlayer, newPlayer);
            this.copyThirst(oldPlayer, newPlayer);
        }
    }

    private void copyTemperature(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
        if(!SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE) || !ConfigHandlerAK.mod_compat.toughasnails.temperature_keeper)
            return;

        ITemperature oldTemp = TemperatureHelper.getTemperatureData(oldPlayer);
        ITemperature newTemp = TemperatureHelper.getTemperatureData(newPlayer);

        newTemp.setTemperature(oldTemp.getTemperature());
    }

    private void copyThirst(EntityPlayer oldPlayer, EntityPlayer newPlayer) {
        if(!SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST) || !ConfigHandlerAK.mod_compat.toughasnails.thirst_keeper.enable)
            return;

        IThirst oldThirst = ThirstHelper.getThirstData(oldPlayer);
        IThirst newThirst = ThirstHelper.getThirstData(newPlayer);

        CategoryThirst config = ConfigHandlerAK.mod_compat.toughasnails.thirst_keeper;

        if(config.persistentThirst) {
            newThirst.setThirst(oldThirst.getThirst());
        } else {
            double thirst = oldThirst.getThirst();
            double thirstLoss = thirst * config.thirstLossPercentage;
            thirstLoss = Math.max(thirstLoss, config.thirstLossFlat);
            double min = config.thirstMinLevel;
            double max = Math.max(min, config.thirstMaxLevel);
            thirst = MathHelper.clamp(thirst - thirstLoss, min, max);
            newThirst.setThirst((int) thirst);
        }

        if(config.persistentHydration) {
            newThirst.setHydration(oldThirst.getHydration());
        } else {
            double hydration = oldThirst.getHydration();
            double hyudrationLoss = hydration * config.hydrationLossPercentage;
            hyudrationLoss = Math.max(hyudrationLoss, config.hydrationLossFlat);
            double min = config.hydrationMinLevel;
            double max = Math.max(min, config.hydrationMaxLevel);
            hydration = MathHelper.clamp(hydration - hyudrationLoss, min, max);
            newThirst.setThirst((int) hydration);
        }
    }
}
