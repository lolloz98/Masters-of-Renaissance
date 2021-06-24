package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * class that models a shelf of the warehouse, leaders shelves included
 */

public class Depot implements Serializable {
    private static final long serialVersionUID = 1028L;

    private Resource resource;
    private final int maxToStore;
    /**
     * how many resources in this depot
     */
    private int stored;
    /**
     * attribute useful to know which type of depot is this(normal or leader). true only if this depot is not a leader depot.
     */
    private final boolean modifiable;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Depot) {
            Depot t = (Depot) obj;
            return resource == t.resource && maxToStore == t.maxToStore && stored == t.stored && modifiable == t.modifiable;
        }
        return false;
    }

    /**
     * normal depot constructor.
     */
    public Depot(int maxToStore, boolean modifiable) throws InvalidArgumentException {
        if (maxToStore <= 0) throw new InvalidArgumentException("Max to store for depot cannot be less than 0");
        this.maxToStore = maxToStore;
        resource = null;
        stored = 0;
        this.modifiable = modifiable;
    }

    /**
     * normal depot constructor, always modifiable.
     */
    public Depot(int maxToStore) throws InvalidArgumentException {
        if (maxToStore <= 0) throw new InvalidArgumentException("Max to store for depot cannot be less than 0");
        this.maxToStore = maxToStore;
        resource = null;
        stored = 0;
        this.modifiable = true;
    }

    /**
     * leader depot constructor.
     */
    public Depot(Resource r) throws InvalidArgumentException {
        this.maxToStore = 2;
        if (!Resource.isDiscountable(r))
            throw new InvalidArgumentException("Invalid type of resource to be contained in depot");
        resource = r;
        stored = 0;
        this.modifiable = false;
    }


    public int getStored() {
        return stored;
    }

    /**
     * @return resources contained in this depot.
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

    /**
     * method that removes howMany resources from this depot.
     *
     * @param howMany
     * @throws InvalidResourceQuantityToDepotException
     */
    public void spendResources(int howMany) throws InvalidResourceQuantityToDepotException {
        if (howMany <= 0 || !enoughResources(howMany)) throw new InvalidResourceQuantityToDepotException(maxToStore);
        if (stored == howMany) clear();
        else
            stored -= howMany;
    }

    public boolean contains(Resource r) {
        return r.equals(resource);
    }

    /**
     * method that add n resources to this depot.
     */
    public void addResource(Resource r, int howMany) throws InvalidTypeOfResourceToDepotException, DifferentResourceForDepotException, InvalidResourceQuantityToDepotException {
        if (howMany == 0) return;
        if (howMany <= 0 || tooManyResources(howMany)) throw new InvalidResourceQuantityToDepotException(maxToStore);
        if (!isResourceAppendable(r)) throw new DifferentResourceForDepotException(r);
        if (!Resource.isDiscountable(r)) throw new InvalidTypeOfResourceToDepotException(r);
        if (stored != 0)
            stored += howMany;
        else {
            resource = r;
            stored = howMany;
        }
    }

    /**
     * method that checks if, adding howMany resources, this depot will became overfull.
     */
    private boolean tooManyResources(int howMany) {
        return stored + howMany > maxToStore;
    }

    public boolean isFull() {
        return stored == maxToStore;
    }

    /**
     * method that controls if the resource r can be added at the depot.
     */
    private boolean isResourceAppendable(Resource r) {
        if (modifiable)
            return isEmpty() || r.equals(resource);
        else
            return r.equals(resource);
    }

    /**
     * method that clears the depot.
     *
     * @return how many resources was stored in this depot.
     */
    public int clear() {
        int result = stored;
        if (modifiable) resource = null;
        stored = 0;
        return result;
    }

    public boolean isEmpty() {
        return stored == 0;
    }

    public boolean enoughResources(int n) {
        return stored >= n;
    }
}
