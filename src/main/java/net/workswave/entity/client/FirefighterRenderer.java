package net.workswave.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.workswave.entity.custom.FirefighterEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FirefighterRenderer extends GeoEntityRenderer<FirefighterEntity> {

    public FirefighterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FirefighterModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(FirefighterEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }


}
