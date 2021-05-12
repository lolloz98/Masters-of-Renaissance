package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.MainActionNotOccurredException;
import it.polimi.ingsw.server.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.server.model.exception.ProductionsResourcesNotFlushedException;
import it.polimi.ingsw.server.model.player.Player;
import java.util.ArrayList;

/**
 * Concrete extension of the class Turn. It has all the variables and methods needed to represent the state of a turn in a MultiPlayer game.
 */

public class TurnMulti extends Turn {
    private final Player currentPlayer;

    public TurnMulti(Player currentPlayer) {
        super();
        this.currentPlayer = currentPlayer;
        this.isPlayable = true;
    }

    public TurnMulti(Player currentPlayer, boolean isPlayable) {
        super();
        this.currentPlayer = currentPlayer;
        this.isPlayable = isPlayable;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method that computes the next turn.
     *
     * @return null if the game is over, otherwise returns the next turn.
     * @throws MarketTrayNotEmptyException if there are resources not flushed in market tray.
     * @throws ProductionsResourcesNotFlushedException if there are resources not flushed in a production.
     * @throws MainActionNotOccurredException if the main action hasn't occurred yet in this turn.
     */
    @Override
    public TurnMulti nextTurn(Game<? extends Turn> game) throws MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        checkConditions();
        MultiPlayer multiPlayer = (MultiPlayer) game;
        ArrayList<Player> allPlayers = multiPlayer.getPlayers();
        multiPlayer.checkEndConditions();
        if (multiPlayer.isLastRound() && currentPlayer == allPlayers.get(allPlayers.size()-1)) return new TurnMulti(currentPlayer, false);
        else {
            int ind = allPlayers.indexOf(currentPlayer);
            return new TurnMulti(allPlayers.get((ind + 1) % allPlayers.size()));
        }
    }
}
