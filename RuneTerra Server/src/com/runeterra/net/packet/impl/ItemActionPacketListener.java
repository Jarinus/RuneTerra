package com.runeterra.net.packet.impl;

import com.runeterra.cache.impl.definitions.ItemDefinition;
import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketConstants;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.content.Consumables;
import com.runeterra.world.content.skills.herblore.HerbIdentification;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.Item;
import com.runeterra.world.model.teleportation.tabs.TabHandler;

@SuppressWarnings("unused")
public class ItemActionPacketListener implements PacketListener {

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int itemId = packet.readShort();
		int slot = packet.readShort();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		if (Consumables.isFood(player, interacted)) {
			return;
		}
		if (ItemDefinition.forId(interacted.getId()).getName().contains("Grimy")) {
			HerbIdentification.cleanHerb(player, interacted);
			return;
		}
		TabHandler.onClick(player, interacted);
		switch (interacted.getId()) {

		}
	}

	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		switch (interacted.getId()) {

		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		final Item interacted = player.getInventory().forSlot(slot);
		if (interacted == null || interacted.getId() != itemId || interacted.getSlot() != slot) {
			return;
		}
		switch (interacted.getId()) {

		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getHealth() <= 0) {
			return;
		}
		switch (packet.getOpcode()) {
		case PacketConstants.SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case PacketConstants.FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case PacketConstants.THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}

}