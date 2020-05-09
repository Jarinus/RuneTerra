package com.runeterra.world.model.syntax.impl;

import com.runeterra.world.content.clan.ClanChatManager;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.syntax.EnterSyntax;

public class JoinClanChat implements EnterSyntax {

	@Override
	public void handleSyntax(Player player, String input) {
		ClanChatManager.join(player, input);
	}

	@Override
	public void handleSyntax(Player player, int input) {
	}

}
