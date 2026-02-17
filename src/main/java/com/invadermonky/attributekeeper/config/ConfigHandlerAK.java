package com.invadermonky.attributekeeper.config;

import com.invadermonky.attributekeeper.AttributeKeeper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(
        modid = AttributeKeeper.MOD_ID,
        name = AttributeKeeper.MOD_ID + "/" + AttributeKeeper.MOD_ID
)
public class ConfigHandlerAK {
    @Config.Name("Experience Keeper")
    public static CategoryExperience experience_keeper = new CategoryExperience();
    @Config.Name("Food Keeper")
    public static CategoryFood food_keeper = new CategoryFood();
    @Config.Name("Mod Compat")
    public static CategoryModCompat mod_compat = new CategoryModCompat();

    public static class CategoryModCompat {
        @Config.Name("Simple Difficulty")
        public CategoryNeedsMod simpledifficulty = new CategoryNeedsMod();
        @Config.Name("Tough As Nails")
        public CategoryNeedsMod toughasnails = new CategoryNeedsMod();

        public static class CategoryNeedsMod {
            @Config.Name("Temperature Keeper")
            @Config.Comment("The player's current body temperature will persist through death.")
            public boolean temperature_keeper = true;

            @Config.Name("Thirst Keeper")
            public CategoryThirst thirst_keeper = new CategoryThirst();

            public static class CategoryThirst {
                @Config.Name("[00] Enable Thirst Keeper")
                @Config.Comment({
                        "Enables the thirst keeper feature, allowing thirst and hydration values to persist after player",
                        "death."
                })
                public boolean enable = true;

                @Config.Name("[01] Persistent Thirst")
                @Config.Comment({
                        "Thirst values will remain unchanged after player death. Setting this value to true overrides all",
                        "other settings."
                })
                public boolean persistentThirst = true;

                @Config.Name("[02] Persistent Hydration")
                @Config.Comment({
                        "Hydration values will remain unchanged after player death. Setting this value to true overrides all",
                        "other settings."
                })
                public boolean persistentHydration = true;

                // ##########################################
                //      Thirst
                // ##########################################

                @Config.Name("[03] Thirst - Percentage Loss")
                @Config.Comment({
                        "The percentage of thirst lost each time a player dies. A value of 0.1 will result in 10% of a",
                        "player's remaining thirst being lost on death."
                })
                public double thirstLossPercentage = 0;

                @Config.Name("[04] Thirst - Flat Loss")
                @Config.Comment({
                        "The flat amount of thirst lost each time the player dies. The amount of thirst lost will be the",
                        "higher value between this and the calculated percentage loss."
                })
                public int thirstLossFlat = 1;

                @Config.Name("[05] Thirst - Minimum Value")
                @Config.Comment({
                        "The minimum thirst value players can have after respawning. This places a limit on how low a player's",
                        "thirst can drop after dying."
                })
                public int thirstMinLevel = 10;

                @Config.Name("[06] Thirst - Maximum Value")
                @Config.Comment({
                        "The maximum thirst value players can have after respawning. This places an upper limit on how high a",
                        "player's thirst can go after dying."
                })
                public int thirstMaxLevel = 20;

                // ##########################################
                //      Hydration
                // ##########################################

                @Config.Name("[07] Hydration - Percentage Loss")
                @Config.Comment({
                        "The percentage of hydration lost each time a player dies. A value of 0.1 will result in 10% of a",
                        "player's remaining hydration being lost on death."
                })
                public double hydrationLossPercentage = 0;

                @Config.Name("[08] Hydration - Flat Loss")
                @Config.Comment({
                        "The flat amount of hydration lost each time the player dies. The amount of hydration lost will be",
                        "the higher value between this and the calculated percentage loss."
                })
                public double hydrationLossFlat = 0;

                @Config.Name("[09] Hydration - Minimum Value")
                @Config.Comment({
                        "The minimum hydration value players can have after respawning. This places a limit on how low a",
                        "player's hydration can drop after dying."
                })
                public double hydrationMinLevel = 6;

                @Config.Name("[10] Hydration - Maximum Value")
                @Config.Comment({
                        "The maximum hydration value players can have after respawning. This places an upper limit on how",
                        "high a player's hydration can go after dying."
                })
                public double hydrationMaxLevel = 6;

            }
        }
    }

    public static class CategoryExperience {
        @Config.Name("[1] Enable Experience Keeper")
        @Config.Comment("Enables the experience keeper feature.")
        public boolean enable = false;

        @Config.Name("[2] Persistent Experience")
        @Config.Comment({
                "Experience will not change after after player death. Setting this to true will also disable",
                "experience drop on player death."
        })
        public boolean persistent = true;

