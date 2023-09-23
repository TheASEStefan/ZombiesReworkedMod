package net.workswave.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.workswave.config.RottedConfig;
import net.workswave.entity.ai.CustomMeleeAttackGoal;
import net.workswave.entity.ai.ReturnToWater;
import net.workswave.entity.categories.RottedWaterZombie;
import net.workswave.entity.categories.RottedZombie;
import net.workswave.registry.ItemRegistry;
import net.workswave.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;


public class MarineEntity extends RottedZombie implements GeoEntity, RottedWaterZombie {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;


    public MarineEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 10;
        ;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, pLevel);
        this.groundNavigation = new GroundPathNavigation(this, pLevel);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new ReturnToWater(this, 1.2));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.2, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return 1.5 + entity.getBbWidth() * entity.getBbWidth();
            }
        });
    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, RottedConfig.SERVER.marine_health.get())
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, RottedConfig.SERVER.marine_damage.get())
                .add(Attributes.ARMOR, 8D)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5);

    }

    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }


    public void travel(Vec3 p_32858_) {
        if (this.isEffectiveAi() && this.isInFluidType()) {
            this.moveRelative(0.1F, p_32858_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85D));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));

        } else {
            super.travel(p_32858_);
        }
    }

    @Override
    public float getStepHeight() {
        return this.isInFluidType() ? 2.0f : 1.0f;
    }

    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isEyeInFluidType(Fluids.WATER.getFluidType())) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.setSwimming(false);
            }
        }

    }

    public int getMaxAirSupply() {
        return 600;
    }

    protected int increaseAirSupply(int p_28389_) {
        return this.getMaxAirSupply();
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }


    public MobType getMobType() {
        return MobType.UNDEAD;
    }


    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.ENTITY_ROTTED_ZOMBIE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundRegistry.ENTITY_ROTTED_ZOMBIE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.ENTITY_ROTTED_ZOMBIE_DEATH.get();
    }


    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
        this.playSound(SoundEvents.DROWNED_STEP, 0.5F, 1.0F);
    }


    protected SoundEvent getSwimSound() {
        return SoundEvents.DROWNED_SWIM;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (event.isMoving()) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("aggression"));
                    }
                    if (isAggressive() && !isUnderWater()) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("aggression"));
                    }
                    if (isUnderWater() && getTarget() == null) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("water idle"));
                    }
                    if (isUnderWater() && getTarget() != null) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                }));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


        protected void dropCustomDeathLoot (DamageSource pSource,int pLooting, boolean pRecentlyHit){
            super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
            Entity entity = pSource.getEntity();
            if(Math.random() <= 0.1F) {
                this.spawnAtLocation(ItemRegistry.ROTTEN_BRAIN.get());
            }
            if(Math.random() <= 0.6F) {
                this.spawnAtLocation(Items.ROTTEN_FLESH);
            }
            if(Math.random() <= 0.1F) {
                this.spawnAtLocation(Items.ROTTEN_FLESH);
            }


        }
    public boolean canBreatheUnderwater() {
        return true;
    }


}