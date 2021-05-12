package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.server.model.exception.MainActionNotOccurredException;
import it.polimi.ingsw.server.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.server.model.exception.ProductionsResourcesNotFlushedException;

/**
 * Class that represents the state of the turn. It's abstract as the game must be either a TurnSingle, containing the state
 * of the turn in a SinglePlayer game, or TurnMulti, containing the state of the turn in a MultiPlayer game.
 */

public abstract class Turn {
    private boolean mainActionOccurred;
    private boolean productionsActivated;
    private boolean marketActivated;
    protected boolean isPlayable;

    public boolean getIsPlayable() {
        return isPlayable;
    }

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
     * @param marketActivated is what to set the flag to
     * @throws MainActionAlreadyOccurredException if it's called twice, if a main action already occurred, or if there are resources not flushed in a production.
     */
    public void setMarketActivated(boolean marketActivated) throws MainActionAlreadyOccurredException, MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException {
        if (mainActionOccurred || productionsActivated || (this.marketActivated && marketActivated)) throw new MainActionAlreadyOccurredException();
        else {
            if (this.marketActivated) { // Take if market has been activated and now it is being set to false
                this.marketActivated = false;
                setMainActionOccurred();
            }
            this.marketActivated = marketActivated;
        }
    }

    /**
     * Sets the productionsActivated flag. If productionsActivated goes from true to false, also sets mainActionOccurred to true.
     *
     * @param productionsActivated is what to set the flag to
     * @throws MainActionAlreadyOccurredException if a main action already occurred, or if there are resources not flushed in market tray.
     */
    public void setProductionsActivated(boolean productionsActivated) throws MainActionAlreadyOccurredException, MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException {
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
     * Sets this.mainActionOccurred to true.
     *
     * @throws MarketTrayNotEmptyException if there are resources not flushed in market tray.
     * @throws ProductionsResourcesNotFlushedException if there are resources not flushed in a production.
     * @throws MainActionAlreadyOccurredException if it's called twice, as the main action can only occur once in every turn.
     */
    public void setMainActionOccurred() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        if (marketActivated) throw new MarketTrayNotEmptyException();
        if (productionsActivated) throw new ProductionsResourcesNotFlushedException();
        if (mainActionOccurred) throw new MainActionAlreadyOccurredException();
        this.mainActionOccurred = true;
    }

    public abstract Turn nextTurn(Game<? extends Turn> game) throws MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException;

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
    protected void checkConditions() throws MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        if (marketActivated) throw new MarketTrayNotEmptyException();
        if (productionsActivated) throw new ProductionsResourcesNotFlushedException();
        if (!mainActionOccurred) throw new MainActionNotOccurredException();
    }
}
