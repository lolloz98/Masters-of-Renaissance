package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedLeaderCardException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

/**
 * LeaderCard with effect of discounting the cost of Develop Cards.
 * To be activated, it requires the fulfillment of RequirementColorsDevelop.
 */
public final class DiscountLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    private final Resource res;
    private final int quantity = 1;
    private boolean hasBeenApplied = false;

    public DiscountLeaderCard(int victoryPoints, RequirementColorsDevelop requirement, Resource res, int id) {
        super(victoryPoints, requirement, id);
        this.res = res;
    }

    /**
     * @return type of resource to discount
     */
    public Resource getRes() {
        return res;
    }

    /**
     * @return amount of resources to discount
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param game current game, it can be affected by this method
     * @throws AlreadyAppliedLeaderCardException if the effect of this card has been applied and yet not removed
     */
    @Override
    public void applyEffect(Game<?> game) {
        if (hasBeenApplied) throw new AlreadyAppliedLeaderCardException();
        if (isActive()) applyEffectNoCheckOnActive(game);
    }

    /**
     * Apply discounts to the develop cards still to be sold in the game
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        hasBeenApplied = true;
        TreeMap<Color, TreeMap<Integer, DeckDevelop>> decks = game.getDecksDevelop();
        for (Color c : decks.keySet()) {
            decks.get(c).forEach((i, d) -> d.applyDiscount(res, quantity));
        }
    }

    /**
     * Remove discount for this.res from the cards still to be sold in the game
     *
     * @param game current game, it is affected by this method
     */
    @Override
    public void removeEffect(Game<?> game) {
        hasBeenApplied = false;
        if (isActive()) {
            TreeMap<Color, TreeMap<Integer, DeckDevelop>> decks = game.getDecksDevelop();
            for (Color c : decks.keySet()) {
                decks.get(c).forEach((i, d) -> d.removeDiscount(res));
            }
        }
    }
}
