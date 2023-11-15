package net.workswave.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class RottedConfig {
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


        public final ForgeConfigSpec.ConfigValue<Double> past_loot_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> past_loot;
        public final ForgeConfigSpec.ConfigValue<Integer> days;
        public final ForgeConfigSpec.ConfigValue<Integer> mob_cap;
        public final ForgeConfigSpec.ConfigValue<Boolean> spawn;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dimension_parameters;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spawns;
        public final ForgeConfigSpec.ConfigValue<Double> marine_health;
        public final ForgeConfigSpec.ConfigValue<Double> marine_damage;
        public final ForgeConfigSpec.ConfigValue<Double> doctor_health;
        public final ForgeConfigSpec.ConfigValue<Double> doctor_damage;
        public final ForgeConfigSpec.ConfigValue<Double> farmer_health;
        public final ForgeConfigSpec.ConfigValue<Double> farmer_damage;
        public final ForgeConfigSpec.ConfigValue<Double> miner_health;
        public final ForgeConfigSpec.ConfigValue<Double> miner_damage;
        public final ForgeConfigSpec.ConfigValue<Double> adventurer_health;
        public final ForgeConfigSpec.ConfigValue<Double> adventurer_damage;
        public final ForgeConfigSpec.ConfigValue<Double> flusk_health;
        public final ForgeConfigSpec.ConfigValue<Double> flusk_damage;
        public final ForgeConfigSpec.ConfigValue<Double> shielder_health;
        public final ForgeConfigSpec.ConfigValue<Double> shielder_damage;
        public final ForgeConfigSpec.ConfigValue<Integer> at_potion_meter;
        public final ForgeConfigSpec.ConfigValue<Integer> buff_potion_meter;
        public final ForgeConfigSpec.ConfigValue<Boolean> use_potions;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> buffing_potions;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> harming_potions;
        public final ForgeConfigSpec.ConfigValue<Boolean> rotted_zombie_contagion_effect;
        public final ForgeConfigSpec.ConfigValue<Double> rotted_zombie_contagion_hit_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> vanilla_zombie__contagion_effect;
        public final ForgeConfigSpec.ConfigValue<Double> vanilla_zombie_contagion_hit_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> vanilla_zombie_leaps;
        public final ForgeConfigSpec.ConfigValue<Boolean> vanilla_zombie_decreased_attack_range;
        public final ForgeConfigSpec.ConfigValue<Boolean> adventurer_assimilation;
        public final ForgeConfigSpec.ConfigValue<Boolean> villager_assimilation;
        public final ForgeConfigSpec.ConfigValue<Integer> block_breaking_chance;
        public final ForgeConfigSpec.ConfigValue<Double> block_breaking_action_sphere;
        public final ForgeConfigSpec.ConfigValue<Boolean> doMobsBreakBlocks;


        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Marine");
            this.marine_health = builder.comment("Default 20").defineInRange("Sets Marine's Max health", 20, 10, Double.MAX_VALUE);
            this.marine_damage = builder.comment("Default 4").defineInRange("Sets Marine's Damage", 4, 2, Double.MAX_VALUE);
            builder.pop();
            builder.push("Farmer");
            this.farmer_health = builder.comment("Default 12").defineInRange("Sets Farmer's Max health", 12, 6, Double.MAX_VALUE);
            this.farmer_damage = builder.comment("Default 4").defineInRange("Sets Farmer's Damage", 4, 2, Double.MAX_VALUE);
            builder.pop();
            builder.push("Miner");
            this.miner_health = builder.comment("Default 16").defineInRange("Sets Miner's Max health", 16, 6, Double.MAX_VALUE);
            this.miner_damage = builder.comment("Default 4").defineInRange("Sets Miner's Damage", 4, 2, Double.MAX_VALUE);
            builder.pop();
            builder.push("Adventurer");
            this.adventurer_health = builder.comment("Default 18").defineInRange("Sets Adventurer's Max health", 18, 6, Double.MAX_VALUE);
            this.adventurer_damage = builder.comment("Default 3").defineInRange("Sets Adventurer's Damage", 3, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Shielder");
            this.shielder_health = builder.comment("Default 28").defineInRange("Sets Shielder's Max health", 28, 8, Double.MAX_VALUE);
            this.shielder_damage = builder.comment("Default 2").defineInRange("Sets Shielder's Damage", 2, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Flusk");
            this.flusk_health = builder.comment("Default 30").defineInRange("Sets Flusk's Max health", 30, 10, Double.MAX_VALUE);
            this.flusk_damage = builder.comment("Default 4").defineInRange("Sets Flusk's Damage", 4, 2, Double.MAX_VALUE);
            builder.pop();
            builder.push("Doctor");
            this.doctor_health = builder.comment("Default 15").defineInRange("Sets Doctor's Max health", 15, 5, Double.MAX_VALUE);
            this.doctor_damage = builder.comment("Default 3").defineInRange("Sets Doctor's Damage", 3, 1, Double.MAX_VALUE);
            this.buffing_potions = builder.defineList("Potions that are used to buff the zombies , NOT effects",
                    Lists.newArrayList("minecraft:fire_resistance","minecraft:regeneration","minecraft:strength" ) , o -> o instanceof String);
            this.harming_potions = builder.defineList("Potions that are used to attack others , NOT effects",
                    Lists.newArrayList("minecraft:weakness","minecraft:poison") , o -> o instanceof String);
            this.use_potions = builder.comment("Default true").define("Should Doctors use Potions?",true);
            this.at_potion_meter = builder.comment("Default 60").defineInRange("Sets the time before throwing a potion when attacking", 60, 1, Integer.MAX_VALUE);
            this.buff_potion_meter = builder.comment("Default 40").defineInRange("Sets the time before throwing a potion when buffing", 40, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.push("Spawns");
            this.spawn = builder.comment("Default false").define("Should mobs spawn after a few days?",false);
            this.mob_cap = builder.comment("Default 60").define("MobCap",60);
            this.dimension_parameters = builder.comment("Default minecraft:is_overworld").defineList("Dictates in what biome the zombies spawn",
                    Lists.newArrayList("minecraft:is_overworld") , o -> o instanceof String);
            this.days = builder.comment("Default 3").define("Days before zombies start spawning",3);
            this.spawns = builder.defineList("mob|weight|minimum|maximum",
                    Lists.newArrayList("rotted:marine|25|1|2","rotted:doctor|40|1|3","rotted:farmer|60|2|5","rotted:miner|50|1|3","rotted:adventurer|55|2|4","rotted:flusk|20|1|1","rotted:shielder|25|1|2") , o -> o instanceof String);
            builder.pop();
            builder.push("Past Loot");
            this.past_loot_chance = builder.comment("Default 0.15").defineInRange("Sets the past-loot drop chance( loot from the zombie's past life )", 0.15, 0, 1);
            this.past_loot = builder.comment("Default true").define("Should Farmers or other Zombies drop past-loot( wheat, iron_ore, carrot, etc. )?",true);
            builder.pop();
            builder.push("Contagion");
            this.rotted_zombie_contagion_hit_chance = builder.comment("Default 0.8").defineInRange("The chance of the mob applying this effect when hitting you", 0.8, 0.1, 1);
            this.rotted_zombie_contagion_effect = builder.comment("Default true").define("Should this effect be applied when zombies hit you or other entities?",true);
            builder.pop();
            builder.push("Contagion Vanilla Zombies");
            this.vanilla_zombie_contagion_hit_chance = builder.comment("Default 0.2").defineInRange("The chance of the mob applying this effect when hitting you", 0.2, 0.1, 1);
            this.vanilla_zombie__contagion_effect = builder.comment("Default true").define("Should this effect be applied when zombies hit you or other entities?",true);
            builder.pop();
            builder.push("Additional AI Changes for the vanilla zombies");
            this.vanilla_zombie_leaps = builder.comment("Default true").define("Should the vanilla zombies perform small leaps at the player?",true);
            this.vanilla_zombie_decreased_attack_range = builder.comment("Default true").define("Should the vanilla zombies have decreased range( rotted-zombie-like ) ?",true);
            builder.pop();
            builder.push("Zombifications");
            this.adventurer_assimilation = builder.comment("Default true").define("Should the player convert after dying with the contagion effect?",true);
            this.villager_assimilation = builder.comment("Default true").define("Should the villager convert after dying with the contagion effect?",true);
            builder.pop();
            builder.push("AI Options + Block Breaking Options");
            this.doMobsBreakBlocks = builder.comment("Default true").define("Should the rotted zombies break blocks?",true);
            this.block_breaking_chance = builder.comment("Default 90").defineInRange("Chance of the block breaking on tick, 90 ~= 1.5%", 90, 60, 200);
            this.block_breaking_action_sphere = builder.comment("Default 0.15").defineInRange("Area of the blocks that the mob can break, the bigger the number, the more the blocks a mob can destroy in one go", 0.15, 0.05, 0.5);
            builder.pop();


        }
    }
    public static class DataGen {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> adventurer_helmet;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> adventurer_chestplate;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> adventurer_main_hand;

        public DataGen(ForgeConfigSpec.Builder builder) {

            builder.push("Adventurer Zombie Equipment");
            builder.comment("Items / chance of spawning with it");
            this.adventurer_helmet = builder.defineList("Head Slot",
                    Lists.newArrayList("minecraft:chainmail_helmet|50","minecraft:iron_helmet|20","minecraft:golden_helmet|20") , o -> o instanceof String);
            this.adventurer_chestplate = builder.defineList("Chest Slot",
                    Lists.newArrayList("minecraft:chainmail_chestplate|50","minecraft:iron_chestplate|20","minecraft:golden_chestplate|20") , o -> o instanceof String);
            this.adventurer_main_hand = builder.defineList("Main Hand Slot",
                    Lists.newArrayList("minecraft:stone_sword|50" , "minecraft:golden_sword|20","minecraft:iron_axe|25", "minecraft:golden_axe|20" , "minecraft:iron_sword|30") , o -> o instanceof String);
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