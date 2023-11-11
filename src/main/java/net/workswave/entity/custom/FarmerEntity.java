package net.workswave.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
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
import java.util.function.Predicate;

public class FarmerEntity extends RottedZombie implements GeoEntity {
    public FarmerEntity farmerEntity;



    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (difficulty) -> {
        return difficulty == Difficulty.HARD;
    };
    private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
    private boolean canBreakDoors;


    private int loot = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public FarmerEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
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
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 20D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, RottedConfig.SERVER.farmer_health.get())
                .add(Attributes.ATTACK_DAMAGE, RottedConfig.SERVER.farmer_damage.get())
                .add(Attributes.ARMOR, 4D);

    }
    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    public void setCanBreakDoors(boolean pCanBreakDoors) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != pCanBreakDoors) {
                this.canBreakDoors = pCanBreakDoors;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(pCanBreakDoors);
                if (pCanBreakDoors) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }

    }

    protected boolean supportsBreakDoorGoal() {
        return true;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);

            int i = pRandom.nextInt(5);
            this.loot = 0;
            if (i == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_HOE));
                this.loot = 1;

            }
            else if (i == 1) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_HOE));
                this.loot = 2;

            }
            else if (i == 2) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_HOE));
                this.loot = 3;

            }
            else if (i == 3) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_HOE));
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


    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("CanBreakDoors", this.canBreakDoors());

    }


    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        this.setCanBreakDoors(pCompound.getBoolean("CanBreakDoors"));

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
                    if(!event.isMoving()){
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
        if(Math.random() <= 0.2F && loot == 1) {
            this.spawnAtLocation(Items.OAK_LOG);
        }
        if(Math.random() <= 0.05F && loot == 1) {
            this.spawnAtLocation(Items.OAK_LOG);
        }
        if(Math.random() <= 0.1F && loot == 1) {
            this.spawnAtLocation(Items.STICK);
        }
        if(Math.random() <= 0.2F && loot == 2) {
            this.spawnAtLocation(Items.COBBLESTONE);
        }
        if(Math.random() <= 0.05F && loot == 2) {
            this.spawnAtLocation(Items.COBBLESTONE);
        }
        if(Math.random() <= 0.1F && loot == 2) {
            this.spawnAtLocation(Items.COBBLESTONE);
        }
        if(Math.random() <= 0.2F && loot == 3) {
            this.spawnAtLocation(Items.IRON_NUGGET);
        }
        if(Math.random() <= 0.05F && loot == 3) {
            this.spawnAtLocation(Items.IRON_NUGGET);
        }
        if(Math.random() <= 0.1F && loot == 3) {
            this.spawnAtLocation(Items.IRON_INGOT);
        }
        if(Math.random() <= 0.2F && loot == 4) {
            this.spawnAtLocation(Items.GOLD_NUGGET);
        }
        if(Math.random() <= 0.05F && loot == 4) {
            this.spawnAtLocation(Items.GOLD_NUGGET);
        }
        if(Math.random() <= 0.1F && loot == 4) {
            this.spawnAtLocation(Items.GOLD_INGOT);
        }
        if(RottedConfig.SERVER.past_loot.get() == true) {
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.POISONOUS_POTATO);
                if (Math.random() <= 0.1f) {
                    this.spawnAtLocation(Items.POISONOUS_POTATO);
                }
            }
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.WHEAT);
                if (Math.random() <= 0.3f) {
                    this.spawnAtLocation(Items.WHEAT);
                }
                if (Math.random() <= 0.1f) {
                    this.spawnAtLocation(Items.WHEAT);
                }
            }
            if(Math.random() <= RottedConfig.SERVER.past_loot_chance.get()) {
                this.spawnAtLocation(Items.CARROT);
                if (Math.random() <= 0.2f) {
                    this.spawnAtLocation(Items.CARROT);
                }
                if (Math.random() <= 0.05f) {
                    this.spawnAtLocation(Items.CARROT);
                }
            }

        }



    }
}
