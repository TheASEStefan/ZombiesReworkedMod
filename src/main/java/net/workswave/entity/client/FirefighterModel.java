package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.FirefighterEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class FirefighterModel extends GeoModel<FirefighterEntity> {


    @Override
    public ResourceLocation getModelResource(FirefighterEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/firefighter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FirefighterEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/firefighter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FirefighterEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/firefighter.animation.json");
    }
}


