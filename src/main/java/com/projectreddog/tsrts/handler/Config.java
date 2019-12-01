package com.projectreddog.tsrts.handler;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.projectreddog.tsrts.TSRTS;
import com.projectreddog.tsrts.utilities.ResourceValues;
import com.projectreddog.tsrts.utilities.UnitAttributes;
import com.projectreddog.tsrts.utilities.WeaponModifierAttributes;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

	public static final String CATEGORY_GENERAL = "general";

	public static final String CATEGORY_UNIT_COST = "unit_cost";

	public static final String CATEGORY_BUILDING_COST = "building_cost";

	public static final String CATEGORY_STARTUP_RESOURCES = "startup_resources";

	public static final String CATEGORY_UNIT_ATTRIBUTES = "unit_attributes";
	public static final String CATEGORY_WEAPON_MODIFIER_ATTRIBUTES = "weapon_modifier_attributes";

	public static final String CATEGORY_RESOURCE_GENERATION = "resource_generation";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	public static ForgeConfigSpec.EnumValue<Modes> CONFIG_GAME_MODE;
	// EXAMPLE: public static ForgeConfigSpec.IntValue CONFIG_INT_VALUE_VAR_NAME;
	// minions

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_COSTS_MINION_STRING;
	public static ResourceValues CONFIG_UNIT_COSTS_MINION;

	// archers

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_COSTS_ARCHER_STRING;
	public static ResourceValues CONFIG_UNIT_COSTS_ARCHER;

	// mounted unit

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_COSTS_LANCER_STRING;
	public static ResourceValues CONFIG_UNIT_COSTS_LANCER;

	// starting resrources

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_START_AMT_STRING;
	public static ResourceValues CONFIG_START_AMT;

	// generation amounts for town hall
	public static ForgeConfigSpec.ConfigValue<String> CONFIG_TOWN_HALL_GENERATE_STRING;
	public static ResourceValues CONFIG_TOWN_HALL_GENERATE;

	// building costs
	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_FARM_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_FARM;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_LUMBER_YARD_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_LUMBER_YARD;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_MINESITE_STONE_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_MINESITE_STONE;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_MINESITE_IRON_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_MINESITE_IRON;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_MINESITE_GOLD_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_MINESITE_GOLD;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_MINESITE_DIAMOND_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_MINESITE_DIAMOND;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_MINESITE_EMERALD_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_MINESITE_EMERALD;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_BARRACKS_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_BARRACKS;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_ARCHERY_RANGE_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_ARCHERY_RANGE;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_WALL_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_WALL;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_STABLES_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_STABLES;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_BUILDING_COSTS_WATCH_TOWER_STRING;
	public static ResourceValues CONFIG_BUILDING_COSTS_WATCH_TOWER;

	/// UNIt attributes
	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_MINION_ATTRIBUTES_STRING;
	public static UnitAttributes CONFIG_UNIT_ATTRIBUTES_MINION;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_ARCHER_ATTRIBUTES_STRING;
	public static UnitAttributes CONFIG_UNIT_ATTRIBUTES_ARCHER;

	public static ForgeConfigSpec.ConfigValue<String> CONFIG_UNIT_MOUNTED_ATTRIBUTES_STRING;
	public static UnitAttributes CONFIG_UNIT_ATTRIBUTES_MOUNTED;
