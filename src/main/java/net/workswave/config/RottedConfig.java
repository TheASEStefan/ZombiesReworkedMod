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


    static {
        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();



    }
    public static class Server {

        public final ForgeConfigSpec.ConfigValue<Double> past_loot_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> past_loot;
        public final ForgeConfigSpec.ConfigValue<Integer> days;
        public final ForgeConfigSpec.ConfigValue<Integer> mob_cap;
        public final ForgeConfigSpec.ConfigValue<Boolean> spawn;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dimension_parameters;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> spawns;
        public final ForgeConfigSpec.ConfigValue<Double> firefighter_health;
        public final ForgeConfigSpec.ConfigValue<Double> firefighter_damage;
        public final ForgeConfigSpec.ConfigValue<Double> marine_health;
        public final ForgeConfigSpec.ConfigValue<Double> marine_damage;
        public final ForgeConfigSpec.ConfigValue<Double> doctor_health;
        public final ForgeConfigSpec.ConfigValue<Double> doctor_damage;
        public final ForgeConfigSpec.ConfigValue<Double> farmer_health;
        public final ForgeConfigSpec.ConfigValue<Double> farmer_damage;
        public final ForgeConfigSpec.ConfigValue<Double> miner_health;
        public final ForgeConfigSpec.ConfigValue<Double> miner_damage;
        public final ForgeConfigSpec.ConfigValue<Integer> at_potion_meter;
        public final ForgeConfigSpec.ConfigValue<Integer> buff_potion_meter;
        public final ForgeConfigSpec.ConfigValue<Boolean> use_potions;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> buffing_potions;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> harming_potions;
        public final ForgeConfigSpec.ConfigValue<Boolean> rotted_zombie_contagion_effect;
        public final ForgeConfigSpec.ConfigValue<Double> rotted_zombie_contagion_hit_chance;
        public final ForgeConfigSpec.ConfigValue<Boolean> vanilla_zombie__contagion_effect;
        public final ForgeConfigSpec.ConfigValue<Double> vanilla_zombie_contagion_hit_chance;


        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Firefighter");
            this.firefighter_health = builder.comment("Default 25").defineInRange("Sets Firefighter's Max health", 25, 10, Double.MAX_VALUE);
            this.firefighter_damage = builder.comment("Default 6").defineInRange("Sets Firefighter's Damage", 6, 2, Double.MAX_VALUE);
            builder.pop();
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
            this.mob_cap = builder.comment("Default 50").define("MobCap",50);
            this.dimension_parameters = builder.comment("Default minecraft:is_overworld").defineList("Dictates in what biome the zombies spawn",
                    Lists.newArrayList("minecraft:is_overworld") , o -> o instanceof String);
            this.days = builder.comment("Default 3").define("Days before zombies start spawning",3);
            this.spawns = builder.defineList("mob|weight|minimum|maximum",
                    Lists.newArrayList("rotted:marine|25|1|2","rotted:firefighter|45|1|4","rotted:doctor|40|1|3","rotted:farmer|60|2|5","rotted:miner|50|1|3") , o -> o instanceof String);
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

        }
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}