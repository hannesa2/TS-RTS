package com.projectreddog.tsrts.entities;

import com.projectreddog.tsrts.entities.ai.MoveToOwnerSpecifiedLocation;
import com.projectreddog.tsrts.entities.ai.NearestAttackableVisionNotRequiredTargetGoal;
import com.projectreddog.tsrts.entities.ai.RetreatToOwnerSpecifiedLocation;
import com.projectreddog.tsrts.handler.Config;
import com.projectreddog.tsrts.reference.Reference;
import com.projectreddog.tsrts.reference.Reference.UNIT_TYPES;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PikemanEntity extends UnitEntity {

	public static final EntityPredicate VISION_NOT_REQUIRED = new EntityPredicate().setLineOfSiteRequired();

	public PikemanEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
		// this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), scoreplayerteam);
		// Minecraft.getInstance().player.getTeam()

	}

	public Reference.UNIT_TYPES getUnitType() {
		return UNIT_TYPES.PIKEMAN;
	}

	protected void registerGoals() {
		// this.goalSelector.addGoal(4, new ZombieEntity.AttackTurtleEggGoal(this, 1.0D, 3));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1D, true));
		// this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new MoveToOwnerSpecifiedLocation(this, 1.1D, 32));
		this.goalSelector.addGoal(1, new RetreatToOwnerSpecifiedLocation(this, 1.1D, 32));

		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));

		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MountedEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, UnitEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableVisionNotRequiredTargetGoal<>(this, TargetEntity.class, false));

	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.93F;
	}

	protected void registerAttributes() {
		super.registerAttributes();
		// getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getARMOR());
		this.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getARMOR_TOUGHNESS());
		this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getATTACK_KNOCKBACK());
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getATTACK_DAMAGE());
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getFOLLOW_RANGE());
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getKNOCK_BACK_RESISTANCE());
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getMAX_HEALTH());
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Config.CONFIG_UNIT_ATTRIBUTES_PIKEMAN.getMOVEMENT_SPEED());

	}
}
