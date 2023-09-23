package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.FarmerEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class FarmerModel extends GeoModel<FarmerEntity> {

    @Override
    public ResourceLocation getModelResource(FarmerEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/farmer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FarmerEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/farmer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FarmerEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/farmer.animation.json");
    }
}
