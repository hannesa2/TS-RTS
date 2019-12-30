package com.projectreddog.tsrts.tileentity;

import com.projectreddog.tsrts.TSRTS;
import com.projectreddog.tsrts.handler.Config;
import com.projectreddog.tsrts.init.ModBlocks;
import com.projectreddog.tsrts.reference.Reference;
import com.projectreddog.tsrts.reference.Reference.STRUCTURE_TYPE;
import com.projectreddog.tsrts.tileentity.interfaces.ITEGuiButtonHandler;
import com.projectreddog.tsrts.utilities.ResourceValues;
import com.projectreddog.tsrts.utilities.TeamEnum;
import com.projectreddog.tsrts.utilities.Utilities;
import com.projectreddog.tsrts.utilities.data.MapStructureData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class BarracksTileEntity extends OwnedCooldownTileEntity implements INamedContainerProvider, ITEGuiButtonHandler {

	public BarracksTileEntity() {
		super(ModBlocks.BARRACKS_TILE_ENTITY_TYPE);
	}

	@Override
	public void ActionAfterCooldown() {

		super.ActionAfterCooldown();

		if (getOwner() != null && getTeam() != null) {
			if (TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().size() > 0) {
				Utilities.SpawnUnitForTeam(TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0), this.getOwner(), this.getWorld(), this.getPos(), this.getTeam(), this.getRallyPoint());

				if (TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].isInfinateBarracksQueue()) {

					ResourceValues rv = Utilities.GetResourceValuesforUnitID(TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0));

					if (Utilities.hasNeededResourcesForResourceValues(getTeam().getName(), rv)) {
						Utilities.spendResourcesForResourceValues(getTeam().getName(), rv);
//check if we have the building reuqired for "SOME" units
						if (((TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0) == Reference.UNIT_ID_ADVANCED_KNIGHT || TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0) == Reference.UNIT_ID_KNIGHT) && TSRTS.teamInfoArray[TeamEnum.getIDFromName(getTeam().getName())].getArmory() > 0) || TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0) == Reference.UNIT_ID_MINION || TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0) == Reference.UNIT_ID_PIKEMAN) {
							TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].AddToProperQueue(TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].getBarracks().get(0));
						}
					}
				}
				TSRTS.TeamQueues[TeamEnum.getIDFromName(getTeam().getName())].RemoveFirstFromBarracksQueue();
			}
			// }
//
//			MinionEntity me = new MinionEntity(null, world);
//
//			world.addEntity(me);
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return new StringTextComponent(getType().getRegistryName().getPath());
	}

	@Override
	public void HandleGuiButton(int buttonId, PlayerEntity player) {
		// TSRTS.LOGGER.info("button ID:" + buttonId);
		super.HandleGuiButton(buttonId, player);

		if (buttonId == Reference.GUI_BUTTON_DEBUG_TESTERYELLOW) {
			this.setOwner("testeryellow");
		} else if (buttonId == Reference.GUI_BUTTON_DEBUG_TESTERBLUE) {
			this.setOwner("testerblue");
		} else if (buttonId == Reference.GUI_BUTTON_DEBUG_TESTERGREEN) {
			this.setOwner("testergreen");
		} else if (buttonId == Reference.GUI_BUTTON_DEBUG_TESTERRED) {
			this.setOwner("testerred");
		}

	}

	@Override
	public STRUCTURE_TYPE getStructureType() {
		return STRUCTURE_TYPE.BARRACKS;
	}

	public void IncreaseCount() {

		TSRTS.teamInfoArray[TeamEnum.getIDFromName(getTeam().getName())].setBaracks(TSRTS.teamInfoArray[TeamEnum.getIDFromName(getTeam().getName())].getBaracks() + 1);
		TSRTS.Structures.put(pos, new MapStructureData(pos, getStructureType(), this.getTeam().getName()));

	}

	public void DecreaseCount() {

		TSRTS.teamInfoArray[TeamEnum.getIDFromName(getTeam().getName())].setBaracks(TSRTS.teamInfoArray[TeamEnum.getIDFromName(getTeam().getName())].getBaracks() - 1);
		TSRTS.Structures.remove(pos);
	}

	@Override
	public void StructureLost() {
		super.StructureLost();
		Utilities.SendMessageToTeam(this.getWorld(), this.getTeam().getName(), "tsrts.destroy.barracks");

	}

	@Override
	public float getDamagedHealthThreashold() {
		return .50f * Config.CONFIG_STRCTURE_TOTAL_HEALTH_BARRACKS.get();
	}
}
