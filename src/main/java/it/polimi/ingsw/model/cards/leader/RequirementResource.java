package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.TreeMap;

public class RequirementResource implements Requirement{
    private final Resource res;
    private final int quantity;

    public RequirementResource(Resource resRequired, int quantity){
        this.res = resRequired;
        this.quantity = quantity;
    }

    /**
     * @return type of resource for this requirement
     */
    public Resource getRes() {
        return res;
    }

    /**
     * @return quantity of resources needed for this requirement
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean checkRequirement(Player player) {
        // TODO: check if in the board of the player there are the necessary resources
        return false;
    }
}
