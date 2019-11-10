package com.projectreddog.tsrts.items.builderitems;

import com.projectreddog.tsrts.init.ModItemGroups;
import com.projectreddog.tsrts.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;

public class FarmBuilderItem extends BuilderItem {

	private ResourceLocation templateName100 = new ResourceLocation(Reference.MODID + ":" + "farm_red_2");
	private ResourceLocation templateName50 = new ResourceLocation(Reference.MODID + ":" + "farm_red_2");
	private ResourceLocation templateName0 = new ResourceLocation(Reference.MODID + ":" + "farm_red_2");

	public FarmBuilderItem() {
		super(new Item.Properties().group(ModItemGroups.weaponsItemGroup));
		setRegistryName(Reference.REIGSTRY_NAME_FARM_BUILDER_ITEM);
	}

	public ResourceLocation getTemplateName100() {
		return this.templateName100;
	}

	public ResourceLocation getTemplateName50() {
		return this.templateName50;
	}

	public ResourceLocation getTemplateName0() {
		return this.templateName0;
	}

	public Vec3i getSize() {

		return new Vec3i(15, 4, 15);
	}

	@Override
	public boolean CanPlaceOn(Block block) {
		return block == Blocks.GRASS_BLOCK;
	}

}