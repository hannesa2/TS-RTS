package com.projectreddog.tsrts.client.model;

import com.projectreddog.tsrts.entities.MinionEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;

public class MinionModel extends BipedModel<MinionEntity> {

	public MinionModel() {

		// FALSE is default steve
		// BODY
		super(0, 0.0F, 64, 64);
	}

	public MinionModel(float size) {
		// super(size, true);
// ARMOR?
		super(size, 0.0F, 64, 32);
	}
}
