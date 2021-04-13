package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.model.exception.ActivateDiscardedCardException;
import it.polimi.ingsw.model.exception.AlreadyActiveLeaderException;
import it.polimi.ingsw.model.exception.RequirementNotSatisfiedException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WarehouseType;

import java.util.TreeMap;

public abstract class LeaderCard<T extends Requirement> implements Card, VictoryPointCalculator {
    private final int victoryPoints;
    private final T requirement;
    private boolean isActive;
    private boolean isDiscarded;
    private final int id;

    public LeaderCard(int victoryPoints, T requirement, int id) {
        this.isActive = false;
        this.isDiscarded = false;
        this.victoryPoints = victoryPoints;
        this.requirement = requirement;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * set isActive to true and use the effect of the card
     * and remove from the board the resources toPay
     *
     * @param game   current game, it is affected by this method
     * @param player player who activates the card, it can be affected by this method
     * @param toPay resources to be paid to activate the card
     * @throws RequirementNotSatisfiedException if the requirement for this card was not satisfied
     * @throws AlreadyActiveLeaderException     if the card was already active
     * @throws ActivateDiscardedCardException   if the card was discarded
     * @throws IllegalArgumentException         if this is not owned by the player (-> not contained in the board of the player)
     */
    public void activate(Game<?> game, Player player, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws RequirementNotSatisfiedException, AlreadyActiveLeaderException, ActivateDiscardedCardException, IllegalArgumentException {
        if (!checkRequirement(player, toPay)) throw new RequirementNotSatisfiedException();
        if (isActive) throw new AlreadyActiveLeaderException();
        if (isDiscarded) throw new ActivateDiscardedCardException();
        if (!player.getBoard().getLeaderCards().contains(this)) throw new IllegalArgumentException();

        isActive = true;
        player.getBoard().payResources(toPay);
        applyEffectNoCheckOnActive(game);
    }

    /**
     * set isActive to true and use the effect of the card
     *
     * @param game   current game, it is affected by this method
     * @param player player who activates the card, it can be affected by this method
     * @throws RequirementNotSatisfiedException if the requirement for this card was not satisfied
     * @throws AlreadyActiveLeaderException     if the card was already active
     * @throws ActivateDiscardedCardException   if the card was discarded
     * @throws IllegalArgumentException         if this is not owned by the player (-> not contained in the board of the player)
     */
    public void activate(Game<?> game, Player player) throws RequirementNotSatisfiedException, AlreadyActiveLeaderException, ActivateDiscardedCardException, IllegalArgumentException {
        if(!getResourcesToBePaidForActivation().isEmpty()) throw new RequirementNotSatisfiedException();
        activate(game, player, new TreeMap<>());
    }

    public void discard() {
        isDiscarded = true;
    }

    /**
     * if the card isActive do the effect of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void applyEffect(Game<?> game);

    /**
     * apply effect of this card without checking if it is active
     *
     * @param game current game: it is affected by this method
     */
    protected abstract void applyEffectNoCheckOnActive(Game<?> game);

    /**
     * if the card isActive remove the effects of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void removeEffect(Game<?> game);

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    /**
     * @param player player on whom to check the requirements
     * @param toPay resources to be paid (if none are required, it must be an empty TreeMap, otherwise return false)
     * @return true if the player can activate the card
     */
    public boolean checkRequirement(Player player, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) {
        return requirement.checkRequirement(player, toPay);
    }

    /**
     * @return requirement for the activation of this card
     */
    protected T getRequirement() {
        return requirement;
    }

    /**
     * @return treeMap with resources to be paid. Empty treeMap if no resources need to be paid
     */
    public TreeMap<Resource, Integer> getResourcesToBePaidForActivation(){
        return requirement.getResourcesToBePaid();
    }
}
