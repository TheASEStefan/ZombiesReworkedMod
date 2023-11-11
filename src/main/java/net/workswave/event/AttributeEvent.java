package net.workswave.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.entity.custom.*;
import net.workswave.registry.EntityRegistry;
import net.workswave.rotted.Rotted;

@Mod.EventBusSubscriber(modid = Rotted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(EntityRegistry.MARINE.get(), MarineEntity.createAttributes().build());
        event.put(EntityRegistry.DOCTOR.get(), DoctorEntity.createAttributes().build());
        event.put(EntityRegistry.FARMER.get(), FarmerEntity.createAttributes().build());
        event.put(EntityRegistry.MINER.get(), MinerEntity.createAttributes().build());
        event.put(EntityRegistry.ADVENTURER.get(), AdventurerEntity.createAttributes().build());
        event.put(EntityRegistry.FLUSK.get(), FluskEntity.createAttributes().build());
        event.put(EntityRegistry.SHIELDER.get(), ShielderEntity.createAttributes().build());

    }



}
