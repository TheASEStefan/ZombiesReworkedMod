package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.AdventurerEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class AdventurerModel extends GeoModel<AdventurerEntity> {

    @Override
    public ResourceLocation getModelResource(AdventurerEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/adventurer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AdventurerEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/adventurer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AdventurerEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/adventurer.animation.json");
    }
}
