package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.server.model.player.Player;

import java.io.Serializable;

public interface Requirement extends Serializable {
    /**
     * @param player player owning the object with this requirement
     * @return true, if the requirement, otherwise false
     */
    boolean checkRequirement(Player player) throws ResourceNotDiscountableException;
}
