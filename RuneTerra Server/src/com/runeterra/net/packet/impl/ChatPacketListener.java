package com.runeterra.net.packet.impl;

import com.runeterra.net.packet.Packet;
import com.runeterra.net.packet.PacketListener;
import com.runeterra.util.Misc;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.ChatMessage.Message;
import com.runeterra.world.model.Flag;
import com.runeterra.world.model.dialogue.DialogueManager;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author Gabriel Hannason
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int color = packet.readByte();
		int effect = packet.readByte();
		String text = packet.readString();
		if (text.length() <= 0) {
			return;
		}
		/*
		 * if(PlayerPunishment.muted(player.getUsername()) ||
		 * PlayerPunishment.IPMuted(player.getHostAddress())) {
		 * player.getPacketSender().sendMessage("You are muted and cannot chat."
		 * ); return; }
		 */
		if (Misc.blockedWord(text)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		player.getChatMessages().set(new Message(color, effect, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
