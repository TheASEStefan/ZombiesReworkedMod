package net.workswave.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.workswave.entity.custom.ShielderEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ShielderRenderer extends GeoEntityRenderer<ShielderEntity> {
    public ShielderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ShielderModel());
        shadowRadius = 0.5f;
    }


    @Override
    public void render(ShielderEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

}
