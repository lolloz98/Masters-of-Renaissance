package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;

public abstract class ControllerActions<T extends Game<?>> {
    private final T game;
    private final ControllerException exceptionHandler;

    public ControllerActions(T game, ControllerException exceptionHandler) {
        this.game = game;
        this.exceptionHandler = exceptionHandler;
    }
}
