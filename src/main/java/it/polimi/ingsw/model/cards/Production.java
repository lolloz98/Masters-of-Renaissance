package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.InvalidResourcesForProductionActivationException;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Board;

import java.util.TreeMap;

public class Production {
    private final TreeMap<Resource, Integer> resourcesToGive;
    private final TreeMap<Resource, Integer> resourcesToGain;

    public Production(TreeMap<Resource, Integer> resourcesToGive, TreeMap<Resource, Integer> resourcesToGain){
        this.resourcesToGain = new TreeMap<>();
        this.resourcesToGive = new TreeMap<>();
        this.resourcesToGain.putAll(resourcesToGain);
        this.resourcesToGive.putAll(resourcesToGive);
    }

    public TreeMap<Resource, Integer> whatResourceToGive(){
        TreeMap<Resource, Integer> copyToGive = new TreeMap<>();
        copyToGive.putAll(resourcesToGive);
        return copyToGive;
    }

    public TreeMap<Resource, Integer> whatResourceToGain(){
        TreeMap<Resource, Integer> copyToGain = new TreeMap<>();
        copyToGain.putAll(resourcesToGain);
        return copyToGain;
    }

    /**
     * Remove resourcesToGive from board and return the gained resources
     *
     * @param resourcesToGive resource that the player wants to give to apply the production
     * @param board board of the player
     * @return gained resources from the production
     * @throws  InvalidResourcesForProductionActivationException if resourceToGive is invalid for the activation of the production
     */
    public TreeMap<Resource, Integer> applyProduction(TreeMap<Resource, Integer> resourcesToGive, Board board){
        if(!checkResForActivation(resourcesToGive)) throw new InvalidResourcesForProductionActivationException();

        // TODO: check for availability and remove resources from board
        TreeMap<Resource, Integer> toGain = new TreeMap<>();
        toGain.putAll(resourcesToGain);
        return toGain;
    }

    /**
     * check if the resources given are ok for the activation of the production
     *
     * @param toGive resources willing to be spent
     * @return true if the production can be activated spending toGive
     */
    public boolean checkResForActivation(TreeMap<Resource, Integer> toGive){
        int surplus = getAmountToGiveForRes(Resource.ANYTHING);
        for(Resource r: new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}){
            int diff = ((toGive.get(r) == null) ? 0 : toGive.get(r)) - getAmountToGiveForRes(r);
            if(diff < 0) return false;
            surplus -= diff;
        }
        return surplus == 0;
    }

    /**
     * how many resource of type r are required for production activation
     *
     * @param r resource
     * @return number of resources required of type r
     */
    private int getAmountToGiveForRes(Resource r){
        return (resourcesToGive.get(r) == null) ? 0 : resourcesToGive.get(r);
    }
}
