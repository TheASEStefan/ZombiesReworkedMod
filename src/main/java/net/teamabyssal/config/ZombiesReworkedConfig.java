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
        public final ForgeConfigSpec.ConfigValue<Boolean> zombies_break_blocks;

        public final ForgeConfigSpec.ConfigValue<Integer> block_breaking_chance;
        public final ForgeConfigSpec.ConfigValue<Double> block_breaking_action_sphere;

        public Server(ForgeConfigSpec.Builder builder) {

            builder.push("AI Changes for the vanilla zombies");
            this.zombie_leaps = builder.comment("Default true").define("Should the zombies perform small leaps at the player?",true);
            this.zombie_accurate_attack_range = builder.comment("Default true").define("Should the zombies have accurate-melee range?",true);
            this.heavy_armor_on_zombies = builder.comment("Default true").define("Should zombies have upgraded armor and equipment and spawn with it less rarely?",true);
            this.sprint_goal = builder.comment("Default true").define("Should zombies perform small sprints when close to the player?",true);
            this.fast_at_night = builder.comment("Default true").define("Should zombies be faster when encountering the player at night time?",true);
            this.zombies_break_blocks = builder.comment("Default true").define("Should zombies break blocks?",true);
            this.block_breaking_chance = builder.comment("Default 150").defineInRange("Chance of the block breaking on tick, 150 ~= 1%, the bigger the number, the smaller the chance on tick", 150, 60, 200);
            this.block_breaking_action_sphere = builder.comment("Default 0.15").defineInRange("Area of the blocks that the mob can break, the bigger the number, the more the blocks a mob can destroy in one go", 0.15, 0.05, 0.5);
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

        public DataGen(ForgeConfigSpec.Builder builder) {

            builder.push("Zombie Equipment");
            builder.comment("Items / chance of spawning with it");
            this.zombie_helmet = builder.defineList("Head Slot",
                    Lists.newArrayList("minecraft:chainmail_helmet|50","minecraft:iron_helmet|20","minecraft:leather_helmet|20") , o -> o instanceof String);
            this.zombie_chestplate = builder.defineList("Chest Slot",
                    Lists.newArrayList("minecraft:chainmail_chestplate|50","minecraft:iron_chestplate|20","minecraft:leather_chestplate|20") , o -> o instanceof String);
            this.zombie_main_hand = builder.defineList("Main Hand Slot",
                    Lists.newArrayList("minecraft:stone_sword|50" , "minecraft:iron_sword|20","minecraft:stone_axe|25", "minecraft:iron_axe|20" , "minecraft:golden_sword|30", "minecraft:iron_pickaxe|20") , o -> o instanceof String);
            this.zombie_legs = builder.defineList("Legs Slot",
                    Lists.newArrayList("minecraft:leather_leggings|50","minecraft:iron_leggings|20","minecraft:chainmail_leggings|20") , o -> o instanceof String);
            this.zombie_feet = builder.defineList("Boots Slot",
                    Lists.newArrayList("minecraft:leather_boots|50","minecraft:iron_boots|20","minecraft:chainmail_boots|20") , o -> o instanceof String);
            this.zombie_off_hand = builder.defineList("Off Hand Slot",
                    Lists.newArrayList("minecraft:torch|50","minecraft:shield|30","minecraft:flint_and_steel|30") , o -> o instanceof String);
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