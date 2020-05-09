package com.runeterra.world.model.teleportation.operational.impl;

import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.PlayerRights;
import com.runeterra.world.model.teleportation.operational.Operationable;

/**
 * Dummy testing
 * @author Dennis
 *
 */
public class AmuletOfGlory implements Operationable {

	@Override
	public void executeOperationalItem(Player player) {
		player.getPacketSender().sendPrivateMessage(player.getLongUsername(), PlayerRights.ADMINISTRATOR, "Amulet of glory functional!");
	}

	@Override
	public int itemId() {
		return 1712;
	}
}