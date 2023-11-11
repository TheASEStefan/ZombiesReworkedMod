package net.workswave.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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


public class MinerEntity extends RottedZombie implements GeoEntity {

    private int loot = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public MinerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 10;

    }


    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        f = f + EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType()) / 2;
        return super.doHurtTarget(entity);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.2, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return 1.5 + entity.getBbWidth() * entity.getBbWidth();
            }
        });
        this.goalSelector.addGoal(0, new LeapAtTargetGoal(this,0.4F));
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

    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 32D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, RottedConfig.SERVER.miner_health.get())
                .add(Attributes.ATTACK_DAMAGE, RottedConfig.SERVER.miner_damage.get())
                .add(Attributes.ARMOR, 8D);

    }



    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
            int i = pRandom.nextInt(5);
            this.loot = 0;
            if (i == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                this.loot = 1;

            }
            else if (i == 1) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                this.loot = 2;

            }
            else if (i == 2) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_PICKAXE));
                this.loot = 3;

            }
            else if (i == 3) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
                this.loot = 4;

            }
            else {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.loot = 0;
            }



    }
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);

        return pSpawnData;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller2) {
        controller2.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (event.isMoving()) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("aggression"));
                    }
                    if(!event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                    }
                    return PlayState.CONTINUE;
                }));
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
        if(Math.random() <= 0.1F) {
            this.spawnAtLocation(ItemRegistry.ROTTEN_BRAIN.get());
        }
        if(Math.random() <= 0.6F) {
            this.spawnAtLocation(Items.ROTTEN_FLESH);
        }
        if(Math.random() <= 0.1F) {
            this.spawnAtLocation(Items.ROTTEN_FLESH);
        }
        if(Math.random() <= 0.25F && loot == 1) {
            this.spawnAtLocation(Items.COBBLESTONE);
        }
        if(Math.random() <= 0.2F && loot == 1) {
            this.spawnAtLocation(Items.STONE);
        }
        if(Math.random() <= 0.15F && loot == 1) {
            this.spawnAtLocation(Items.STONE);
        }
        if(Math.random() <= 0.30F && loot == 2) {
            this.spawnAtLocation(Items.IRON_INGOT);
        }
        if(Math.random() <= 0.1F && loot == 2) {
            this.spawnAtLocation(Items.IRON_NUGGET);
        }
        if(Math.random() <= 0.15F && loot == 2) {
            this.spawnAtLocation(Items.IRON_NUGGET);
        }
        if(Math.random() <= 0.25F && loot == 3) {
            this.spawnAtLocation(Items.GOLD_NUGGET);
        }
        if(Math.random() <= 0.08F && loot == 3) {
            this.spawnAtLocation(Items.GOLD_NUGGET);
        }
        if(Math.random() <= 0.12F && loot == 3) {
            this.spawnAtLocation(Items.GOLD_NUGGET);
        }
        if(Math.random() <= 0.15F && loot == 4) {
            this.spawnAtLocation(Items.DIAMOND);
        }
        if(Math.random() <= 0.02F && loot == 4) {
            this.spawnAtLocation(Items.DIAMOND_ORE);
        }
        if(Math.random() <= 0.05F && loot == 4) {
            this.spawnAtLocation(Items.DEEPSLATE_DIAMOND_ORE);
        }
        if(RottedConfig.SERVER.past_loot.get() == true) {
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.TORCH);
                if (Math.random() <= 0.1f) {
                    this.spawnAtLocation(Items.TORCH);
                }
            }
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.TORCH);
                if (Math.random() <= 0.3f) {
                    this.spawnAtLocation(Items.TORCH);
                }
                if (Math.random() <= 0.1f) {
                    this.spawnAtLocation(Items.LANTERN);
                }
            }
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.NAME_TAG);
                if (Math.random() <= 0.2f) {
                    this.spawnAtLocation(Items.LANTERN);
                }
                if (Math.random() <= 0.05f) {
                    this.spawnAtLocation(Items.NAME_TAG);
                }
            }

        }



    }
}
