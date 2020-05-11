package com.runeterra.net.packet.impl;

import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.container.impl.Bank;
import com.runeterra.world.model.container.impl.Inventory;

/**
 * This packet listener is called when an item is dragged onto another slot.
 * 
 * @author relex lawl
 */

public class SwitchItemSlotPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getHealth() <= 0)
			return;
		int interfaceId = packet.readInt();
		packet.readByteC();
		int fromSlot = packet.readLEShortA();
		int toSlot = packet.readLEShort();

		// Bank..
		if (interfaceId >= Bank.CONTAINER_START && interfaceId < Bank.CONTAINER_START + Bank.TOTAL_BANK_TABS) {

			final int tab = player.isSearchingBank() ? Bank.BANK_SEARCH_TAB_INDEX : interfaceId - Bank.CONTAINER_START;

			if (fromSlot >= 0 && fromSlot < player.getBank(tab).capacity() && toSlot >= 0
					&& toSlot < player.getBank(tab).capacity() && toSlot != fromSlot) {
				Bank.rearrange(player, player.getBank(tab), fromSlot, toSlot);
			}

			return;
		}

		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
		case Bank.INVENTORY_INTERFACE_ID:
			if (fromSlot >= 0 && fromSlot < player.getInventory().capacity() && toSlot >= 0
					&& toSlot < player.getInventory().capacity() && toSlot != fromSlot) {
				player.getInventory().swap(fromSlot, toSlot).refreshItems();
			}
			break;
		}
	}
}
