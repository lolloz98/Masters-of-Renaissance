package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

/**
 * class that models a shelf of the warehouse, leaders shelves included
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
        if(maxToStore<=0) throw new IllegalArgumentException();
        this.maxToStore = maxToStore;
        resource = Resource.NOTHING;
        stored = 0;
        this.modifiable = modifiable;
    }

    /**
     * normal depot constructor, always modifiable
     */
    public Depot(int maxToStore) {
        if(maxToStore<=0) throw new IllegalArgumentException();
        this.maxToStore = maxToStore;
        resource = Resource.NOTHING;
        stored = 0;
        this.modifiable = true;
    }

    /**
     * leader depot constructor
     */
    public Depot(Resource r) {
        this.maxToStore = 2;
        if(!Resource.isDiscountable(r)) throw new IllegalArgumentException();
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
        else
            stored -= howMany;
    }

    public boolean contains(Resource r) {
        return r.equals(resource);
    }

    /**
     * method that add n resources to the depot
     */
    public void addResource(Resource r, int howMany) {
        if (howMany <= 0 || tooManyResources(howMany)) throw new InvalidResourceQuantityToDepotException();
        if (!isResourceAppendable(r)) throw new DifferentResourceForDepotException();
        if (!Resource.isDiscountable(r)) throw new InvalidTypeOfResourceToDepotExeption();
        if (stored != 0)
            stored+=howMany;
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
    private boolean isResourceAppendable(Resource r) {
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
        return stored >= n ;
    }
}