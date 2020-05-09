package com.runeterra.net.packet.impl;

import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketConstants;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.dialogue.DialogueManager;

/**
 * This packet listener handles player's mouse click on the "Click here to
 * continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case PacketConstants.DIALOGUE_OPCODE:
			DialogueManager.next(player);
			break;
		}
	}
}
