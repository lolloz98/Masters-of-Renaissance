package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedDiscountForResException;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedLeaderCardException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * LeaderCard with effect of discounting the cost of Develop Cards.
 * To be activated, it requires the fulfillment of RequirementColorsDevelop.
 */
public final class DiscountLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    private static final Logger logger = LogManager.getLogger(DiscountLeaderCard.class);
    private static final long serialVersionUID = 1001L;

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
    public void applyEffect(Game<?> game) throws AlreadyAppliedLeaderCardException, AlreadyAppliedDiscountForResException {
        if (hasBeenApplied) throw new AlreadyAppliedLeaderCardException();
        if (isActive()) applyEffectNoCheckOnActive(game);
    }

    /**
     * Apply discounts to the develop cards still to be sold in the game
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) throws AlreadyAppliedDiscountForResException {
        hasBeenApplied = true;
        TreeMap<Color, TreeMap<Integer, DeckDevelop>> decks = game.getDecksDevelop();
        for (Color c : decks.keySet()) {
            for (Map.Entry<Integer, DeckDevelop> entry : decks.get(c).entrySet()) {
                DeckDevelop d = entry.getValue();
                try {
                    d.applyDiscount(res, quantity);
                } catch (ResourceNotDiscountableException e) {
                    logger.error("Resource specified by leader is not discountable");
                }
            }
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
