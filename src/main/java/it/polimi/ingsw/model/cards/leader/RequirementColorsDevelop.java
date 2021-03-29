package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.player.Player;

import java.util.TreeMap;

public class RequirementColorsDevelop implements Requirement {
    private final TreeMap<Color, Integer> requiredDevelops;

    /**
     * @return required develop cards (how many for each color), for this requirement
     */
    public TreeMap<Color, Integer> getRequiredDevelop() {
        TreeMap<Color, Integer> copy = new TreeMap<>();
        copy.putAll(requiredDevelops);
        return copy;
    }

    public RequirementColorsDevelop(TreeMap<Color, Integer> requiredDevelops){
        this.requiredDevelops = new TreeMap<>();
        this.requiredDevelops.putAll(requiredDevelops);
    }

    @Override
    public boolean checkRequirement(Player player) {
        // TODO: check if in player there are the requiredDevelop by color
        return false;
    }
}
