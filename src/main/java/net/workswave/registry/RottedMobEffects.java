package net.workswave.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.effects.Contagion;
import net.workswave.rotted.Rotted;


public class RottedMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Rotted.MODID);
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }



    public static final RegistryObject<MobEffect> CONTAGION = MOB_EFFECTS.register("contagion",
            () -> new Contagion().addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            "91AEAA56-376B-4498-935B-2F7F68070635", -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED,
                            "91AEAA56-376B-4498-935B-2F7F68070635", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));

}



