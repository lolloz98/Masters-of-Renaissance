package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionNotOccurredException;
import it.polimi.ingsw.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.model.exception.ProductionsResourcesNotFlushedException;

public class TurnSingle extends Turn{
    private final boolean lorenzoPlaying;

    public TurnSingle(boolean lorenzoPlaying) {
        super();
        this.lorenzoPlaying = lorenzoPlaying;
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
        if (singlePlayer.isLastTurn()) return null;
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
