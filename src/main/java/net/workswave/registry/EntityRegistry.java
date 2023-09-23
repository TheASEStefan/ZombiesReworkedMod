package net.workswave.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.config.RottedConfig;
import net.workswave.entity.custom.*;
import net.workswave.rotted.Rotted;

import java.util.ArrayList;
import java.util.List;

public class EntityRegistry {


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Rotted.MODID);


    public static final MobCategory ZOMBIE = MobCategory.create("zombie","zombie", RottedConfig.SERVER.mob_cap.get(),false,false,128);

    public  static  final List<Entity> ZOMBIE_ENTITIES = new ArrayList<>();
    public static final RegistryObject<EntityType<MarineEntity>> MARINE =
            ENTITY_TYPES.register("marine",
                    () -> EntityType.Builder.of(MarineEntity::new, ZOMBIE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(Rotted.MODID, "marine").toString()));
    public static final RegistryObject<EntityType<FirefighterEntity>> FIREFIGHTER =
            ENTITY_TYPES.register("firefighter",
                    () -> EntityType.Builder.of(FirefighterEntity::new, ZOMBIE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(Rotted.MODID, "firefighter").toString()));
    public static final RegistryObject<EntityType<DoctorEntity>> DOCTOR =
            ENTITY_TYPES.register("doctor",
                    () -> EntityType.Builder.of(DoctorEntity::new, ZOMBIE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(Rotted.MODID, "doctor").toString()));

    public static final RegistryObject<EntityType<FarmerEntity>> FARMER =
            ENTITY_TYPES.register("farmer",
                    () -> EntityType.Builder.of(FarmerEntity::new, ZOMBIE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(Rotted.MODID, "farmer").toString()));
    public static final RegistryObject<EntityType<MinerEntity>> MINER =
            ENTITY_TYPES.register("miner",
                    () -> EntityType.Builder.of(MinerEntity::new, ZOMBIE)
                            .sized(0.8f, 1.8f)
                            .build(new ResourceLocation(Rotted.MODID, "miner").toString()));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
