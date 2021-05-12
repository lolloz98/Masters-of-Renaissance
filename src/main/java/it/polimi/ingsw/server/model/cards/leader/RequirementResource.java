package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.Player;

import java.util.TreeMap;

/**
 * To be fulfilled, the player needs to have 5 resources of a specific type.
 */
public class RequirementResource implements Requirement {
    private final Resource res;
    private final int quantity = 5;

    public RequirementResource(Resource resRequired) {
        this.res = resRequired;
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

    /**
     * @param player player owning the object with this requirement
     * @return true, if the player has enough resources to spend for this requirement
     */
    @Override
    public boolean checkRequirement(Player player) throws ModelException {
        return player.getBoard().enoughResToActivate(new TreeMap<>() {{
            put(res, quantity);
        }});
    }
}
