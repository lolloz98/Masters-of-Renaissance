package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.game.Resource;

public class Depot {
    private Resource resource;
    private final int maxToStore;
    private int stored;

    public Depot(int maxToStore) {
        this.maxToStore = maxToStore;
        resource=Resource.NOTHING;
        stored=0;
    }

    public void addResource(Resource r){
        if(!isResourceAppendable(r)) throw new InvalidResourceForDepotExeption();
        if(isFull()) throw new DepotFullExeption();
        //TODO
    }

    public boolean isFull(){
        return stored==maxToStore;
    }

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

    public int freeSpace(){
        return maxToStore-stored;
    }

    public Resource getTypeOfResource(){
        return resource;
    }


}
