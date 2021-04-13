package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WarehouseType;

import java.util.TreeMap;

public interface Requirement {
    /**
     * @param player player owning the object with this requirement
     * @param toPay  resources to be paid for the activation of this card
     * @return true, if the requirement is satisfied and toPay
     * contains precisely the resources to pay (if none, toPay must be an empty treeMap), otherwise false
     */
    boolean checkRequirement(Player player, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay);

    /**
     * @return treeMap with resources to be paid. Empty treeMap if no resources need to be paid
     */
    TreeMap<Resource, Integer> getResourcesToBePaid();
}
