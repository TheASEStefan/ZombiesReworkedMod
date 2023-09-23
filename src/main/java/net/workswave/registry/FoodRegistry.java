package net.workswave.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties ROTTEN_BRAIN = new FoodProperties.Builder().nutrition(1).fast()
            .saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.POISON, 200), 1f).effect(() ->
                    new MobEffectInstance(MobEffects.CONFUSION, 200), 1f).build();
}
