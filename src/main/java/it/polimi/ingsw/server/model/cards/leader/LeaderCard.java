package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.cards.Card;
import it.polimi.ingsw.server.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.player.Player;

public abstract class LeaderCard<T extends Requirement> implements Card, VictoryPointCalculator {
    private static final long serialVersionUID = 1002L;

    private final int victoryPoints;
    private final T requirement;
    private boolean isActive;
    private boolean isDiscarded;
    private final int id;

    /**
     * @param obj object to compare
     * @return true if obj is a card with the same id of this
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card){
            Card t = (Card) obj;
            return id == t.getId();
        }
        return false;
    }

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
     * set isActive and apply the effect
     *
     * @param game   current game, it is affected by this method
     * @param player player who activates the card, it can be affected by this method
     * @throws RequirementNotSatisfiedException if the requirement for this card was not satisfied
     * @throws AlreadyActiveLeaderException     if the card was already active
     * @throws ActivateDiscardedCardException   if the card was discarded
     * @throws InvalidArgumentException         if this is not owned by the player (-> not contained in the board of the player)
     */
    public void activate(Game<?> game, Player player) throws ModelException {
        if (!checkRequirement(player)) throw new RequirementNotSatisfiedException();
        if (isActive) throw new AlreadyActiveLeaderException();
        if (isDiscarded) throw new ActivateDiscardedCardException();
        if (!player.getBoard().getLeaderCards().contains(this)) throw new InvalidArgumentException("The leaderCard specified is not owned by the player");

        isActive = true;
        applyEffectNoCheckOnActive(game);
    }

    public void discard() {
        isDiscarded = true;
    }

    /**
     * if the card isActive do the effect of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void applyEffect(Game<?> game) throws AlreadyAppliedLeaderCardException, AlreadyAppliedDiscountForResException, AlreadyPresentLeaderResException, TooManyLeaderResourcesException;

    /**
     * apply effect of this card without checking if it is active
     *
     * @param game current game: it is affected by this method
     */
    protected abstract void applyEffectNoCheckOnActive(Game<?> game) throws AlreadyAppliedDiscountForResException, AlreadyPresentLeaderResException, TooManyLeaderResourcesException;

    /**
     * if the card isActive remove the effects of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void removeEffect(Game<?> game) throws NoSuchResourceException;

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    /**
     * @param player player on whom to check the requirements
     * @return true if the player can activate the card
     */
    public boolean checkRequirement(Player player) throws ModelException {
        return requirement.checkRequirement(player);
    }

    /**
     * @return requirement for the activation of this card
     */
    public T getRequirement() {
        return requirement;
    }
}
