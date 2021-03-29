package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

public class TurnSingle extends Turn{
    private boolean isLorenzoPlaying;
    private Player player;

    public TurnSingle(boolean isLorenzoPlaying) {
        this.isLorenzoPlaying = isLorenzoPlaying;
    }

    public TurnSingle nextTurn(SinglePlayer singlePlayer){
        if(isLorenzoPlaying) return new TurnSingle(false);
        else return new TurnSingle(true);
    }

    public void performLorenzoAction(SinglePlayer singlePlayer){

    }
}
