package com.runeterra.net.packet.impl;

import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.entity.impl.player.Player;

public class PlayerInactivePacketListener implements PacketListener {

	// CALLED EVERY 3 MINUTES OF INACTIVITY

	@Override
	public void handleMessage(Player player, Packet packet) {

	}
}
