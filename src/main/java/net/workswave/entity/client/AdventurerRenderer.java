package net.workswave.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.workswave.entity.custom.AdventurerEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nonnull;

public class AdventurerRenderer extends DynamicGeoEntityRenderer<AdventurerEntity> {
    private static final String CHESTPLATE = "Down";
    private static final String HELMET = "Head";
    private static final String OFF_HAND = "Arm2";

    AdventurerEntity golem;
    MultiBufferSource bufferIn;
    ResourceLocation text;
    public AdventurerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AdventurerModel());
        shadowRadius = 0.5f;
        addRenderLayer(new ItemArmorGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getArmorItemForBone (GeoBone bone, AdventurerEntity animatable){
                return switch (bone.getName()) {
                    case CHESTPLATE -> this.chestplateStack;
                    case HELMET -> this.helmetStack;
                    case OFF_HAND -> this.offhandStack;
                    default -> null;
                };
            }

            @Nonnull
            @Override
            protected EquipmentSlot getEquipmentSlotForBone (GeoBone bone, ItemStack stack, AdventurerEntity animatable){
                return switch (bone.getName()) {
                    case CHESTPLATE -> EquipmentSlot.CHEST;
                    case HELMET -> EquipmentSlot.HEAD;
                    case OFF_HAND -> EquipmentSlot.OFFHAND;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }
            @Nonnull
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, AdventurerEntity animatable, HumanoidModel<?> baseModel) {
                return switch (bone.getName()) {
                    case CHESTPLATE -> baseModel.body;
                    case HELMET -> baseModel.head;
                    case OFF_HAND -> baseModel.leftArm;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });
    }


    @Override
    public void preRender(PoseStack poseStack, AdventurerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.golem = animatable;
        this.bufferIn = bufferSource;
        this.text = this.getTextureLocation(animatable);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }



    @Override
    public void renderRecursively(PoseStack stack, AdventurerEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(stack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("item")) {
            stack.pushPose();
            RenderUtils.translateToPivotPoint(stack, bone);
            stack.mulPose(Axis.XP.rotationDegrees(-90f));
            stack.translate(0, 0, -0.1f);
            stack.scale(1f, 1f, 1f);
            ItemStack itemstack = animatable.getMainHandItem();
            if(!itemstack.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, stack, bufferIn, animatable.level(), 0);
            }
            stack.popPose();
            buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(text));
        }
    }

}
