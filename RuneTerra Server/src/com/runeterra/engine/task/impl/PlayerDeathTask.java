package com.runeterra.engine.task.impl;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.runeterra.GameConstants;
import com.runeterra.engine.task.Task;
import com.runeterra.world.content.ItemsKeptOnDeath;
import com.runeterra.world.entity.combat.pvp.BountyHunter;
import com.runeterra.world.entity.combat.pvp.BountyHunter.Emblem;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.grounditems.GroundItemManager;
import com.runeterra.world.model.Animation;
import com.runeterra.world.model.DamageDealer;
import com.runeterra.world.model.Flag;
import com.runeterra.world.model.GroundItem;
import com.runeterra.world.model.Item;
import com.runeterra.world.model.Locations.Location;
import com.runeterra.world.model.Position;
import com.runeterra.world.model.Skill;
import com.runeterra.world.model.movement.MovementStatus;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

	/**
	 * The PlayerDeathTask constructor.
	 * 
	 * @param player
	 *            The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	private Player player;
	private int ticks = 5;
	private boolean dropItems = true;
	Position oldPosition;
	Location loc;
	ArrayList<Item> itemsToKeep = null;

	@Override
	public void execute() {
		if (player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				player.getMovementQueue().setMovementStatus(MovementStatus.DISABLED).reset();
				break;
			case 3:
				player.performAnimation(new Animation(0x900));
				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				break;
			case 1:
				this.oldPosition = player.getPosition().copy();
				this.loc = player.getLocation();

				DamageDealer damageDealer = player.getCombat().getTopDamageDealer(true, null);
				Player killer = damageDealer == null ? null : damageDealer.getPlayer();

				/*
				 * if(player.getRights().equals(PlayerRights.OWNER) ||
				 * player.getRights().equals(PlayerRights.DEVELOPER)) {
				 * dropItems = false; } if(killer != null) {
				 * if(killer.getRights().equals(PlayerRights.OWNER) ||
				 * killer.getRights().equals(PlayerRights.DEVELOPER)) {
				 * dropItems = false; } }
				 */
				if (dropItems) {

					// Delete emblems from inventory
					for (Emblem emblem : BountyHunter.Emblem.values()) {
						if (player.getInventory().contains(emblem.id)) {
							player.getInventory().delete(emblem.id, player.getInventory().getAmount(emblem.id));
						}
					}

					// Get items to keep
					itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);

					// Fetch player's items
					final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
					playerItems.addAll(player.getInventory().getValidItems());
					playerItems.addAll(player.getEquipment().getValidItems());

					// The position the items will be dropped at
					final Position position = player.getPosition();

					// Go through player items, drop them to killer
					for (Item item : playerItems) {

						// Keep tradeable items
						if (!item.getDefinition().isTradeable() || itemsToKeep.contains(item)) {
							if (!itemsToKeep.contains(item)) {
								itemsToKeep.add(item);
							}
							continue;
						}

						// Drop items
						if (item != null && item.getId() > 0 && item.getAmount() > 0) {
							GroundItemManager.spawnGroundItem((killer != null ? killer : player),
									new GroundItem(item, position,
											killer != null ? killer.getUsername() : player.getUsername(),
											player.getHostAddress(), false, 150, true, 150));
						}

					}

					// Give killer rewards
					if (killer != null) {
						if (killer.getLocation() == Location.WILDERNESS) {
							killer.getBountyHunter().killedPlayer(player);
						}
					}

					player.getInventory().resetItems().refreshItems();
					player.getEquipment().resetItems().refreshItems();
				}
				player.getPacketSender().sendInterfaceRemoval();
				player.getCombat().reset();
				player.setWalkToTask(null);
				player.getSkillManager().stopSkilling();
				break;
			case 0:
				if (dropItems) {
					if (itemsToKeep != null) {
						for (Item it : itemsToKeep) {
							player.getInventory().add(it.getId(), 1);
						}
						itemsToKeep.clear();
					}
				}
				player.restart();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				loc.onDeath(player);
				if (player.getPosition().equals(oldPosition)) {
					player.moveTo(GameConstants.DEFAULT_POSITION.copy());
				}
				player = null;
				oldPosition = null;
				stop();
				break;
			}
			ticks--;
		} catch (Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if (player != null) {
				player.moveTo(GameConstants.DEFAULT_POSITION.copy());
				player.setHealth(player.getSkillManager().getMaxLevel(Skill.HITPOINTS));
			}
		}
	}
}
