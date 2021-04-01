package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.game.Resource;

public class Depot {
    private Resource resource;
    private final int maxToStore;
    private int stored;
    private boolean modifiable;

    public Depot(int maxToStore,boolean modifiable) {
        this.maxToStore = maxToStore;
        resource=Resource.NOTHING;
        stored=0;
        this.modifiable=modifiable;
    }

    public boolean contains(Resource r){
        return r.equals(resource);
    }

public boolean isResModifiable(){
        return modifiable;
}

//modifies the type of resource that the depot contains
public void modifyTypeOfResource(Resource type){
        if(!isResModifiable()) throw new DepotResourceModificationException();
        resource=type;
}

    //method that add n resources to the depot
    public void addResource(Resource r, int howMany){
        if(howMany<=0) throw new InvalidResourceQuantityToDepotException();
        if(!isResourceAppendable(r)) throw new DifferentResourceForDepotException();
        if(tooManyResources(howMany)) throw new TooManyResourcesToAddExeption();//TODO: unire le due eccezioni volendo
        if(!Resource.isDiscountable(r)) throw new InvalidTypeOfResourceToDepotExeption();
        if(stored!=0)
            stored++;
        else{
            resource=r;
            stored=howMany;
        }
    }

    //method that checks if the depot is overfull
    private boolean tooManyResources(int howMany){
        return stored+howMany>maxToStore;
    }

    public boolean isFull(){
        return stored==maxToStore;
    }

    //method that controls if the resource r can be added at the depot
    public boolean isResourceAppendable(Resource r){
        return isEmpty()||r.equals(resource);
    }

    public void clear(){
        resource=Resource.NOTHING;
        stored=0;
    }

    public boolean isEmpty(){
        return stored==0;
    }

    public int getFreeSpace(){
        return maxToStore-stored;
    }

    public Resource getTypeOfResource(){
        return resource;
    }


}
