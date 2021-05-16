package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.enums.Resource;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * class that models the lockbox of the player
 */
public class StrongBox implements Serializable {
    private static final long serialVersionUID = 1032L;

    private final TreeMap<Resource, Integer> resources;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StrongBox){
            return resources.equals(((StrongBox) obj).resources);
        }
        return false;
    }

    public StrongBox() {
        resources = new TreeMap<>();
    }

    public TreeMap<Resource, Integer> getResources() {
        return new TreeMap<>(resources);
    }

    /**
     * Add resGained to the strongBox
     *
     * @param resGained gained resources, to add to the strongBox
     * @throws InvalidArgumentException         if there is any negative integer in resGained
     * @throws ResourceNotDiscountableException if there is any resource notDiscountable in resGained
     */
    public void addResources(TreeMap<Resource, Integer> resGained) throws ResourceNotDiscountableException, InvalidArgumentException {
        for (Resource r : resGained.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            if (resGained.get(r) < 0) throw new InvalidArgumentException("Cannot gain a negative amount of resources");
        }
        for (Resource r : resGained.keySet()) {
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
     * @throws InvalidArgumentException    if there is a negative Integer in resToSpend
     */
    public void spendResources(TreeMap<Resource, Integer> resToSpend) throws ResourceNotDiscountableException, NotEnoughResourcesException, InvalidArgumentException {
        for (Resource r : resToSpend.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            if (resToSpend.get(r) < 0) throw new InvalidArgumentException("Cannot spend a negative amount of resources");
        }
        if (!hasResources(resToSpend)) throw new NotEnoughResourcesException();
        for (Resource r : resToSpend.keySet()) {
            resources.replace(r, resources.get(r) - resToSpend.get(r));
            if (resources.get(r) == 0)
                resources.remove(r);
        }
    }

    /**
     * @param resToSpend check if the strongBox has this amount of resources
     * @return true, if there are at least resToSpend resources in the strongBox, false otherwise
     */
    protected boolean hasResources(TreeMap<Resource, Integer> resToSpend) throws ResourceNotDiscountableException, InvalidArgumentException {
        for (Resource r : resToSpend.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            if (resToSpend.get(r) < 0) throw new InvalidArgumentException("Cannot spend a negative amount of resources");
        }
        for (Resource r : resToSpend.keySet()) {
            if (!resources.containsKey(r) || resources.get(r) < resToSpend.get(r))
                return false;
        }
        return true;
    }
}

