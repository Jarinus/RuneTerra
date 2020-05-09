package com.runeterra.net.packet.impl;

import com.runeterra.cache.impl.definitions.NpcDefinition;
import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if (npc <= 0) {
			return;
		}
		System.out.println("NPC: " + npc);
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if (npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
		}
	}

}
