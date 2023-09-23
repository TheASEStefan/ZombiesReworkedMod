package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.MarineEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class MarineModel extends GeoModel<MarineEntity> {

    @Override
    public ResourceLocation getModelResource(MarineEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/marine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarineEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/marine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarineEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/marine.animation.json");
    }
}


