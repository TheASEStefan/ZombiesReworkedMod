package net.workswave.extra;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.entity.ai.FollowOthersGoal;
import net.workswave.entity.ai.SearchAreaGoal;
import net.workswave.entity.categories.RottedZombie;
import net.workswave.rotted.Rotted;

@Mod.EventBusSubscriber(modid = Rotted.MODID)
public class AiRevamp {
    @SubscribeEvent()
    public static void addSpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager abstractVillager = (Villager) event.getEntity();abstractVillager.goalSelector.addGoal(2, new AvoidEntityGoal(abstractVillager, RottedZombie.class, 16.0F, 0.8F, 0.85F));
        }

        if (event.getEntity() instanceof WanderingTrader) {
            WanderingTrader wanderingTraderEntity = (WanderingTrader) event.getEntity();
            wanderingTraderEntity.goalSelector.addGoal(1, new AvoidEntityGoal(wanderingTraderEntity, RottedZombie.class, 16.0F, 0.8F, 0.85F));
        }

        if (event.getEntity() instanceof Zombie || event.getEntity() instanceof Husk || event.getEntity() instanceof Drowned || event.getEntity() instanceof ZombieVillager) {
            Zombie zombie = (Zombie) event.getEntity();
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(RottedZombie.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Zombie.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Husk.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(Drowned.class));
            zombie.targetSelector.addGoal(1, (new HurtByTargetGoal(zombie)).setAlertOthers(ZombieVillager.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.7, RottedZombie.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.7, Zombie.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.7, ZombieVillager.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.7, Drowned.class));
            zombie.goalSelector.addGoal(10, new FollowOthersGoal(zombie, 0.7, Husk.class));

        }
    }
}
