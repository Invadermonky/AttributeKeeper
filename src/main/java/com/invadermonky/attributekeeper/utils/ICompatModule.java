package com.invadermonky.attributekeeper.utils;

import net.minecraft.entity.player.EntityPlayer;

public interface ICompatModule {
    void copyStats(EntityPlayer oldPlayer, EntityPlayer newPlayer, boolean isDeath);
}