        @Config.Name("[3] Drop Experience")
        @Config.Comment("Set to false to prevent the player from dropping experience on death.")
        public boolean dropExperience = true;

        @Config.RangeDouble(min = 0, max = 1.0)
        @Config.Name("[4] Experience - Percentage Loss")
        @Config.Comment({
                "The percentage of total levels lost each time a player dies. A value of 0.1 will result in 10% of a",
                "player's total levels being lost on death."
        })
        public double modifier = 0;

        @Config.RangeInt(min = 0, max = 10000)
        @Config.Name("[5] Experience - Flat Loss")
        @Config.Comment({
                "The flat experience level loss whenever a player dies. The number of levels lost lost will be the",
                "higher value between this and the calculated percentage loss."
        })
        public int flatLoss = 0;

        @Config.RangeInt(min = -1, max = 10000)
        @Config.Name("[6] Maximum Levels Kept")
        @Config.Comment({
                "The highest possible experience levels a player can keep after death. A negative value will remove",
                "any limits."
        })
        public int maxLevels = 7;
    }

    public static class CategoryFood {
        @Config.Name("[00] Enable Hunger Keeper")
        @Config.Comment({
                "Enables the Hunger Keeper feature, allowing food and saturation values to persist after player",
                "death."
        })
        public boolean enable = true;

        @Config.Name("[01] Persistent Food Values")
        @Config.Comment({
                "Hunger values will persist after player death. Setting this value to true overrides all other",
                "settings."
        })
        public boolean persistentFood = true;

        @Config.Name("[02] Persistent Saturation Values")
        @Config.Comment({
                "Food saturation values will persist after player death. Setting this value to true overrides all",
                "other settings."
        })
        public boolean persistentSaturation = true;

        // ##########################################
        //      Hunger
        // ##########################################

        @Config.RangeDouble(min = 0, max = 1.0)
        @Config.Name("[03] Food - Percentage Loss")
        @Config.Comment({
                "The percentage of hunger lost each time a player dies. A value of 0.1 will result in 10% of a",
                "player's remaining hunger being lost on death."
        })
        public double foodLossPercentage = 0;

        @Config.RangeInt(min = 0, max = 20)
        @Config.Name("[04] Food - Flat Loss")
        @Config.Comment({
                "The flat amount of hunger lost each time the player dies. The amount of hunger lost will be the",
                "higher value between this and the calculated percentage loss."
        })
        public int foodLossFlat = 0;

        @Config.RangeInt(min = 0, max = 20)
        @Config.Name("[05] Food - Minimum Value")
        @Config.Comment({
                "The minimum hunger value players can have after respawning. This places a limit on how low a",
                "player's hunger can drop after dying."
        })
        public int foodMinLevel = 10;

        @Config.RangeInt(min = 0, max = 20)
        @Config.Name("[06] Food - Maximum Value")
        @Config.Comment({
                "The maximum food value players can have after respawning. This places an upper limit on how high a",
                "player's food can go after dying."
        })
        public int foodMaxLevel = 20;

        // ##########################################
        //      Saturation
        // ##########################################

        @Config.RangeDouble(min = 0, max = 1.0)
        @Config.Name("[07] Saturation - Percentage Loss")
        @Config.Comment({
                "The percentage of saturation lost each time a player dies. A value of 0.1 will result in 10% of a",
                "player's remaining saturation being lost on death."
        })
        public double saturationLossPercentage = 0;

        @Config.RangeDouble(min = 0, max = 20.0)
        @Config.Name("[08] Saturation - Minimum Value")
        @Config.Comment({
                "The minimum saturation value players can have after respawning. This places a limit on how low a",
                "player's saturation can drop after dying."
        })
        public double saturationLossFlat = 0;

        @Config.RangeDouble(min = 0, max = 20.0)
        @Config.Name("[09] Saturation - Minimum Value")
        @Config.Comment({
                "The minimum saturation value players can have after respawning. This places a limit on how low a",
                "player's saturation can drop after dying."
        })
        public double saturationMinLevel = 5.0;

        @Config.RangeDouble(min = 0, max = 20.0)
        @Config.Name("[10] Saturation - Maximum Value")
        @Config.Comment({
                "The maximum saturation value players can have after respawning. This places an upper limit on how",
                "high a player's saturation can go after dying."
        })
        public double saturationMaxLevel = 5.0;
    }

    @Mod.EventBusSubscriber(modid = AttributeKeeper.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(AttributeKeeper.MOD_ID)) {
                ConfigManager.sync(AttributeKeeper.MOD_ID, Config.Type.INSTANCE);
                FileHandlerAK.syncConfig();
            }
        }
    }
}
