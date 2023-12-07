package net.teamabyssal.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamabyssal.config.ZombiesReworkedConfig;


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

            if (entity instanceof Player player && !world.isClientSide && (ZombiesReworkedConfig.SERVER.player_assimilation.get()) && Math.random() <= ZombiesReworkedConfig.SERVER.assimilation_chance.get() && player.getLastDamageSource() != null && player.getLastDamageSource().getEntity() instanceof Zombie) {
                Component name = player.getName();
                Zombie zombie = EntityType.ZOMBIE.create(world);
                ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                ItemStack hand = player.getItemBySlot(EquipmentSlot.MAINHAND);
                ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
                assert zombie != null;
                zombie.setItemSlot(EquipmentSlot.HEAD, head);
                zombie.setItemSlot(EquipmentSlot.CHEST, chest);
                zombie.setItemSlot(EquipmentSlot.LEGS, legs);
                zombie.setItemSlot(EquipmentSlot.FEET, boots);
                zombie.setItemSlot(EquipmentSlot.MAINHAND, hand);
                zombie.setItemSlot(EquipmentSlot.OFFHAND, offhand);
                zombie.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                zombie.setCustomName(name);
                zombie.setDropChance(EquipmentSlot.HEAD, 0);
                zombie.setDropChance(EquipmentSlot.CHEST, 0);
                zombie.setDropChance(EquipmentSlot.MAINHAND, 0);
                zombie.setDropChance(EquipmentSlot.OFFHAND, 0);
                zombie.setDropChance(EquipmentSlot.FEET, 0);
                zombie.setDropChance(EquipmentSlot.LEGS, 0);
                world.addFreshEntity(zombie);
                zombie.playSound(SoundEvents.ZOMBIE_INFECT);


                if (player.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY() + 1, player.getZ(), 4, 0.4, 1.0, 0.4, 0);
                }
            }
            else if (entity instanceof Villager villager && !world.isClientSide && Math.random() <= ZombiesReworkedConfig.SERVER.assimilation_chance.get() && villager.getLastDamageSource() != null && villager.getLastDamageSource().getEntity() instanceof Zombie) {
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

