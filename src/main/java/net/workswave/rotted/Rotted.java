package net.workswave.rotted;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.workswave.biome.BiomeModification;
import net.workswave.config.RottedConfig;
import net.workswave.event.HandlerEvents;
import net.workswave.registry.*;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Rotted.MODID)
public class Rotted {
    public static final String MODID = "rotted";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Rotted() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RottedConfig.SERVER_SPEC ,"rottedconfig.toml");
        RottedConfig.loadConfig(RottedConfig.SERVER_SPEC,
                FMLPaths.CONFIGDIR.get().resolve("rottedconfig.toml").toString());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);


        GeckoLib.initialize();


        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(HandlerEvents::SpawnPlacement);

        EntityRegistry.register(modEventBus);

        ItemRegistry.register(modEventBus);

        SoundRegistry.register(modEventBus);

        RottedMobEffects.register(modEventBus);

        CreativeTabRegistry.register(modEventBus);

        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers =
                DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Rotted.MODID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("rotted_spawns", BiomeModification::makeCodec);






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