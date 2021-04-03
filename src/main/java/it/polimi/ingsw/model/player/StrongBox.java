package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.InvalidResourcesToAddToStrongBoxException;
import it.polimi.ingsw.model.exception.NotEnoughResourcesExeption;
import it.polimi.ingsw.model.game.Resource;

import java.util.TreeMap;

//class that models the lockbox of the player

public class StrongBox {
    private TreeMap<Resource,Integer> resources;

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
        TreeMap<Resource, Integer> StrongBoxContent=new TreeMap<>();
        StrongBoxContent.putAll(resources);
        return StrongBoxContent;
    }

    //method that add discountableresources(==GOLD,SHIELD,ROCK,SERVANT) to the strongbox
    public void addResources(TreeMap<Resource,Integer> resGained){
        for(Resource r: resGained.keySet()){
            if(resGained.get(r)<0) throw new InvalidResourcesToAddToStrongBoxException();
            if(Resource.isDiscountable(r))
                resources.replace(r,resources.get(r)+resGained.get(r));
        }
    }

    //method that subtract the resources to the strongbox
    public void spendResources(TreeMap<Resource,Integer> resToSpend){
        if(!hasResources(resToSpend)) throw new NotEnoughResourcesExeption();
        for(Resource r: resToSpend.keySet()){
            if(resToSpend.get(r)<0) throw new InvalidResourcesToAddToStrongBoxException();
            resources.replace(r,resources.get(r)-resToSpend.get(r));
        }
    }

    private boolean hasResources(TreeMap<Resource,Integer> resToSpend){
        for(Resource r: resToSpend.keySet()) {
            if (resources.get(r) < resToSpend.get(r))
                return false;
        }
        return true;
    }
}

