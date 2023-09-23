package net.workswave.registry;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.rotted.Rotted;

@EventBusSubscriber(modid = Rotted.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Rotted.MODID);
    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    private static RegistryObject<SoundEvent> soundRegistry(String id){
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Rotted.MODID, id)));
    }

    public static final RegistryObject<SoundEvent> ENTITY_ROTTED_ZOMBIE_AMBIENT  = soundRegistry("entity.rotted_zombie.ambient");
    public static final RegistryObject<SoundEvent> ENTITY_ROTTED_ZOMBIE_HURT = soundRegistry("entity.rotted_zombie.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ROTTED_ZOMBIE_DEATH = soundRegistry("entity.rotted_zombie.death");


}
