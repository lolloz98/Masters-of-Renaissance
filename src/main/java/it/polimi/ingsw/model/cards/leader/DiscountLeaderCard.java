package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.TreeMap;

public final class DiscountLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    private final Resource res;
    private final int quantity = 1;

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

    @Override
    public void applyEffect(Game<?> game) {
        if (isActive()) applyEffectNoCheckOnActive(game);
    }

    /**
     * Apply discounts to the develop cards still to be sold in the game
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        // TODO: check
        TreeMap<Color, TreeMap<Integer, DeckDevelop>> decks = game.getDecksDevelop();
        for (Color c : decks.keySet()) {
            decks.get(c).forEach((i, d) -> d.applyDiscount(res, quantity));
        }
    }

    /**
     * If any, remove discounts from the cards still to be sold in the game
     *
     * @param game current game, it is affected by this method
     */
    @Override
    public void removeEffect(Game<?> game) {
        // TODO: check
        if (isActive()) {
            TreeMap<Color, TreeMap<Integer, DeckDevelop>> decks = game.getDecksDevelop();
            for (Color c : decks.keySet()) {
                decks.get(c).forEach((i, d) -> d.removeDiscounts());
            }
        }
    }
}
