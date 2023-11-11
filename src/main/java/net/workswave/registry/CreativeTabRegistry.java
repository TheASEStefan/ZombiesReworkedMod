package net.workswave.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.rotted.Rotted;


public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Rotted.MODID);



    public static final RegistryObject<CreativeModeTab> ITEM = TABS.register("items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Rotted.MODID + ".item")).icon(() -> new ItemStack(ItemRegistry.ROTTEN_BRAIN.get())).displayItems((enabledFeatures, entries) -> {
        entries.accept(ItemRegistry.MARINE_SPAWN_EGG.get());
        entries.accept(ItemRegistry.DOCTOR_SPAWN_EGG.get());
        entries.accept(ItemRegistry.FARMER_SPAWN_EGG.get());
        entries.accept(ItemRegistry.MINER_SPAWN_EGG.get());
        entries.accept(ItemRegistry.ADVENTURER_SPAWN_EGG.get());
        entries.accept(ItemRegistry.FLUSK_SPAWN_EGG.get());
        entries.accept(ItemRegistry.SHIELDER_SPAWN_EGG.get());
        entries.accept(ItemRegistry.ROTTEN_BRAIN.get());
        entries.accept(ItemRegistry.FLUSK_VISCERA.get());
        entries.accept(ItemRegistry.VISCERIC_KNIFE.get());

    }).build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

}
