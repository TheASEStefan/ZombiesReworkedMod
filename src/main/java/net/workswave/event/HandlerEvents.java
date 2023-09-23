package net.workswave.event;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.entity.categories.RottedZombie;
import net.workswave.registry.EntityRegistry;
import net.workswave.rotted.Rotted;

@Mod.EventBusSubscriber(modid = Rotted.MODID)
public class HandlerEvents {

    @SubscribeEvent
    public static void SpawnPlacement(SpawnPlacementRegisterEvent event){
        event.register(EntityRegistry.FIREFIGHTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,RottedZombie::checkMonsterRottedZombieRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityRegistry.FARMER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,RottedZombie::checkMonsterRottedZombieRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityRegistry.DOCTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,RottedZombie::checkMonsterRottedZombieRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityRegistry.MINER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,RottedZombie::checkMonsterRottedZombieRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityRegistry.MARINE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RottedZombie::checkMonsterRottedZombieRules, SpawnPlacementRegisterEvent.Operation.AND);

    }
}
