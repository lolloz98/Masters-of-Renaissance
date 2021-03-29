package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.InvalidResourcesForProductionActivationException;
import it.polimi.ingsw.model.exception.ProductionAlreadyActivatedException;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Board;

import java.util.TreeMap;

public class Production {
    private final TreeMap<Resource, Integer> resourcesToGive;
    private final TreeMap<Resource, Integer> resourcesToGain;
    private final TreeMap<Resource, Integer> gainedResources;

    public Production(TreeMap<Resource, Integer> resourcesToGive, TreeMap<Resource, Integer> resourcesToGain){
        this.resourcesToGain = new TreeMap<>();
        this.resourcesToGive = new TreeMap<>();
        this.gainedResources = new TreeMap<>();
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
     * @param resourcesToGain resource that the player wants to gain after the application production
     * @param board board of the player
     * @throws  InvalidResourcesForProductionActivationException if resourceToGive or resourcesToGain are invalid for the activation of the production
     * @throws ProductionAlreadyActivatedException if the production has already been activated in this turn
     */
    public void applyProduction(TreeMap<Resource, Integer> resourcesToGive, TreeMap<Resource, Integer> resourcesToGain, Board board){
        if(!checkResToGiveForActivation(resourcesToGive)) throw new InvalidResourcesForProductionActivationException();
        if(!checkResToGainForActivation(resourcesToGain)) throw new InvalidResourcesForProductionActivationException();
        if(hasProductionBeenActivated()) throw new ProductionAlreadyActivatedException();
        // TODO: remove resourcesToGive from board

        gainedResources.putAll(resourcesToGain);
    }

    /**
     *
     * @param byPlayer resource given byPlayer to be spent for the activation of the production
     * @return true if byPlayer is ok for the activation of the production
     */
    public boolean checkResToGiveForActivation(TreeMap<Resource, Integer> byPlayer){
        return checkResForActivation(byPlayer, resourcesToGive);
    }

    /**
     *
     * @param byPlayer resources that the player wants to gain after the activation of the production
     * @return true if byPlayer can be gained from this production (it is comparable to resourcesToGain)
     */
    public boolean checkResToGainForActivation(TreeMap<Resource, Integer> byPlayer){
        return checkResForActivation(byPlayer, resourcesToGain);
    }

    /**
     * check if the resources given are ok for the activation of the production
     *
     * @param byPlayer resources willing to be spent (or to be gained)
     * @param toBeCompared resourcesToGive or resourcesToGain by this production
     * @return true if the resources byPlayer meet the demand of toBeCompared
     */
    private boolean checkResForActivation(TreeMap<Resource, Integer> byPlayer, TreeMap<Resource, Integer> toBeCompared){
        int surplus = getAmountForRes(Resource.ANYTHING, toBeCompared);
        for(Resource r: new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}){
            int diff = ((byPlayer.get(r) == null) ? 0 : byPlayer.get(r)) - getAmountForRes(r, toBeCompared);
            if(diff < 0) return false;
            surplus -= diff;
        }
        return surplus == 0;
    }

    /**
     * how many resources of type r are in map
     *
     * @param r resource
     * @param map map to look into to
     * @return number of resources required of type r
     */
    private int getAmountForRes(Resource r, TreeMap<Resource, Integer> map){
        return (map.get(r) == null) ? 0 : map.get(r);
    }

    /**
     * @return true if the production has been activated (and has not been flushed)
     */
    public boolean hasProductionBeenActivated(){
        return !gainedResources.isEmpty();
    }

    /**
     * @return gainedResources (empty TreeMap if the production has not been activated)
     */
    public TreeMap<Resource, Integer> getGainedResources(){
        TreeMap<Resource, Integer> copy = new TreeMap<>();
        copy.putAll(gainedResources);
        return copy;
    }

    /**
     * Put in the board the resources gained from this production
     * @param board board of the player
     */
    public void flushGainedToBoard(Board board){
        // TODO: call somethings like board.flush(gainedResources) after board implementation

        gainedResources.clear();
    }
}
