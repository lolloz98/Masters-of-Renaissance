package it.polimi.ingsw.server.model.cards;

import java.io.Serializable;

public interface VictoryPointCalculator extends Serializable {
    /**
     * @return number of victoryPoints related to the object implementing this interface
     */
    int getVictoryPoints();
}
