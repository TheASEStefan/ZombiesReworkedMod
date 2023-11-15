package net.workswave.entity.categories;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.workswave.config.RottedConfig;
import net.workswave.entity.ai.*;
import net.workswave.registry.EntityRegistry;
import net.workswave.registry.RottedMobEffects;
import net.workswave.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class RottedZombie extends Monster {
    @Nullable
    BlockPos searchPos;


    public RottedZombie(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.xpReward = 5;
        EntityRegistry.ZOMBIE_ENTITIES.add(this);
    }

    public static boolean checkMonsterRottedZombieRules(EntityType<? extends RottedZombie> p_219014_, ServerLevelAccessor levelAccessor, MobSpawnType p_219016_, BlockPos pos, RandomSource source) {
        if (RottedConfig.SERVER.spawn.get()) {
            if (levelAccessor.dayTime() < (24000L * RottedConfig.SERVER.days.get())) {
                return false;
            }
        }

        return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, pos, source) && checkMobSpawnRules(p_219014_, levelAccessor, p_219016_, pos, source);
    }


    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>
                (this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>
                (this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>
                (this, WanderingTrader.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>
                (this, IronGolem.class, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(RottedZombie.class));
        this.goalSelector.addGoal(10, new FollowOthersGoal(this, 0.7, RottedZombie.class));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Zombie.class));
        this.goalSelector.addGoal(10, new FollowOthersGoal(this, 0.7, Zombie.class));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Drowned.class));
        this.goalSelector.addGoal(10, new FollowOthersGoal(this, 0.7, Drowned.class));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Husk.class));
        this.goalSelector.addGoal(10, new FollowOthersGoal(this, 0.7, Husk.class));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombieVillager.class));
        this.goalSelector.addGoal(10, new FollowOthersGoal(this, 0.7, ZombieVillager.class));
        this.goalSelector.addGoal(10, new FloatDiveGoal(this));
        this.goalSelector.addGoal(4, new SearchAreaGoal(this, 1.2));
        this.goalSelector.addGoal(3, new LocalTargettingGoal(this));


    }

    @Nullable
    public BlockPos getSearchPos() {
        return searchPos;
    }

    public void setSearchPos(@Nullable BlockPos searchPos) {
        this.searchPos = searchPos;
    }


    public void travel(Vec3 p_32858_) {
        if (this.isEffectiveAi() && this.isInFluidType()) {
            this.moveRelative(0.1F, p_32858_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.6D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(p_32858_);
        }

    }
    public void aiStep() {
        super.aiStep();
        Entity attackTarget = this.getTarget();
        if (this.isAlive() && attackTarget != null && this.hasLineOfSight(attackTarget) && this.distanceTo(attackTarget) <= 10F && RottedConfig.SERVER.doMobsBreakBlocks.get()) {

            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                boolean flag = false;
                AABB aabb = this.getBoundingBox().inflate(RottedConfig.SERVER.block_breaking_action_sphere.get());

                for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                    BlockState blockstate = this.level().getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (this.getRandom().nextInt(RottedConfig.SERVER.block_breaking_chance.get()) == 0 && blockstate.getDestroySpeed(level(), blockpos) < getDestroySpeed() && blockstate.getDestroySpeed(level(), blockpos) >= 0)  {
                        this.level().playSound((Player)null, this.blockPosition(), SoundRegistry.ENTITY_ROTTED_ZOMBIE_BREAKS_BLOCK.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                        flag = this.level().destroyBlock(blockpos, false, this) || flag;
                    }

                }

            }


        }
    }
    public int getDestroySpeed(){
        return 5;
    }

    public int getMaxAirSupply() {
        return 1200;
    }
    protected int increaseAirSupply(int p_28389_) {
        return this.getMaxAirSupply();
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    @Override
    public boolean doHurtTarget(Entity entity) {
        if(RottedConfig.SERVER.rotted_zombie_contagion_effect.get() == true) {
                float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                f = f + EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType()) / 2;
                if(Math.random() <= RottedConfig.SERVER.rotted_zombie_contagion_hit_chance.get()) {
                  if (entity instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(RottedMobEffects.CONTAGION.get(), 600, 0), this);
                }
            }
        }
        return super.doHurtTarget(entity);
    }
}


