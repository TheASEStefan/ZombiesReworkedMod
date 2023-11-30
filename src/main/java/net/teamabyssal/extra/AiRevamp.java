package net.teamabyssal.extra;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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
            if (zombie.isAlive() && target != null) {
                if (item == shield && Math.random() <= 0.3F) {
                    zombie.level().playSound((Player)null, zombie.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
                     if(!(target instanceof Player player)) {
                         zombie.setHealth(zombie.getHealth() + 1.5F * (float) Math.PI / 4 - (float) Math.atan2(60, 120));
                         zombie.swing(InteractionHand.OFF_HAND);
                     }
                     else {
                         zombie.setHealth(zombie.getHealth() + (float) player.getMainHandItem().getDamageValue() / 3);
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
                    if (item == fire && Math.random() <= 0.3F) {
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



    public static int getDestroySpeed() {
        return 5;
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

            ItemStack itemStack = zombie.getMainHandItem();
            Item item = itemStack.getItem();
            Item pickaxe = Items.IRON_PICKAXE;
            if (item == pickaxe) {


                Entity attackTarget = zombie.getTarget();
                if (zombie.isAlive() && attackTarget != null && zombie.hasLineOfSight(attackTarget) && zombie.distanceTo(attackTarget) <= 10F && ZombiesReworkedConfig.SERVER.zombies_break_blocks.get()) {

                    if (zombie.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(zombie.level(), zombie)) {
                        boolean flag = false;
                        AABB aabb = zombie.getBoundingBox().inflate(ZombiesReworkedConfig.SERVER.block_breaking_action_sphere.get());

                        for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                            BlockState blockstate = zombie.level().getBlockState(blockpos);
                            Block block = blockstate.getBlock();
                            if (zombie.getRandom().nextInt(ZombiesReworkedConfig.SERVER.block_breaking_chance.get()) == 0 && blockstate.getDestroySpeed(zombie.level(), blockpos) < getDestroySpeed() && blockstate.getDestroySpeed(zombie.level(), blockpos) >= 0) {
                                zombie.level().playSound((Player) null, zombie.blockPosition(), SoundEvents.ZOMBIE_HORSE_AMBIENT, SoundSource.HOSTILE, 1.0F, 0.6F);
                                flag = zombie.level().destroyBlock(blockpos, false, zombie) || flag;
                            }


                        }


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
