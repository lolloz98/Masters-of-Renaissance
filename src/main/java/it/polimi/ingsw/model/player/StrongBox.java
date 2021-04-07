package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.model.game.Resource;

import java.util.TreeMap;

/**
 * class that models the lockbox of the player
 */
public class StrongBox {
    private final TreeMap<Resource,Integer> resources;

    public StrongBox() {
        resources = new TreeMap<>();
        /* superflui??
        resources.put(Resource.SHIELD, 0);
        resources.put(Resource.GOLD, 0);
        resources.put(Resource.ROCK, 0);
        resources.put(Resource.SERVANT, 0);
        */

    }

    public TreeMap<Resource, Integer> getResources() {
        return new TreeMap<>(resources);
    }

    /**
     * Add resGained to the strongBox
     *
     * @param resGained gained resources, to add to the strongBox
     * @throws IllegalArgumentException if there is any negative integer in resGained
     * @throws ResourceNotDiscountableException if there is any resource notDiscountable in resGained
     */
    public void addResources(TreeMap<Resource, Integer> resGained) {
        for (Resource r : resGained.keySet()) {
            if (resGained.get(r) < 0) throw new IllegalArgumentException();
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            if (resources.containsKey(r)) {
                resources.replace(r, resources.get(r) + resGained.get(r));
            } else {
                resources.put(r, resGained.get(r));
            }
        }
    }

    /**
     * Subtract resources in resToSpend from the strongBox
     *
     * @param resToSpend resources to subtract from the strongBox
     * @throws NotEnoughResourcesException if there are not enough resources in the strongBox
     * @throws IllegalArgumentException if there is a negative Integer in resToSpend
     */
    public void spendResources(TreeMap<Resource, Integer> resToSpend) {
        if (!hasResources(resToSpend)) throw new NotEnoughResourcesException();
        for (Resource r : resToSpend.keySet()) {
            if (resToSpend.get(r) < 0) throw new IllegalArgumentException();
            resources.replace(r, resources.get(r) - resToSpend.get(r));
        }
    }

    /**
     * @param resToSpend check if the strongBox has this amount of resources
     * @return true, if there are at least resToSpend resources in the strongBox, false otherwise
     */
    public boolean hasResources(TreeMap<Resource,Integer> resToSpend){
        for(Resource r: resToSpend.keySet()) {
            if(resources.get(r) == null && resToSpend.get(r) > 0)
                return false;
            if (resources.get(r) < resToSpend.get(r))
                return false;
        }
        return true;
    }
}

