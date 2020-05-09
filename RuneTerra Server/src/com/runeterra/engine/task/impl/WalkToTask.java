package com.runeterra.engine.task.impl;

import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.Position;
import com.runeterra.world.model.movement.MovementStatus;

/**
 * Represents a movement action for a game character.
 *
 * @author Gabriel Hannason
 */

public class WalkToTask {

    public interface FinalizedMovementTask {
        void execute();
    }

    private final int distance;
    private final Player player;
    private final Position destination;
    private final FinalizedMovementTask finalizedTask;

    /**
     * The WalkToTask constructor.
     *
     * @param entity        The associated game character.
     * @param destination   The destination the game character will move to.
     * @param finalizedTask The task a player must execute upon reaching said destination.
     */
    public WalkToTask(Player entity, Position destination, int distance, FinalizedMovementTask finalizedTask) {
        this.player = entity;
        this.destination = destination;
        this.finalizedTask = finalizedTask;
        this.distance = distance;
    }

    /**
     * Executes the action if distance is correct
     */
    public void onTick() {
        if (player == null)
            return;
        if (!player.isRegistered()) {
            player.setWalkToTask(null);
            return;
        }
        if (player.busy() || destination == null
                || player.getMovementQueue().getMovementStatus() == MovementStatus.DISABLED) {
            player.setWalkToTask(null);
            return;
        }
        if (player.getPosition().getDistance(destination) <= distance) {
            finalizedTask.execute();
            if (player.getInteractingEntity() != null) {
                player.setEntityInteraction(null);
            }
            player.setWalkToTask(null);
        }
    }
}
