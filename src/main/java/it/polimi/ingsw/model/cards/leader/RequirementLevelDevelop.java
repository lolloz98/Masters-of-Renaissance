package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.player.Player;

public class RequirementLevelDevelop implements Requirement {
    // in all the cards that have this requirement, the level is 2
    private final int level = 2;
    private final Color color;

    public RequirementLevelDevelop(Color color){
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

    @Override
    public boolean checkRequirement(Player player) {
        // TODO: check if in player there is the requiredDevelop by color and level
        return false;
    }
}
