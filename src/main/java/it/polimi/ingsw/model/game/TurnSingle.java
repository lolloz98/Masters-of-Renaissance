package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionNotOccurredException;
import it.polimi.ingsw.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.model.exception.ProductionsResourcesNotFlushedException;

/**
 * Concrete extension of the class Turn. It has all the variables and methods needed to represent the state of a turn in a SinglePlayer game.
 */

public class TurnSingle extends Turn{
    private final boolean lorenzoPlaying;

    public TurnSingle(boolean lorenzoPlaying) {
        super();
        this.lorenzoPlaying = lorenzoPlaying;
        this.isPlayable = true;
    }

    public TurnSingle(boolean lorenzoPlaying, boolean isPlayable) {
        super();
        this.lorenzoPlaying = lorenzoPlaying;
        this.isPlayable = isPlayable;
    }

    public boolean isLorenzoPlaying() {
        return lorenzoPlaying;
    }

    /**
     * Method that computes the next turn.
     *
     * @param game
     * @return null if the game is over, otherwise returns the next turn.
     * @throws MarketTrayNotEmptyException if there are resources not flushed in market tray.
     * @throws ProductionsResourcesNotFlushedException if there are resources not flushed in a production.
     * @throws MainActionNotOccurredException if the main action hasn't occurred yet in this turn.
     */
    @Override
    public TurnSingle nextTurn(Game<? extends Turn> game){
        checkConditions();
        SinglePlayer singlePlayer = (SinglePlayer) game;
        singlePlayer.checkEndConditions();
        if (singlePlayer.isLastTurn()) return new TurnSingle(lorenzoPlaying, false);
        else {
            if (lorenzoPlaying) {
                return new TurnSingle(false);
            }
            else {
                return new TurnSingle(true);
            }
        }
    }
}
