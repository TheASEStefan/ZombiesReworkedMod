package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.DoctorEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.model.GeoModel;

public class DoctorModel extends GeoModel<DoctorEntity> {

    @Override
    public ResourceLocation getModelResource(DoctorEntity object) {
        return new ResourceLocation(Rotted.MODID, "geo/doctor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DoctorEntity object) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/doctor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DoctorEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/doctor.animation.json");
    }
}
