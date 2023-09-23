package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.MinerEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class MinerModel extends GeoModel<MinerEntity> {

    @Override
    public ResourceLocation getModelResource(MinerEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/miner.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinerEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/miner.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MinerEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/miner.animation.json");
    }
}
