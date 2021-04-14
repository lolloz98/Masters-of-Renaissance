package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.DevelopCardSlot;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WarehouseType;

import java.util.TreeMap;

/**
 * To be fulfilled, the player needs to have one developCards of a specific color of level 2.
 */
public class RequirementLevelDevelop implements Requirement {
    // in all the cards that have this requirement, the level is 2
    private final int level = 2;
    private final Color color;

    public RequirementLevelDevelop(Color color) {
        this.color = color;
    }

    /**
     * @return level of the card needed for this requirement
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return color of the card needed for this requirement
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param player player owning the object with this requirement
     * @return true, if the player has at least a DevelopCard of level 2 of color this.color
     */
    @Override
    public boolean checkRequirement(Player player) {

        for (DevelopCardSlot ds : player.getBoard().getDevelopCardSlots())
            for (DevelopCard c : ds.getCards())
                if (c.getLevel() == level && c.getColor() == color) return true;
        return false;
    }
}
