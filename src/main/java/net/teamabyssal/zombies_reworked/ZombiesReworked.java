package net.teamabyssal.zombies_reworked;

import com.mojang.logging.LogUtils;
import net.asestefan.api.ClockworkLib;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.teamabyssal.config.ZombiesReworkedConfig;
import net.teamabyssal.zombies_reworked.sound.ModdedSounds;

import org.slf4j.Logger;

@Mod(ZombiesReworked.MODID)
public class ZombiesReworked {
    public static final String MODID = "zombies_reworked";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ZombiesReworked()
    {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ZombiesReworkedConfig.DATAGEN_SPEC ,"ZombiesReworked-data.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ZombiesReworkedConfig.SERVER_SPEC ,"ZombiesReworked-config.toml");
        ZombiesReworkedConfig.loadConfig(ZombiesReworkedConfig.SERVER_SPEC,
                FMLPaths.CONFIGDIR.get().resolve("ZombiesReworked-config.toml").toString());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        ModLoadingContext context = ModLoadingContext.get();

        MinecraftForge.EVENT_BUS.register(this);
        ModdedSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    


    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {



        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {


    }
}