package net.workswave.extra;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.registry.ItemRegistry;

@Mod.EventBusSubscriber

public class ZombieDies {


    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null && !event.getEntity().level().isClientSide) {
            Entity entity = event.getEntity();

            if (entity instanceof Zombie) {

                    if(Math.random() <= 0.2f) {
                        event.getEntity().spawnAtLocation(ItemRegistry.ROTTEN_BRAIN.get());
                    }
            }
        }
    }
}
