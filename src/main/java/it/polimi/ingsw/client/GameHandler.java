package it.polimi.ingsw.client;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.Observable;
import it.polimi.ingsw.messages.requests.ClientMessage;

import java.io.IOException;

public abstract class GameHandler extends Observable implements Runnable {
    protected LocalGame<?> localGame;

    public void setLocalGame(LocalGame<?> localGame) {
        this.localGame = localGame;
    }

    public abstract void dealWithMessage(ClientMessage message) throws IOException;

    @Override
    public abstract void run();
}
