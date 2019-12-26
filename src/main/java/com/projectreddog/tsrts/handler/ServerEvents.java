package com.projectreddog.tsrts.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.projectreddog.tsrts.TSRTS;
import com.projectreddog.tsrts.init.ModNetwork;
import com.projectreddog.tsrts.init.ModResearch;
import com.projectreddog.tsrts.network.ResearchUnlockedPacketToClient;
import com.projectreddog.tsrts.network.UnitQueueChangedPacketToClient;
import com.projectreddog.tsrts.utilities.TeamEnum;
import com.projectreddog.tsrts.utilities.TeamInfo;
import com.projectreddog.tsrts.utilities.TeamInfo.Resources;
import com.projectreddog.tsrts.utilities.Utilities;

import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {

	private static int coolDownAmountRest = 20 * 10;
	private static int coolDownAmountRemaining = coolDownAmountRest;
	private static boolean writeHeader = true;

	@SubscribeEvent
	public static void onServerTickEvent(final ServerTickEvent event) {

		if (event.phase == Phase.END) {

			coolDownAmountRemaining--;

			if (coolDownAmountRemaining <= 0) {

				coolDownAmountRemaining = coolDownAmountRest;

				for (int i = 0; i < TSRTS.teamInfoArray.length; i++) {

					int foodDelta = 0;
					int woodDelta = 0;
					int stoneDelta = 0;
					int ironDelta = 0;
					int goldDelta = 0;
					int diamondDelta = 0;
					int emeraldDelta = 0;

					foodDelta = foodDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getFOOD());
					woodDelta = woodDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getWOOD());
					stoneDelta = stoneDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getSTONE());
					ironDelta = ironDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getIRON());
					goldDelta = goldDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getGOLD());
					diamondDelta = diamondDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getDIAMOND());
					emeraldDelta = emeraldDelta + (TSRTS.teamInfoArray[i].getTownHalls() * Config.CONFIG_TOWN_HALL_GENERATE.getEMERALD());

					foodDelta = foodDelta + (TSRTS.teamInfoArray[i].getFarms() * Config.CONFIG_RATE_GENRATE_FOOD.get());
					woodDelta = woodDelta + (TSRTS.teamInfoArray[i].getLumberYard() * Config.CONFIG_RATE_GENRATE_WOOD.get());
					stoneDelta = stoneDelta + (TSRTS.teamInfoArray[i].getMineSiteStone() * Config.CONFIG_RATE_GENRATE_STONE.get());
					ironDelta = ironDelta + (TSRTS.teamInfoArray[i].getMineSiteIron() * Config.CONFIG_RATE_GENRATE_IRON.get());
					goldDelta = goldDelta + (TSRTS.teamInfoArray[i].getMineSiteGold() * Config.CONFIG_RATE_GENRATE_GOLD.get());
					diamondDelta = diamondDelta + (TSRTS.teamInfoArray[i].getMineSiteDiamond() * Config.CONFIG_RATE_GENRATE_DIAMOND.get());
					emeraldDelta = emeraldDelta + (TSRTS.teamInfoArray[i].getMineSiteEmerald() * Config.CONFIG_RATE_GENRATE_EMERALD.get());

					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.FOOD, foodDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.WOOD, woodDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.STONE, stoneDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.IRON, ironDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.GOLD, goldDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.DIAMOND, diamondDelta);
					Utilities.AddResourcesToTeam(TeamEnum.values()[i].getName(), Resources.EMERALD, emeraldDelta);

					// REsearch
					if (TSRTS.teamInfoArray[i].getCurrenResearchWorkRemaining() > 0) {
						TSRTS.teamInfoArray[i].setCurrenResearchWorkRemaining(TSRTS.teamInfoArray[i].getCurrenResearchWorkRemaining() - (TSRTS.teamInfoArray[i].getResearchCenter()));
					}
					if (TSRTS.teamInfoArray[i].getCurrenResearchWorkRemaining() <= 0) {
						if (TSRTS.teamInfoArray[i].getCurrenResearchKey() != "") {
							ModResearch.getResearch(TSRTS.teamInfoArray[i].getCurrenResearchKey()).setUnlocked(true, i);
							ModNetwork.SendToALLPlayers(new ResearchUnlockedPacketToClient(TSRTS.teamInfoArray[i].getCurrenResearchKey(), TeamEnum.values()[i].getName()));

							TSRTS.teamInfoArray[i].setCurrenResearchKey("");
							TSRTS.teamInfoArray[i].setCurrenResearchWorkRemaining(0);
							TSRTS.teamInfoArray[i].setFullResearchWorkRemaining(0);

						}
					}

					// SEND the changes to the clients !
					Utilities.SendTeamToClient(TeamEnum.values()[i].getName());

					int totFood = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.FOOD);
					int totWood = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.WOOD);
					int totStone = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.STONE);
					int totIron = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.IRON);
					int totGold = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.GOLD);
					int totDiamond = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.DIAMOND);
					int totEmerald = TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())].GetResource(Resources.EMERALD);

					WriteToEcoStats(TeamEnum.values()[i].getName(), foodDelta, woodDelta, stoneDelta, ironDelta, goldDelta, diamondDelta, emeraldDelta, totFood, totWood, totStone, totIron, totGold, totDiamond, totEmerald);
					WriteBuildingStats(TeamEnum.values()[i].getName(), TSRTS.teamInfoArray[TeamEnum.getIDFromName(TeamEnum.values()[i].getName())]);
				}

			}

			for (int i = 0; i < TeamEnum.values().length; i++) {
				if (TSRTS.TeamQueues[i].hasChanged) {
					ModNetwork.SendToALLPlayers(new UnitQueueChangedPacketToClient(TSRTS.TeamQueues[i].getBarracks(), TSRTS.TeamQueues[i].getArcheryRange(), TSRTS.TeamQueues[i].getStables(), TSRTS.TeamQueues[i].getSiegeWorkshop(), i));

					TSRTS.TeamQueues[i].hasChanged = false;
				}
			}

		}
	}

	public static void WriteBuildingStats(String teamName, TeamInfo ti) {
		String delimiter = ",";
		if (writeHeader) {
			TSRTS.LOGGER.info("BUILDINGSTATS-HEADER: Timestamp, TeamName, Archeryrange , Armory , Baracks , Farms , Gates , LumberYard , MineSiteDiamond , MineSiteEmerald , MineSiteEmerald , MineSiteGold , MineSiteIron , MineSiteIron , MineSiteStone , ResearchCenter , Siegeworkshop , Stables , TownHalls , Walls , Wallsteps , Watchtowers");
			writeHeader = false;

		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String timeStamp = dtf.format(now);
		String tmp = "BUILDINGSTATS: " + delimiter + timeStamp + delimiter + teamName + delimiter + ti.getArcheryrange() + delimiter + ti.getArmory() + delimiter + ti.getBaracks() + delimiter + ti.getFarms() + delimiter + ti.getGates() + delimiter + ti.getLumberYard() + delimiter + ti.getMineSiteDiamond() + delimiter + ti.getMineSiteEmerald() + delimiter + ti.getMineSiteEmerald() + delimiter + ti.getMineSiteGold() + delimiter + ti.getMineSiteIron() + delimiter + ti.getMineSiteIron() + delimiter + ti.getMineSiteStone() + delimiter + ti.getResearchCenter() + delimiter + ti.getSiegeworkshop() + delimiter + ti.getStables() + delimiter + ti.getTownHalls() + delimiter + ti.getWalls() + delimiter + ti.getWallsteps() + delimiter + ti.getWatchtowers();

		TSRTS.LOGGER.info(tmp);

	}

	public static void WriteToEcoStats(String teamName, int foodDelta, int woodDelta, int stoneDelta, int ironDelta, int goldDelta, int diamondDelta, int emeraldDelta, int totFood, int totWood, int totStone, int totIron, int totGold, int totDiamond, int totEmerald) {
		String delimiter = ",";

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String timeStamp = dtf.format(now);
		String tmp = "ECOSTATS: " + delimiter + timeStamp + delimiter + teamName + delimiter + foodDelta + delimiter + woodDelta + delimiter + stoneDelta + delimiter + ironDelta + delimiter + goldDelta + delimiter + diamondDelta + delimiter + emeraldDelta + delimiter + totFood + delimiter + totWood + delimiter + totStone + delimiter + totIron + delimiter + totGold + delimiter + totDiamond + delimiter + totEmerald;

		TSRTS.LOGGER.info(tmp);

	}

}
