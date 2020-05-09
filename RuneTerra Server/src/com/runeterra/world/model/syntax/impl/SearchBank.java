package com.runeterra.world.model.syntax.impl;

import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.container.impl.Bank;
import com.runeterra.world.model.syntax.EnterSyntax;

public class SearchBank implements EnterSyntax {

	@Override
	public void handleSyntax(Player player, String input) {
		Bank.search(player, input);
	}

	@Override
	public void handleSyntax(Player player, int input) {

	}

}
