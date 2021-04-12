package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.model.exception.MainActionNotOccurredException;
import it.polimi.ingsw.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.model.exception.ProductionsResourcesNotFlushedException;

/**
 * Class that represents the state of the turn. It's abstract as the game must be either a TurnSingle, containing the state
 * of the turn in a SinglePlayer game, or TurnMulti, containing the state of the turn in a MultiPlayer game.
 */

public abstract class Turn {
    private boolean mainActionOccurred;
    private boolean productionsActivated;
    private boolean marketActivated;

    public boolean isProductionsActivated() {
        return productionsActivated;
    }

    public boolean isMainActionOccurred() {
        return mainActionOccurred;
    }

    public boolean isMarketActivated() {
        return marketActivated;
    }

    /**
     * Sets the marketActivated flag. If marketActivated goes from true to false, also sets mainActionOccurred to true.
     *
     * @throws MainActionAlreadyOccurredException if it's called twice, if a main action already occurred, or if there are resources not flushed in a production.
     */
    public void setMarketActivated(boolean marketActivated) {
        if (mainActionOccurred || productionsActivated || (this.marketActivated && marketActivated)) throw new MainActionAlreadyOccurredException();
        else {
            if (this.marketActivated && !marketActivated) {
                this.marketActivated = false;
                setMainActionOccurred();
            }
            this.marketActivated = marketActivated;
        }
    }

    /**
     * Sets the productionsActivated flag. If productionsActivated goes from true to false, also sets mainActionOccurred to true.
     *
     * @throws MainActionAlreadyOccurredException if a main action already occurred, or if there are resources not flushed in market tray.
     */
    public void setProductionsActivated(boolean productionsActivated) {
        if (mainActionOccurred || marketActivated) throw new MainActionAlreadyOccurredException();
        else {
            if (this.productionsActivated && !productionsActivated) {
                this.productionsActivated = false;
                setMainActionOccurred();
            }
            this.productionsActivated = productionsActivated;
        }
    }

    /**
     * Sets the mainActionOccurred flag.
     *
     * @throws MarketTrayNotEmptyException if there are resources not flushed in market tray.
     * @throws ProductionsResourcesNotFlushedException if there are resources not flushed in a production.
     * @throws MainActionAlreadyOccurredException if it's called twice, as the main action can only occur once in every turn.
     */
    public void setMainActionOccurred() {
        if (marketActivated) throw new MarketTrayNotEmptyException();
        if (productionsActivated) throw new ProductionsResourcesNotFlushedException();
        if (mainActionOccurred) throw new MainActionAlreadyOccurredException();
        this.mainActionOccurred = true;
    }

    public abstract Turn nextTurn(Game<? extends Turn> game);

    public Turn(){
        marketActivated = false;
        productionsActivated = false;
        mainActionOccurred = false;
    }

    /**
     * Helper method to check if a main action occurred, or if there are resources not flushed in market tray or in a production.
     *
     * @throws MarketTrayNotEmptyException if there are resources not flushed in market tray.
     * @throws ProductionsResourcesNotFlushedException if there are resources not flushed in a production.
     * @throws MainActionNotOccurredException if the main action hasn't occurred yet in this turn.
     */
    protected void checkConditions(){
        if (marketActivated) throw new MarketTrayNotEmptyException();
        if (productionsActivated) throw new ProductionsResourcesNotFlushedException();
        if (!mainActionOccurred) throw new MainActionNotOccurredException();
    }
}
