package net.workswave.entity.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.registry.EntityRegistry;
import net.workswave.rotted.Rotted;

@Mod.EventBusSubscriber(modid = Rotted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(EntityRegistry.MARINE.get(), MarineRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FIREFIGHTER.get(), FirefighterRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DOCTOR.get(), DoctorRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FARMER.get(), FarmerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MINER.get(), MinerRenderer::new);




    }



}
