package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.ShielderEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class ShielderModel extends GeoModel<ShielderEntity> {

    @Override
    public ResourceLocation getModelResource(ShielderEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/shielder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShielderEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/shielder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShielderEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/shielder.animation.json");
    }
}
