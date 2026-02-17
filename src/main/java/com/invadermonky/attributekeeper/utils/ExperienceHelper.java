package com.invadermonky.attributekeeper.utils;

public class ExperienceHelper {
    public static int getExperienceForLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else if (level >= 15) {
            return 37 + (level - 15) * 5;
        } else {
            return 7 + level * 2;
        }
    }

    protected static int getTotalExperienceForLevel(int level) {
        int xp = 0;
        for (int i = 0; i < level; i++) {
            xp += getExperienceForLevel(i);
        }
        return xp;
    }

    public static int getRemainingExperience(int levels, int experience) {
        return Math.max(0, experience - getTotalExperienceForLevel(levels));
    }

    public static int getLevelsForExperience(int experience) {
        int levels = 0;
        int barCap = getExperienceForLevel(1);
        while (experience >= barCap) {
            experience -= barCap;
            levels++;
            barCap = getExperienceForLevel(levels + 1);
        }
        return levels;
    }
}
