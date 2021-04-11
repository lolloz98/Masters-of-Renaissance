package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.game.Resource;

import java.util.TreeMap;

/**
 * class that models a shelf of the warehouse, leaders shelfs's included
 */

public class Depot {
    private Resource resource;
    private final int maxToStore;
    private int stored;
    /**
     * true only if the depot is not a leader depot
     */
    private final boolean modifiable;

    /**
     * normal depot constructor
     */
    public Depot(int maxToStore, boolean modifiable) {
        this.maxToStore = maxToStore;
        resource = Resource.NOTHING;
        stored = 0;
        this.modifiable = modifiable;
    }

    /**
     * leader depot constructor
     */
    public Depot(Resource r) {
        this.maxToStore = 2;
        resource = r;
        stored = 0;
        this.modifiable = false;
    }


    public int getStored() {
        return stored;
    }

    /**
     * @return resources contained in this depot
     */
    public TreeMap<Resource, Integer> getStoredResources() {
        return (isEmpty() ? new TreeMap<>() : new TreeMap<>() {{
            put(resource, stored);
        }});
    }

    public int getMaxToStore() {
        return maxToStore;
    }

    public int getFreeSpace() {
        return maxToStore - stored;
    }

    public Resource getTypeOfResource() {
        return resource;
    }

    public boolean isResModifiable() {
        return modifiable;
    }

    public void spendResources(int howMany) {
        if (howMany <= 0 || !enoughResources(howMany)) throw new InvalidResourceQuantityToDepotException();
        if (stored == howMany) clear();
        stored -= howMany;
    }

    public boolean contains(Resource r) {
        return r.equals(resource);
    }

    /**
     * modifies the type of resource that the depot contains
     *
     * @param type new type of resources that the depot can contain
     * @throws DepotResourceModificationException if the Depot is not modifiable, or it is not empty
     */
    public void modifyTypeOfResource(Resource type) {
        if (!isResModifiable()) throw new DepotResourceModificationException();
        if (stored != 0) throw new DepotResourceModificationException();
        resource = type;
    }

    /**
     * method that add n resources to the depot
     */
    public void addResource(Resource r, int howMany) {
        if (howMany < 0) throw new InvalidResourceQuantityToDepotException();
        if (!isResourceAppendable(r)) throw new DifferentResourceForDepotException();
        if (tooManyResources(howMany)) throw new TooManyResourcesToAddExeption();
        if (!Resource.isDiscountable(r)) throw new InvalidTypeOfResourceToDepotExeption();
        if (stored != 0)
            stored++;
        else {
            resource = r;
            stored = howMany;
        }
    }

    /**
     * method that checks if the depot is overfull
     */
    private boolean tooManyResources(int howMany) {
        return stored + howMany > maxToStore;
    }

    public boolean isFull() {
        return stored == maxToStore;
    }

    /**
     * method that controls if the resource r can be added at the depot
     */
    public boolean isResourceAppendable(Resource r) {
        if (modifiable)
            return isEmpty() || r.equals(resource);
        else
            return r.equals(resource);
    }

    public int clear() {
        int result = stored;
        if (modifiable) resource = Resource.NOTHING;
        stored = 0;
        return result;
    }


    public boolean isEmpty() {
        return stored == 0;
    }

    public boolean enoughResources(int n) {
        return stored - n >= 0;
    }
}
