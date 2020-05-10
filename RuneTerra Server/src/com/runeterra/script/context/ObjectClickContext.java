package com.runeterra.script.context;

import com.runeterra.world.entity.impl.object.GameObject;
import com.runeterra.world.entity.impl.player.Player;

public class ObjectClickContext {
    public final Player player;
    public final GameObject gameObject;

    public ObjectClickContext(Player player, GameObject gameObject) {
        this.player = player;
        this.gameObject = gameObject;
    }
}
