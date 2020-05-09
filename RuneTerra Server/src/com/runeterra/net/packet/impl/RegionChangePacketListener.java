package com.runeterra.net.packet.impl;

import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.collision.region.RegionClipping;
import com.runeterra.world.entity.impl.object.ObjectHandler;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.grounditems.GroundItemManager;

public class RegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isAllowRegionChangePacket()) {
			/*
			 * RegionClipping.loadRegion(player.getPosition().getX(),
			 * player.getPosition().getY());
			 * player.getPacketSender().sendMapRegion();
			 * CustomObjects.handleRegionChange(player);
			 * GroundItemManager.handleRegionChange(player);
			 * Sounds.handleRegionChange(player); player.getTolerance().reset();
			 * Hunter.handleRegionChange(player); if(player.getRegionInstance()
			 * != null && player.getPosition().getX() != 1 &&
			 * player.getPosition().getY() != 1) {
			 * if(player.getRegionInstance().equals(RegionInstanceType.BARROWS)
			 * || player.getRegionInstance().equals(RegionInstanceType.
			 * WARRIORS_GUILD)) player.getRegionInstance().destruct(); }
			 * player.getNpcFacesUpdated().clear();
			 */
			RegionClipping.loadRegion(player.getPosition().getX(), player.getPosition().getY());
			ObjectHandler.onRegionChange(player);

			// CustomObjects.handleRegionChange(player);
			GroundItemManager.onRegionChange(player);
			player.getTolerance().reset();
			player.setRegionChange(false).setAllowRegionChangePacket(false);
		}
	}
}
