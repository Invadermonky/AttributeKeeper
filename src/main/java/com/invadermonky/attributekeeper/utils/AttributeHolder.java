package com.invadermonky.attributekeeper.utils;

import com.google.common.base.Preconditions;
import com.invadermonky.attributekeeper.AttributeKeeper;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class AttributeHolder {
    /** The IAttribute name. */
    public String attributeName;
    /** If the attribute value is persistent after death. If true, this overrides all other settings. */
    public boolean persistent;
    /** The attribute loss modifier value. */
    public double attributeLossPercentage;
    /** The flat attribute loss value after death. This will overwrite the modifier setting if it is larger than the calculated modifier loss. */
    public double attributeLossFlat;
    /** The minimum attribute value. The attribute setting cannot fall below this value after death. */
    public double minimumLevel;
    /** The maximum attribute value. The attribute value cannot be above this value after player death. */
    public double maximumLevel;

    public boolean validateArguments() throws IllegalArgumentException {
        try {
            Preconditions.checkArgument(this.attributeName != null && !this.attributeName.isEmpty(), "'attributeName' cannot be null or empty.");
            if (!this.persistent) {
                Preconditions.checkArgument(this.attributeLossPercentage >= 0 && this.attributeLossPercentage <= 1.0, "'modifier' must be between 0 and 1.");
                Preconditions.checkArgument(this.attributeLossFlat >= 0, "'flatLoss' cannot be less than 0.");
                Preconditions.checkArgument(this.maximumLevel >= this.minimumLevel, "'maximumLevel' cannot be less than 'minimumLevel'.");
            }
            return true;
        } catch (IllegalArgumentException e) {
            AttributeKeeper.LOGGER.error(e);
            return false;
        }
    }

    public void preserveAttribute(EntityPlayer player, IAttributeInstance originalAttribute) {
        if(!this.matches(originalAttribute))
            return;

        IAttributeInstance instance = player.getEntityAttribute(originalAttribute.getAttribute());
        double newBase = this.getNewBaseValue(originalAttribute.getBaseValue());
        if (instance == null) {
            player.getAttributeMap().registerAttribute(originalAttribute.getAttribute()).setBaseValue(newBase);
        } else {
            instance.setBaseValue(newBase);
        }
    }

    private double getNewBaseValue(double originalBaseValue) {
        if(this.persistent) {
            return originalBaseValue;
        } else {
            double loss = originalBaseValue * this.attributeLossPercentage;
            loss = Math.max(this.attributeLossFlat, loss);
            double min = Math.min(originalBaseValue, this.minimumLevel);
            double max = Math.max(min, this.maximumLevel);
            return MathHelper.clamp(originalBaseValue - loss, min, max);
        }
    }

    public boolean matches(IAttributeInstance instance) {
        return instance != null && instance.getAttribute().getName().equals(this.attributeName);
    }
}
