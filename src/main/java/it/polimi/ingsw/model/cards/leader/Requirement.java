package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.player.Player;

public interface Requirement {
    /**
     * @param player player owning the object with this requirement
     * @return true, if the requirement, otherwise false
     */
    boolean checkRequirement(Player player);
}
