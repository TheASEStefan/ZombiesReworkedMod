package net.teamabyssal.extra;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.teamabyssal.config.ZombiesReworkedConfig;
import net.teamabyssal.entity.ai.*;
import net.teamabyssal.zombies_reworked.ZombiesReworked;

import java.util.Objects;

import static net.minecraft.world.entity.EntityType.DROWNED;

@Mod.EventBusSubscriber(modid = ZombiesReworked.MODID)
public class AiRevamp {


    @SubscribeEvent()
    public static void onZombieHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Zombie || event.getEntity() instanceof Husk || event.getEntity() instanceof Drowned || event.getEntity() instanceof ZombieVillager) {
            Zombie zombie = (Zombie) event.getEntity();
            Entity target = zombie.getLastHurtByMob();
            ItemStack itemStack = zombie.getOffhandItem();
            Item item = itemStack.getItem();
            Item shield = Items.SHIELD;
            Item totem = Items.TOTEM_OF_UNDYING;
            Item pearl = Items.ENDER_PEARL;
            int cnt = 0;
            int sub = 0;
            if (zombie.isAlive() && target != null) {
                if (item == shield && Math.random() <= 0.3F && ZombiesReworkedConfig.SERVER.shield_use.get()) {
                    zombie.level().playSound((Player)null, zombie.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
                     if (!(target instanceof Player player)) {
                         zombie.setHealth(zombie.getHealth() + 1.5F * (float) Math.PI / 4 - (float) Math.atan2(60, 120));
                         zombie.swing(InteractionHand.OFF_HAND);
                     }
                     else {
                         zombie.setHealth(zombie.getHealth() + (float) player.getMainHandItem().getDamageValue() / 3);
                         zombie.swing(InteractionHand.OFF_HAND);
                     }
                }

                else if (item == totem && Math.random() <= 0.5F && event != null && (zombie.getHealth() <= 4 || zombie.isDeadOrDying()) && ZombiesReworkedConfig.SERVER.totem_use.get()) {
                    ++cnt;
                    if (cnt == 1) {

                        zombie.level().playSound((Player)null, zombie.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1.0F, 1.0F);
                        zombie.setHealth(zombie.getHealth() + zombie.getMaxHealth() / 3);
                        zombie.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1), zombie);
                        zombie.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1), zombie);
                        zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0), zombie);
                        zombie.getOffhandItem().setCount(0);
                        zombie.swing(InteractionHand.OFF_HAND);
                        zombie.swing(InteractionHand.MAIN_HAND);



                    }
                }
                else if (item == pearl && Math.random() <= 0.2F && event != null && ZombiesReworkedConfig.SERVER.pearl_use.get()) {
                    ++sub;
                    if (sub == 1) {
                        zombie.level().playSound((Player)null, zombie.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
                        zombie.teleportTo(zombie.getX() + zombie.getRandom().nextInt(2), zombie.getY(), zombie.getZ() + zombie.getRandom().nextInt(5));
                        zombie.getOffhandItem().setCount(0);
                    }
                }

            }

        }
        else {
            LivingEntity entity = event.getEntity();
            LivingEntity attacker = entity.getLastHurtByMob();
            if (attacker != null) {
                ItemStack itemStack = attacker.getOffhandItem();
                Item item = itemStack.getItem();
                Item fire = Items.FLINT_AND_STEEL;
                if (entity.isAlive() && attacker.isAlive()) {
                    if (item == fire && Math.random() <= 0.3F && ZombiesReworkedConfig.SERVER.flint_and_steel_use.get()) {
                        entity.level().playSound((Player) null, entity.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.HOSTILE, 1.0F, 1.0F);
                        entity.setSecondsOnFire(5);
                        attacker.swing(InteractionHand.OFF_HAND);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void FishingADrowned(ItemFishedEvent event) {
        if (event != null) {
            if (Math.random() < 0.05 && event.getHookEntity().isOpenWaterFishing()){
                Drowned drowned = new Drowned(DROWNED,event.getEntity().level());
                drowned.moveTo(event.getHookEntity().getX(),event.getHookEntity().getY(),event.getHookEntity().getZ());
                drowned.setTarget(event.getEntity());
                event.getEntity().level().addFreshEntity(drowned);
            }
        }
    }


    @SubscribeEvent()
    public static void addSpawn(EntityJoinLevelEvent event) {
        
        ItemStack helmetG = ItemStack.EMPTY;
        ItemStack chestG = ItemStack.EMPTY;
        ItemStack legsG = ItemStack.EMPTY;
        ItemStack bootG = ItemStack.EMPTY;
        ItemStack mainG = ItemStack.EMPTY;
        ItemStack offG = ItemStack.EMPTY;

        boolean right = false;




        if (event.getEntity() instanceof Zombie || event.getEntity() instanceof Husk || event.getEntity() instanceof Drowned || event.getEntity() instanceof ZombieVillager) {

            /**
             * /// AI Stuff
             * zombie is all the extra AI the zombies use as of now
             */

            Zombie zombie = (Zombie) event.getEntity();
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Zombie.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Husk.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Drowned.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(ZombieVillager.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.8, Zombie.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.8, ZombieVillager.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.8, Drowned.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.8, Husk.class));

            if (ZombiesReworkedConfig.SERVER.fast_at_night.get() && zombie.level().isNight()) {
                Objects.requireNonNull(zombie.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue((double) 0.28F);
            }

            if (ZombiesReworkedConfig.SERVER.sprint_goal.get() && zombie.getTarget() != null && zombie.getRandom().nextInt(10) == 0 && zombie.distanceTo(zombie.getTarget()) <= 12F && !right) {
                right = true;
                if (right) {
                    Objects.requireNonNull(zombie.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue((double) 0.35F);
                }
                if (right && zombie.getRandom().nextInt(8) == 0) {
                    Objects.requireNonNull(zombie.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(zombie.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
                    right = false;
                }
            }

            /**
             * /// Block Breaking
             */



            ItemStack varstack = zombie.getOffhandItem();
            Item item = varstack.getItem();
            Item spyglass = Items.SPYGLASS;

                if (item == spyglass && ZombiesReworkedConfig.SERVER.spyglass_use.get()) {
                    zombie.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(zombie.getAttributeBaseValue(Attributes.FOLLOW_RANGE) + 64);
                    if (zombie.getTarget() != null) {
                        zombie.getLookControl().setLookAt(zombie.getTarget());
                        if (zombie.distanceTo(zombie.getTarget()) > 32F && zombie.getRandom().nextInt(20) == 0) {
                            zombie.level().playSound((Player)null, zombie.blockPosition(), SoundEvents.SPYGLASS_USE, SoundSource.HOSTILE, 1.0F, 1.0F);
                        }
                    }
                }




            if (ZombiesReworkedConfig.SERVER.zombie_leaps.get()) {
                zombie.goalSelector.addGoal(0, new LeapAtTargetGoal(zombie,0.2F));
            }

            if (ZombiesReworkedConfig.SERVER.zombie_accurate_attack_range.get()) {
                zombie.goalSelector.addGoal(4, new MeleeAttackGoal(zombie, 1.5, false) {
                    @Override
                    protected double getAttackReachSqr(LivingEntity entity) {
                        return 1.5 + entity.getBbWidth() * entity.getBbWidth();
                    }
                });
            }

            /**
             * /// Equipment
             * zombie configures and selects what items to put on the zombies, change the items via the config
             */


            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_helmet.get()) {
                String[] string = str.split("\\|" );
                ItemStack helmet = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F) {
                    helmetG = helmet;
                }
            }
            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_chestplate.get()) {
                String[] string = str.split("\\|" );
                ItemStack chest = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F) {
                    chestG = chest;
                }
            }
            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_legs.get()) {
                String[] string = str.split("\\|" );
                ItemStack legs = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F) {
                    legsG = legs;
                }
            }
            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_feet.get()) {
                String[] string = str.split("\\|" );
                ItemStack boot = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F) {
                    bootG = boot;
                }
            }
            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_main_hand.get()) {
                String[] string = str.split("\\|" );
                ItemStack main = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F && !(zombie instanceof Drowned)) {
                    mainG = main;
                }
            }
            for (String str : ZombiesReworkedConfig.DATAGEN.zombie_off_hand.get()) {
                String[] string = str.split("\\|" );
                ItemStack off = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string[0]))));
                if (Math.random() < Integer.parseUnsignedInt(string[1]) / 100F) {
                    offG = off;
                }
            }

            zombie.setItemSlot(EquipmentSlot.MAINHAND, mainG);
            zombie.setItemSlot(EquipmentSlot.OFFHAND, offG);
            zombie.setItemSlot(EquipmentSlot.HEAD, helmetG);
            zombie.setItemSlot(EquipmentSlot.CHEST, chestG);
            zombie.setItemSlot(EquipmentSlot.LEGS, legsG);
            zombie.setItemSlot(EquipmentSlot.FEET, bootG);

        }
    }


}
