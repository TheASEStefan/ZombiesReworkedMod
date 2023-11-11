package net.workswave.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.workswave.entity.custom.MarineEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarineRenderer extends GeoEntityRenderer<MarineEntity> {
     public MarineRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MarineModel());
        shadowRadius = 0.5f;
    }


    @Override
    public void render(MarineEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }



}
