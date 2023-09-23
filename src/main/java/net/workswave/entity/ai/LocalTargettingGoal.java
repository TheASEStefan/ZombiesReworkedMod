package net.workswave.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.workswave.entity.categories.RottedZombie;

import java.util.List;

public class LocalTargettingGoal extends Goal {
    private final RottedZombie mob;
    public LocalTargettingGoal(RottedZombie mob){
        this.mob = mob;
    }
    @Override
    public boolean canUse() {
        return (mob.getTarget() != null || mob.getSearchPos() != null) && this.mob.getRandom().nextInt(0,10) == 7;
    }

    @Override
    public boolean canContinueToUse() {
        return (mob.getTarget() != null || mob.getSearchPos() != null);
    }

    @Override
    public void start() {
        super.start();
        this.Targeting(mob);
    }

    public void Targeting(Entity entity){
        double range;
        if (this.mob.getAttributeBaseValue(Attributes.FOLLOW_RANGE) < 32){
            range = this.mob.getAttributeBaseValue(Attributes.FOLLOW_RANGE);
        }else{
            range = 32;
        }

        AABB boundingBox = entity.getBoundingBox().inflate(range);
        List<Entity> entities = entity.level().getEntities(entity, boundingBox , EntitySelector.NO_CREATIVE_OR_SPECTATOR);

        for (Entity entity1 : entities) {
            if(entity1 instanceof RottedZombie livingEntity) {
                if (livingEntity.getTarget() == null && this.mob.getTarget() != null && this.mob.getTarget().isAlive() && !this.mob.getTarget().isInvulnerable()){
                    livingEntity.setTarget(mob.getTarget());
                }else if (livingEntity.getSearchPos() == null && this.mob.getSearchPos() != null){
                    livingEntity.setSearchPos(this.mob.getSearchPos());
                }
            }
        }
    }
}