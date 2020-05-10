package com.runeterra.script.api;

import com.runeterra.script.context.ObjectClickContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectBehavior implements Behavior {
    private final Map<Integer, Consumer<ObjectClickContext>> firstClickHandlers = new HashMap<>();
    private final Map<Integer, Consumer<ObjectClickContext>> secondClickHandlers = new HashMap<>();
    private final Map<Integer, Consumer<ObjectClickContext>> thirdClickHandlers = new HashMap<>();
    private final Map<Integer, Consumer<ObjectClickContext>> fourthClickHandlers = new HashMap<>();
    private final Map<Integer, Consumer<ObjectClickContext>> fifthClickHandlers = new HashMap<>();

    @Override
    public void initialize() {
        firstClickHandlers.clear();
        secondClickHandlers.clear();
        thirdClickHandlers.clear();
        fourthClickHandlers.clear();
        fifthClickHandlers.clear();
    }

    public void onFirstClick(int objectId, Consumer<ObjectClickContext> handler) {
        firstClickHandlers.put(objectId, handler);
    }

    public void onSecondClick(int objectId, Consumer<ObjectClickContext> handler) {
        secondClickHandlers.put(objectId, handler);
    }

    public void onThirdClick(int objectId, Consumer<ObjectClickContext> handler) {
        thirdClickHandlers.put(objectId, handler);
    }

    public void onFourthClick(int objectId, Consumer<ObjectClickContext> handler) {
        fourthClickHandlers.put(objectId, handler);
    }

    public void onFifthClick(int objectId, Consumer<ObjectClickContext> handler) {
        fifthClickHandlers.put(objectId, handler);
    }

    public Map<Integer, Consumer<ObjectClickContext>> getFirstClickHandlers() {
        return firstClickHandlers;
    }

    public Map<Integer, Consumer<ObjectClickContext>> getSecondClickHandlers() {
        return secondClickHandlers;
    }

    public Map<Integer, Consumer<ObjectClickContext>> getThirdClickHandlers() {
        return thirdClickHandlers;
    }

    public Map<Integer, Consumer<ObjectClickContext>> getFourthClickHandlers() {
        return fourthClickHandlers;
    }

    public Map<Integer, Consumer<ObjectClickContext>> getFifthClickHandlers() {
        return fifthClickHandlers;
    }
}
