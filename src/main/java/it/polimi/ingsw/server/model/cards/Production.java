package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.model.utility.Utility;

import java.io.Serializable;
import java.util.TreeMap;

public class Production implements Serializable {
    private static final long serialVersionUID = 1015L;

    private final TreeMap<Resource, Integer> resourcesToGive;
    private final TreeMap<Resource, Integer> resourcesToGain;
    private final TreeMap<Resource, Integer> gainedResources;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Production){
            Production t = (Production) obj;
            return resourcesToGive.equals(t.resourcesToGive) && resourcesToGain.equals(t.resourcesToGain)
                    && gainedResources.equals(t.gainedResources);
        }
        return false;
    }

    public Production(TreeMap<Resource, Integer> resourcesToGive, TreeMap<Resource, Integer> resourcesToGain) {
        this.resourcesToGain = new TreeMap<>(resourcesToGain);
        this.resourcesToGive = new TreeMap<>(resourcesToGive);
        this.gainedResources = new TreeMap<>();
    }

    public TreeMap<Resource, Integer> whatResourceToGive() {
        return new TreeMap<>(resourcesToGive);
    }

    public TreeMap<Resource, Integer> whatResourceToGain() {
        return new TreeMap<>(resourcesToGain);
    }

    /**
     * Remove toPay from board and return the gained resources
     *
     * @param toPay           resource that the player wants to pay to apply the production
     * @param resourcesToGain resource that the player wants to gain after the application production
     * @param board           board of the player
     * @throws ProductionAlreadyActivatedException if the production has already been activated in this turn
     * @throws InvalidResourcesByPlayerException   if toPay or resourcesToGain contain invalid type of Resources
     * @throws NotEnoughResourcesException         if there are not enough resources topay on the board
     */
    public void applyProduction(TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay, TreeMap<Resource, Integer> resourcesToGain, Board board) throws InvalidResourcesByPlayerException, ProductionAlreadyActivatedException, ResourceNotDiscountableException, NotEnoughResourcesException, InvalidArgumentException, InvalidResourceQuantityToDepotException {
        if (!checkResToGiveForActivation(Utility.getTotalResources(toPay)))
            throw new InvalidResourcesByPlayerException("Invalid resources to pay specified");
        if (!checkResToGainForActivation(resourcesToGain)) throw new InvalidResourcesByPlayerException("Invalid resources to gain specified");
        if (hasBeenActivated()) throw new ProductionAlreadyActivatedException();

        board.payResources(toPay); // it can throw NotEnoughResourcesException

        gainedResources.putAll(resourcesToGain);
    }

    /**
     * @param byPlayer resource given byPlayer to be spent for the activation of the production
     * @return true if byPlayer is ok for the activation of the production
     * @throws InvalidResourcesByPlayerException if byPlayer contains invalid type of Resources
     */
    public boolean checkResToGiveForActivation(TreeMap<Resource, Integer> byPlayer) throws InvalidResourcesByPlayerException {
        for (Resource r : byPlayer.keySet()) {
            if (!Resource.isDiscountable(r)) throw new InvalidResourcesByPlayerException("A type of resource specified is invalid");
        }
        return checkResForActivation(byPlayer, resourcesToGive);
    }

    /**
     * @param byPlayer resources that the player wants to gain after the activation of the production
     * @return true if byPlayer can be gained from this production (it is comparable to resourcesToGain)
     * @throws InvalidResourcesByPlayerException if byPlayer contains invalid type of Resources
     */
    public boolean checkResToGainForActivation(TreeMap<Resource, Integer> byPlayer) throws InvalidResourcesByPlayerException {
        for (Resource r : byPlayer.keySet()) {
            if (!Resource.isDiscountable(r) && r != Resource.FAITH) throw new InvalidResourcesByPlayerException("A type of resource specified is invalid");
        }
        return checkResForActivation(byPlayer, resourcesToGain);
    }

    /**
     * check if the resources given are ok for the activation of the production
     *
     * @param byPlayer     resources willing to be spent (or to be gained)
     * @param toBeCompared resourcesToGive or resourcesToGain by this production
     * @return true if the resources byPlayer meet the demand of toBeCompared
     */
    private boolean checkResForActivation(TreeMap<Resource, Integer> byPlayer, TreeMap<Resource, Integer> toBeCompared) {
        if (getAmountForRes(Resource.FAITH, byPlayer) != getAmountForRes(Resource.FAITH, toBeCompared)) return false;

        int surplus = getAmountForRes(Resource.ANYTHING, toBeCompared);
        for (Resource r : new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}) {
            int diff = getAmountForRes(r, byPlayer) - getAmountForRes(r, toBeCompared);
            if (diff < 0) return false;
            surplus -= diff;
        }
        return surplus == 0;
    }

    /**
     * how many resources of type r are in map
     *
     * @param r   resource
     * @param map map to look into to
     * @return number of resources required of type r
     */
    private int getAmountForRes(Resource r, TreeMap<Resource, Integer> map) {
        return (map.get(r) == null) ? 0 : map.get(r);
    }

    /**
     * @return true if the production has been activated (and has not been flushed)
     */
    public boolean hasBeenActivated() {
        return !gainedResources.isEmpty();
    }

    /**
     * @return gainedResources (empty TreeMap if the production has not been activated)
     */
    public TreeMap<Resource, Integer> getGainedResources() {
        return new TreeMap<>(gainedResources);
    }

    /**
     * Put in the board the resources gained from this production.
     * If in the gained resources there is FAITH points, then move on the faith track.
     *
     * @param board board of the player
     * @param game  current game
     */
    public void flushGainedToBoard(Board board, Game<?> game) throws ResourceNotDiscountableException, InvalidStepsException, EndAlreadyReachedException, InvalidArgumentException {
        board.flushGainedResources(gainedResources, game);
        gainedResources.clear();
    }
}
