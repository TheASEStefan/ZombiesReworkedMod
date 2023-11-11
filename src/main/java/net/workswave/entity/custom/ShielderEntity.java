package net.workswave.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.workswave.config.RottedConfig;
import net.workswave.entity.ai.CustomMeleeAttackGoal;
import net.workswave.entity.categories.RottedZombie;
import net.workswave.registry.ItemRegistry;
import net.workswave.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Objects;


public class ShielderEntity extends RottedZombie implements GeoEntity {

    public int chargetime = 0;
    private boolean charge = false;
    private boolean sure = false;



    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ShielderEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 12;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(RottedZombie.class));
        this.goalSelector.addGoal(4, new CustomMeleeAttackGoal(this, 1.5, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return 1.5 + entity.getBbWidth() * entity.getBbWidth();
            }
        });
    }



    @Override
    public void tick() {
        super.tick();
        this.chargetime = 0;
        Entity attackTarget = this.getTarget();
        if (this.isAlive() && attackTarget != null && attackTarget.isAlive() && this.hasLineOfSight(attackTarget) && (!this.isInWater() && this.onGround())) {
            if (this.getRandom().nextInt(100) == 0 && this.distanceTo(attackTarget) <= 16F && chargetime <= 0) {
                this.charge = true;
                this.chargetime = 120;
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.3D);
            }
            if (this.charge && chargetime > 0) {
                --this.chargetime;
            }
            if (this.charge && chargetime < 0) {
                this.charge = false;
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.2D);
            }
        }
    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 32D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.MAX_HEALTH, RottedConfig.SERVER.shielder_health.get())
                .add(Attributes.ATTACK_DAMAGE, RottedConfig.SERVER.shielder_damage.get())
                .add(Attributes.ARMOR, 8D);

    }




    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(
                new AnimationController<>(this, "data", 5, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (event.isMoving()) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
                    }
                    if (event.isMoving() && this.chargetime > 0 && this.charge && Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).getBaseValue() == 0.3D) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("charge"));
                    }
                    if (!event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                    }

                    return PlayState.CONTINUE;
                }));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        this.sure = false;
        if (pSource.is(DamageTypes.MOB_ATTACK) && Math.random() <= 0.3F) {

                this.sure = true;
                return super.hurt(pSource, pAmount / 3);
        }

        return super.hurt(pSource, pAmount);

    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        double d0 = entity.getX() - this.getX();
        double d1 = entity.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        if (this.charge && this.chargetime > 0) {

            entity.push(d0 / d2 * 3.5D, 0.3D, d1 / d2 * 3.0D);
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
        if (this.sure) {
            return SoundEvents.SHIELD_BLOCK;
        }
        return SoundRegistry.ENTITY_ROTTED_ZOMBIE_HURT.get();
    }


    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.ENTITY_ROTTED_ZOMBIE_DEATH.get();
    }


    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.5F, 1.0F);
    }


    protected SoundEvent getSwimSound() {
        return SoundEvents.DROWNED_SWIM;
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        Entity entity = pSource.getEntity();
        if (Math.random() <= 0.1F) {
            this.spawnAtLocation(ItemRegistry.ROTTEN_BRAIN.get());
        }
        if (Math.random() <= 0.6F) {
            this.spawnAtLocation(Items.ROTTEN_FLESH);
        }
        if (Math.random() <= 0.1F) {
            this.spawnAtLocation(Items.ROTTEN_FLESH);
        }

    }
}
