package net.workswave.registry;


import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.items.ViscericKnife;
import net.workswave.rotted.Rotted;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Rotted.MODID);

    public static final RegistryObject<Item> MARINE_SPAWN_EGG = ITEMS.register("marine_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.MARINE, -16764109, -16737844, new Item.Properties()));
    public static final RegistryObject<Item> DOCTOR_SPAWN_EGG = ITEMS.register("doctor_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.DOCTOR, 0xf6f5f5, 0x151313, new Item.Properties()));
    public static final RegistryObject<Item> FARMER_SPAWN_EGG = ITEMS.register("farmer_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.FARMER, 0xb2b312, 0x6d6d0b, new Item.Properties()));
    public static final RegistryObject<Item> MINER_SPAWN_EGG = ITEMS.register("miner_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.MINER, 0x241b0c, 0x563008, new Item.Properties()));
    public static final RegistryObject<Item> ADVENTURER_SPAWN_EGG = ITEMS.register("adventurer_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.ADVENTURER, 0x1212b3, 0x1263b3, new Item.Properties()));
    public static final RegistryObject<Item> FLUSK_SPAWN_EGG = ITEMS.register("flusk_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.FLUSK, 0xb1b312, 0x3f0706, new Item.Properties()));
    public static final RegistryObject<Item> SHIELDER_SPAWN_EGG = ITEMS.register("shielder_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.SHIELDER, 0x262626, 0x000000, new Item.Properties()));

    public static final RegistryObject<Item> ROTTEN_BRAIN = ITEMS.register("rotten_brain",
            () -> new Item(new Item.Properties().food(FoodRegistry.ROTTEN_BRAIN).stacksTo(64)));
    public  static final RegistryObject<Item> FLUSK_VISCERA = ITEMS.register("flusk_viscera",
            () -> new Item( new Item.Properties()));
    public  static final RegistryObject<Item> VISCERIC_KNIFE = ITEMS.register("visceric_knife", ViscericKnife::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
