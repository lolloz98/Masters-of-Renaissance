package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class TurnMulti extends Turn {
    private final Player currentPlayer;

    public TurnMulti(Player currentPlayer) {
        super();
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method that computes the next turn.
     *
     * @return null if the game is over, otherwise returns the next turn.
     */
    @Override
    public TurnMulti nextTurn(Game game){
        MultiPlayer multiPlayer = (MultiPlayer) game;
        ArrayList<Player> allPlayers = multiPlayer.getPlayers();
        multiPlayer.checkEndConditions();
        if (multiPlayer.isLastTurn() && currentPlayer == allPlayers.get(allPlayers.size()-1)) return null;
        else {
            int ind = allPlayers.indexOf(currentPlayer);
            return new TurnMulti(allPlayers.get((ind + 1) % allPlayers.size()));
        }
    }
}
