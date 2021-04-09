package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.player.DevelopCardSlot;
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
        this.requiredDevelops = new TreeMap<>(requiredDevelops);
    }

    /**
     * @param player player owning the object with this requirement
     * @return true if player has at least the develop cards specified in requiredDevelops
     */
    @Override
    public boolean checkRequirement(Player player) {
        TreeMap<Color, Integer> req = new TreeMap<>(requiredDevelops);
        for(DevelopCardSlot ds: player.getBoard().getDevelopCardSlots())
            for(DevelopCard c: ds.getCards())
                if(req.containsKey(c.getColor())) {
                    req.replace(c.getColor(), req.get(c.getColor()) - 1);
                }

        for(Color c: req.keySet()){
            if(req.get(c) > 0) return false;
        }
        return true;
    }
}
