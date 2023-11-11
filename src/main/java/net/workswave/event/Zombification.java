package net.workswave.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.config.RottedConfig;
import net.workswave.entity.custom.AdventurerEntity;
import net.workswave.registry.EntityRegistry;
import net.workswave.registry.RottedMobEffects;

@Mod.EventBusSubscriber
public class Zombification {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null && !event.getEntity().level().isClientSide) {
            Level world = event.getEntity().level();
            double x = event.getEntity().getX();
            double y = event.getEntity().getY();
            double z = event.getEntity().getZ();
            Entity entity = event.getEntity();

            if (entity instanceof Player player && player.hasEffect(RottedMobEffects.CONTAGION.get()) && !world.isClientSide && (RottedConfig.SERVER.adventurer_assimilation.get() == true)) {
                Component name = player.getName();
                AdventurerEntity adventurerEntity = EntityRegistry.ADVENTURER.get().create(world);
                ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack hand = player.getItemBySlot(EquipmentSlot.MAINHAND);
                assert adventurerEntity != null;
                adventurerEntity.setItemSlot(EquipmentSlot.HEAD, head);
                adventurerEntity.setItemSlot(EquipmentSlot.CHEST, chest);
                adventurerEntity.setItemSlot(EquipmentSlot.MAINHAND, hand);
                adventurerEntity.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                adventurerEntity.setCustomName(name);
                adventurerEntity.setDropChance(EquipmentSlot.HEAD, 0);
                adventurerEntity.setDropChance(EquipmentSlot.CHEST, 0);
                adventurerEntity.setDropChance(EquipmentSlot.MAINHAND, 0);
                world.addFreshEntity(adventurerEntity);
                adventurerEntity.playSound(SoundEvents.ZOMBIE_INFECT);
                if (player.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY() + 1, player.getZ(), 4, 0.4, 1.0, 0.4, 0);
                }
            }
            else if (entity instanceof Villager villager && villager.hasEffect(RottedMobEffects.CONTAGION.get()) && !world.isClientSide && (RottedConfig.SERVER.villager_assimilation.get() == true)) {
                ZombieVillager zombieVillager = EntityType.ZOMBIE_VILLAGER.create(world);
                assert zombieVillager != null;
                zombieVillager.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                world.addFreshEntity(zombieVillager);
                zombieVillager.playSound(SoundEvents.ZOMBIE_INFECT);
                if (villager.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.EXPLOSION, villager.getX(), villager.getY() + 1, villager.getZ(), 4, 0.4, 1.0, 0.4, 0);
                }
            }
        }
    }
}

