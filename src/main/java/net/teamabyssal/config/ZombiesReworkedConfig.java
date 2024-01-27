package net.teamabyssal.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class ZombiesReworkedConfig {
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final DataGen DATAGEN;
    public static final ForgeConfigSpec DATAGEN_SPEC;


    static {

        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();

        Pair<DataGen , ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(DataGen::new);
        DATAGEN = commonPair.getLeft();
        DATAGEN_SPEC = commonPair.getRight();


    }
    public static class Server {

        public final ForgeConfigSpec.ConfigValue<Boolean> zombie_leaps;
        public final ForgeConfigSpec.ConfigValue<Boolean> zombie_accurate_attack_range;
        public final ForgeConfigSpec.ConfigValue<Boolean> player_assimilation;
        public final ForgeConfigSpec.ConfigValue<Boolean> villager_assimilation;
        public final ForgeConfigSpec.ConfigValue<Double> assimilation_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> heavy_armor_on_zombies;
        public final ForgeConfigSpec.ConfigValue<Boolean> sprint_goal;
        public final ForgeConfigSpec.ConfigValue<Boolean> fast_at_night;
        public final ForgeConfigSpec.ConfigValue<Boolean> spyglass_use;
        public final ForgeConfigSpec.ConfigValue<Boolean> pearl_use;
        public final ForgeConfigSpec.ConfigValue<Boolean> shield_use;
        public final ForgeConfigSpec.ConfigValue<Boolean> totem_use;
        public final ForgeConfigSpec.ConfigValue<Boolean> flint_and_steel_use;
        public final ForgeConfigSpec.ConfigValue<Integer> block_breaking_chance;
        public final ForgeConfigSpec.ConfigValue<Double> block_breaking_action_sphere;
        public final ForgeConfigSpec.ConfigValue<Boolean> doMobsBreakBlocks;

        public Server(ForgeConfigSpec.Builder builder) {

            builder.push("AI Changes for the vanilla zombies");
            this.zombie_leaps = builder.comment("Default true").define("Should the zombies perform small leaps at the player?",true);
            this.zombie_accurate_attack_range = builder.comment("Default true").define("Should the zombies have accurate-melee range?",true);
            this.heavy_armor_on_zombies = builder.comment("Default true").define("Should zombies have upgraded armor and equipment and spawn with it less rarely?",true);
            this.sprint_goal = builder.comment("Default true").define("Should zombies perform small sprints when close to the player?",true);
            this.fast_at_night = builder.comment("Default true").define("Should zombies be faster when encountering the player at night time?",true);
            this.doMobsBreakBlocks = builder.comment("Default true").define("Should the zombies break blocks?",true);
            this.block_breaking_chance = builder.comment("Default 90").defineInRange("Chance of the block breaking on tick, 120 ~= 0,8%", 120, 60, 200);
            this.block_breaking_action_sphere = builder.comment("Default 0.15").defineInRange("Area of the blocks that the zombies can break, the bigger the number, the more the blocks a mob can destroy in one go", 0.15, 0.05, 0.5);
            builder.pop();

            builder.push("Items that the zombies can use");
            this.spyglass_use = builder.comment("Default true").define("Should the zombies use spyglasses?",true);
            this.pearl_use = builder.comment("Default true").define("Should the zombies use ender pearls?",true);
            this.shield_use = builder.comment("Default true").define("Should the zombies use shields?",true);
            this.totem_use = builder.comment("Default true").define("Should the zombies use totems of undying?",true);
            this.flint_and_steel_use = builder.comment("Default true").define("Should the zombies use flint and steels?",true);
            builder.pop();

            builder.push("Zombifications");
            this.player_assimilation = builder.comment("Default true").define("Should the player convert after getting killed by a zombie?",true);
            this.villager_assimilation = builder.comment("Default true").define("Should the villager convert after getting killed by a zombie?",true);
            this.assimilation_chance = builder.comment("Default 0.2").defineInRange("What should be the chance of the player/villager converting to a zombie/zombie villager when getting killed?", 0.2D, 0.05D, Double.MAX_VALUE);
            builder.pop();


        }
    }
    public static class DataGen {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_helmet;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_chestplate;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_legs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_feet;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_main_hand;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> zombie_off_hand;
        public final ForgeConfigSpec.ConfigValue<Boolean> improved_equipment;
        public final ForgeConfigSpec.ConfigValue<Integer> improved_equipment_chance;

        public DataGen(ForgeConfigSpec.Builder builder) {

            builder.push("Zombie Equipment");
            builder.comment("Items / chance of spawning with it");
            this.zombie_helmet = builder.defineList("Head Slot",
                    Lists.newArrayList("minecraft:chainmail_helmet|50","minecraft:iron_helmet|20","minecraft:leather_helmet|20") , o -> o instanceof String);
            this.zombie_chestplate = builder.defineList("Chest Slot",
                    Lists.newArrayList("minecraft:chainmail_chestplate|50","minecraft:iron_chestplate|20","minecraft:leather_chestplate|20") , o -> o instanceof String);
            this.zombie_main_hand = builder.defineList("Main Hand Slot",
                    Lists.newArrayList("minecraft:stone_sword|50" , "minecraft:iron_sword|20","minecraft:golden_pickaxe|15", "minecraft:iron_pickaxe|20" , "minecraft:golden_sword|30") , o -> o instanceof String);
            this.zombie_legs = builder.defineList("Legs Slot",
                    Lists.newArrayList("minecraft:leather_leggings|50","minecraft:iron_leggings|20","minecraft:chainmail_leggings|20") , o -> o instanceof String);
            this.zombie_feet = builder.defineList("Boots Slot",
                    Lists.newArrayList("minecraft:leather_boots|50","minecraft:iron_boots|20","minecraft:chainmail_boots|20") , o -> o instanceof String);
            this.zombie_off_hand = builder.defineList("Off Hand Slot",
                    Lists.newArrayList( "minecraft:ender_pearl|15", "minecraft:totem_of_undying|10", "minecraft:spyglass|20", "minecraft:shield|30", "minecraft:flint_and_steel|30") , o -> o instanceof String);
            this.improved_equipment = builder.comment("Default true").define("Should zombies spawn with more armor and tools and more commonly",true);
            this.improved_equipment_chance = builder.comment("Default 160").define("Chance of the zombies spawning with improved equipment, the bigger the number the smaller the chance",160);
            builder.pop();

        }

    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}