/// weapon moidifiers
	public static ForgeConfigSpec.ConfigValue<String> CONFIG_LANCE_WEAPON_MODIFIER_ATTRIBUTES_STRING;
	public static WeaponModifierAttributes CONFIG_WEAPON_MODIFIER_ATTRIBUTES_LANCE;

	static {

		setupGeneralConfig();
		setupStartinResourcesConfig();
		setupResourceGeneration();
		setupUnitCostConfig();
		setupBuildingCostConfig();
		setupUnitAttributeConfig();
		setupWeaponAttributeConfig();

		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	private static void setupWeaponAttributeConfig() {
		COMMON_BUILDER.comment("Weapon ATTRIBUTES. ATTRIBUTES ARE ORDERED LIKE THIS ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER: . They are comma separated list of floats Example : 8,-2.9").push(CATEGORY_WEAPON_MODIFIER_ATTRIBUTES);
		CONFIG_LANCE_WEAPON_MODIFIER_ATTRIBUTES_STRING = COMMON_BUILDER.comment("Defines a comma separted list of values for each attribute modifier in order for the LANCE").define("lance_attribute_modifier", "8,-2.9");
		COMMON_BUILDER.pop();

	}

	private static void PostProcessConfigs() {
		CONFIG_UNIT_ATTRIBUTES_MINION = new UnitAttributes(StringToFloatArray(CONFIG_UNIT_MINION_ATTRIBUTES_STRING.get()));
		CONFIG_UNIT_ATTRIBUTES_ARCHER = new UnitAttributes(StringToFloatArray(CONFIG_UNIT_ARCHER_ATTRIBUTES_STRING.get()));
		CONFIG_UNIT_ATTRIBUTES_MOUNTED = new UnitAttributes(StringToFloatArray(CONFIG_UNIT_MOUNTED_ATTRIBUTES_STRING.get()));
		CONFIG_WEAPON_MODIFIER_ATTRIBUTES_LANCE = new WeaponModifierAttributes(StringToFloatArray(CONFIG_LANCE_WEAPON_MODIFIER_ATTRIBUTES_STRING.get()));
		CONFIG_START_AMT = new ResourceValues(StringToIntArray(CONFIG_START_AMT_STRING.get()));
		CONFIG_TOWN_HALL_GENERATE = new ResourceValues(StringToIntArray(CONFIG_TOWN_HALL_GENERATE_STRING.get()));
		CONFIG_UNIT_COSTS_MINION = new ResourceValues(StringToIntArray(CONFIG_UNIT_COSTS_MINION_STRING.get()));
		CONFIG_UNIT_COSTS_ARCHER = new ResourceValues(StringToIntArray(CONFIG_UNIT_COSTS_ARCHER_STRING.get()));
		CONFIG_UNIT_COSTS_LANCER = new ResourceValues(StringToIntArray(CONFIG_UNIT_COSTS_LANCER_STRING.get()));
		CONFIG_BUILDING_COSTS_FARM = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_FARM_STRING.get()));
		CONFIG_BUILDING_COSTS_LUMBER_YARD = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_LUMBER_YARD_STRING.get()));
		CONFIG_BUILDING_COSTS_MINESITE_STONE = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_MINESITE_STONE_STRING.get()));
		CONFIG_BUILDING_COSTS_MINESITE_IRON = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_MINESITE_IRON_STRING.get()));
		CONFIG_BUILDING_COSTS_MINESITE_GOLD = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_MINESITE_GOLD_STRING.get()));
		CONFIG_BUILDING_COSTS_MINESITE_DIAMOND = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_MINESITE_DIAMOND_STRING.get()));
		CONFIG_BUILDING_COSTS_MINESITE_EMERALD = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_MINESITE_EMERALD_STRING.get()));
		CONFIG_BUILDING_COSTS_BARRACKS = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_BARRACKS_STRING.get()));
		CONFIG_BUILDING_COSTS_ARCHERY_RANGE = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_ARCHERY_RANGE_STRING.get()));
		CONFIG_BUILDING_COSTS_WALL = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_WALL_STRING.get()));
		CONFIG_BUILDING_COSTS_STABLES = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_STABLES_STRING.get()));
		CONFIG_BUILDING_COSTS_WATCH_TOWER = new ResourceValues(StringToIntArray(CONFIG_BUILDING_COSTS_WATCH_TOWER_STRING.get()));
	}

	private static void setupUnitAttributeConfig() {
		COMMON_BUILDER.comment("UNIT ATTRIBUTES. ATTRIBUTES ARE ORDERED LIKE THIS : MAX_HEALTH,KNOCK_BACK_RESISTANCE, MOVEMENT_SPEED,ARMOR,ARMOR_TOUGHNESS,ATTACK_KNOCKBACK,ATTACKD_DAMAGE,FOLLOW_RANGE. They are comma separated list of floats Example : 20.0,0.0,0.25,2.0,0.0,0.0,3.0,35.0").push(CATEGORY_UNIT_ATTRIBUTES);

		CONFIG_UNIT_MINION_ATTRIBUTES_STRING = COMMON_BUILDER.comment("Defines a comma separted list of values for each attribute in order for the MINION. Atttributes are ").define("unit_minion_attributes", "15.0,0.0,0.35,2.0,0.0,0.0,3.0,35.0");
		CONFIG_UNIT_ARCHER_ATTRIBUTES_STRING = COMMON_BUILDER.comment("Defines a comma separted list of values for each attribute in order for the ARCHER. Atttributes are ").define("unit_archer_attributes", "10.0,0.0,0.30,2.0,0.0,0.0,3.0,35.0");
		CONFIG_UNIT_MOUNTED_ATTRIBUTES_STRING = COMMON_BUILDER.comment("Defines a comma separted list of values for each attribute in order for the MOUNTED. Atttributes are ").define("unit_mounted_attributes", "10.0,0.0,0.40,2.0,0.0,0.0,3.0,35.0");
		COMMON_BUILDER.pop();

	}

	private static void setupStartinResourcesConfig() {
		COMMON_BUILDER.comment("Starting resources").push(CATEGORY_STARTUP_RESOURCES);
		CONFIG_START_AMT_STRING = COMMON_BUILDER.comment("Defines the starting amount for each resource").define("StartingAmount", "100,100,75,50,25,0,0");

		COMMON_BUILDER.pop();
	}

	private static void setupResourceGeneration() {
		COMMON_BUILDER.comment("resource generation").push(CATEGORY_RESOURCE_GENERATION);

		CONFIG_TOWN_HALL_GENERATE_STRING = COMMON_BUILDER.comment("Defines the amount of food the town hall generate per unit of time").define("townHallGenerates", "5,5,5,5,1,1,0");

		COMMON_BUILDER.pop();

	}

	private static void setupBuildingCostConfig() {
		COMMON_BUILDER.comment("Building Costs").push(CATEGORY_BUILDING_COST);

		CONFIG_BUILDING_COSTS_FARM_STRING = COMMON_BUILDER.comment("Defines the cost For the farm").define("farmBulidingCosts", "10,15,5,0,0,0,0");
		CONFIG_BUILDING_COSTS_LUMBER_YARD_STRING = COMMON_BUILDER.comment("Defines the cost For the lumber Yard").define("lumberYardBulidingCosts", "10,10,15,5,0,0,0");
		CONFIG_BUILDING_COSTS_MINESITE_STONE_STRING = COMMON_BUILDER.comment("Defines the cost For the minesite Stone").define("minesiteStoneBulidingCosts", "2,30,15,15,0,0,0");
		CONFIG_BUILDING_COSTS_MINESITE_IRON_STRING = COMMON_BUILDER.comment("Defines the cost For the minesite Iron").define("minesiteIronBulidingCosts", "30,45,9,8,0,0,0");
		CONFIG_BUILDING_COSTS_MINESITE_GOLD_STRING = COMMON_BUILDER.comment("Defines the cost For the minesite Gold").define("minesiteGoldBulidingCosts", "36,38,9,20,0,0,0");
		CONFIG_BUILDING_COSTS_MINESITE_DIAMOND_STRING = COMMON_BUILDER.comment("Defines the cost For the minesite Diamond").define("minesiteDiamondBulidingCosts", "42,45,14,23,0,0,0");
		CONFIG_BUILDING_COSTS_MINESITE_EMERALD_STRING = COMMON_BUILDER.comment("Defines the cost For the minesite Emerald").define("minesiteEmeraldBulidingCosts", "600,5,2,3,2,0,0");
		CONFIG_BUILDING_COSTS_BARRACKS_STRING = COMMON_BUILDER.comment("Defines the cost For the barracks").define("barracksBulidingCosts", "44,70,38,29,11,0,0");
		CONFIG_BUILDING_COSTS_ARCHERY_RANGE_STRING = COMMON_BUILDER.comment("Defines the cost For the Archery Range").define("archeryRangeBulidingCosts", "44,100,46,36,17,0,0");
		CONFIG_BUILDING_COSTS_WALL_STRING = COMMON_BUILDER.comment("Defines the cost For the Wall").define("wallBulidingCosts", "10,5,20,0,3,0,0");
		CONFIG_BUILDING_COSTS_STABLES_STRING = COMMON_BUILDER.comment("Defines the cost For the Stables").define("stablesBulidingCosts", "66,120,40,30,35,0,0");
		CONFIG_BUILDING_COSTS_WATCH_TOWER_STRING = COMMON_BUILDER.comment("Defines the cost For the Watch Tower").define("watchTowerBulidingCosts", "20,50,25,4,20,0,0");

		COMMON_BUILDER.pop();
	}

	private static void setupUnitCostConfig() {
		COMMON_BUILDER.comment("Unit Costs").push(CATEGORY_UNIT_COST);
		CONFIG_UNIT_COSTS_MINION_STRING = COMMON_BUILDER.comment("Defines the cost For the minion").define("unitCostsMinion", "6,4,4,4,0,0,0");
		CONFIG_UNIT_COSTS_ARCHER_STRING = COMMON_BUILDER.comment("Defines the cost For the archer").define("unitCostsArcher", "5,6,2,4,4,0,0");
		CONFIG_UNIT_COSTS_LANCER_STRING = COMMON_BUILDER.comment("Defines the cost For the lancer").define("unitCostsLancer", "10,6,0,1,6,0,0");

		COMMON_BUILDER.pop();
	}

	private static void setupGeneralConfig() {
		COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
		CONFIG_GAME_MODE = COMMON_BUILDER.comment("Sets the game mode, 0 = RUN , 1 = WORLDBUILDER. World builder will not spawn units from structures").defineEnum("gameMode", Modes.RUN);
		COMMON_BUILDER.pop();
	}

	public enum Modes {
		RUN, WORLDBUILDER
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		configData.load();
		spec.setConfig(configData);
		PostProcessConfigs();

	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		PostProcessConfigs();

	}

	@SubscribeEvent
	public static void onReload(final ModConfig.ConfigReloading configEvent) {
		PostProcessConfigs();

	}

	public static float[] StringToFloatArray(String input) {
		float[] tmp;
		try {
			String[] stringArray = input.split(",");
			tmp = new float[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				tmp[i] = Float.parseFloat(stringArray[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TSRTS.LOGGER.error("INVALID CONFIG PLEASE FIX- MOD WILL MISSBEHAVE!!");
			tmp = new float[0];
		}

		return tmp;
	}

	public static int[] StringToIntArray(String input) {
		int[] tmp;
		try {
			String[] stringArray = input.split(",");
			tmp = new int[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				tmp[i] = Integer.parseInt(stringArray[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TSRTS.LOGGER.error("INVALID CONFIG PLEASE FIX- MOD WILL MISSBEHAVE!!");
			tmp = new int[0];
		}

		return tmp;
	}

	public enum UnitStats {
		MAX_HEALTH, KNOCK_BACK_RESISTANCE, MOVEMENT_SPEED, ARMOR, ARMOR_TOUGHNESS, ATTACK_KNOCKBACK, ATTACKD_DAMAGE, FOLLOW_RANGE
	}

	public enum WeaponModifierStats {
		ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER
	}
}
