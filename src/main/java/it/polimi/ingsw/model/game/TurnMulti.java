package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

public class TurnMulti extends Turn {
    private Player currentPlayer;

    public TurnMulti() {
        // sets current player
    }

    public TurnMulti nextTurn(MultiPlayer multiPlayer){
        return new TurnMulti(); // with the next player
    }
}
