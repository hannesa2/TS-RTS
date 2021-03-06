package com.projectreddog.tsrts.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.projectreddog.tsrts.client.model.TargetModel;
import com.projectreddog.tsrts.entities.TargetEntity;
import com.projectreddog.tsrts.reference.Reference;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TargetRenderer<T extends TargetEntity> extends EntityRenderer {

	private static ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/targetentity_yellow.png");
	private static ResourceLocation TEXTURE_YELLOW = new ResourceLocation(Reference.MODID, "textures/entity/targetentity_yellow.png");
	private static ResourceLocation TEXTURE_BLUE = new ResourceLocation(Reference.MODID, "textures/entity/targetentity_blue.png");
	private static ResourceLocation TEXTURE_GREEN = new ResourceLocation(Reference.MODID, "textures/entity/targetentity_green.png");
	private static ResourceLocation TEXTURE_RED = new ResourceLocation(Reference.MODID, "textures/entity/targetentity_red.png");

	public TargetModel model = new TargetModel();

	public TargetRenderer(EntityRendererManager manager) {
		super(manager);

	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		this.bindEntityTexture(entity);
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x, (float) y, (float) z);
		model.render((TargetEntity) entity, 1, 1, 1, 1, 1, 1);

		if (((TargetEntity) entity).newHurtTime > 0) {
			GlStateManager.depthFunc(514);
			GlStateManager.disableTexture();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color4f(1.0F, 0.0F, 0.0F, 0.5F);
			model.render((TargetEntity) entity, 1, 1, 1, 1, 1, 1);
			GlStateManager.enableTexture();
			GlStateManager.disableBlend();
			GlStateManager.depthFunc(515);
		}

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		if (entity.getTeam() != null) {
			if (entity.getTeam().getName().equals("red")) {
				return TEXTURE_RED;
			} else if (entity.getTeam().getName().equals("blue")) {
				return TEXTURE_BLUE;
			} else if (entity.getTeam().getName().equals("green")) {
				return TEXTURE_GREEN;
			} else if (entity.getTeam().getName().equals("yellow")) {
				return TEXTURE_YELLOW;
			} else {
				return TEXTURE;
			}
		} else {
			return TEXTURE;
		}
	}

}
