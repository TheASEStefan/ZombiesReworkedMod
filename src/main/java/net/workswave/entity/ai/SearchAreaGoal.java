package net.workswave.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.workswave.entity.categories.RottedZombie;

import java.util.EnumSet;

public class SearchAreaGoal extends Goal {
    public final RottedZombie rottedzombie;
    public final double speed;
    public  int tryTicks;

    public SearchAreaGoal(RottedZombie rottedzombie , double speed){
        this.rottedzombie = rottedzombie;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    protected void moveMobToBlock() {
        this.rottedzombie.getNavigation().moveTo((double)((float)this.rottedzombie.getSearchPos().getX()) + 0.5D, (double)(this.rottedzombie.getSearchPos().getY() + 1), (double)((float)this.rottedzombie.getSearchPos().getZ()) + 0.5D, 1);
    }
    @Override
    public boolean canUse() {
        return this.rottedzombie.getSearchPos() != null && rottedzombie.getTarget() == null;
    }

    @Override
    public void start() {
        this.moveMobToBlock();
        this.tryTicks = 0;
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        return rottedzombie.getTarget() == null;
    }


    @Override
    public void tick() {
        super.tick();
        ++this.tryTicks;
        if (this.rottedzombie.getSearchPos() != null && shouldRecalculatePath()){
            this.rottedzombie.getNavigation().moveTo(this.rottedzombie.getSearchPos().getX(),this.rottedzombie.getSearchPos().getY(),this.rottedzombie.getSearchPos().getZ(),1);
        }
        if (this.rottedzombie.getSearchPos() != null && this.rottedzombie.getSearchPos().closerToCenterThan(this.rottedzombie.position(),9.0)){
            rottedzombie.setSearchPos(null);
        }
    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 40 == 0;
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}