package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.playing.DestroyedView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalGameState;

/**
 * Abstract class that will be extended by the views which work like a conversation to pick resources combinations, for
 * example FlushMarketCombination and ActivateProduction.
 * The reason for this is to implement in all of these views the handling of the destroyed game.
 */
public abstract class ConversationalView extends View<CLI> {
    protected LocalGame<?> localGame;

    @Override
    public void notifyUpdate() {
        if (localGame.getState() == LocalGameState.DESTROYED){
            localGame.removeAllObservers();
            ui.setState(new DestroyedView(ui));
            // must call the draw function because this state change is not called by a player command, but by an update from the server
            ui.getState().draw();
        }
    }
}
