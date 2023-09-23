package net.workswave.extra;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.config.RottedConfig;
import net.workswave.registry.RottedMobEffects;

@Mod.EventBusSubscriber
public class ZombieSpreadsVirus {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event != null && event.getEntity() != null && !event.getEntity().level().isClientSide) {
            Entity attacker = event.getSource().getEntity();
            Entity target = event.getEntity();
            if(RottedConfig.SERVER.vanilla_zombie__contagion_effect.get() == true) {
                if (attacker instanceof Zombie || attacker instanceof Drowned || attacker instanceof ZombieVillager || attacker instanceof Husk) {

                    if (target instanceof LivingEntity) {
                        if (Math.random() <= RottedConfig.SERVER.vanilla_zombie_contagion_hit_chance.get()){
                            ((LivingEntity) target).addEffect(new MobEffectInstance(RottedMobEffects.CONTAGION.get(), 600, 0), target);

                        }
                    }
                }


            }
        }
    }
}
