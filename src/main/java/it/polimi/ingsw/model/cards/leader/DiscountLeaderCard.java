package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Player;

public final class DiscountLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    private final Resource res;
    private final int quantity = 1;

    public DiscountLeaderCard(int victoryPoints, RequirementColorsDevelop requirement, Resource res) {
        super(victoryPoints, requirement);
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
    public void applyEffect(Game game) {
        if(isActive()) applyEffectNoCheckOnActive(game);
    }

    @Override
    protected void applyEffectNoCheckOnActive(Game game) {
        // TODO: discount the cards in the game
    }

    @Override
    public void removeEffect(Game game) {
        // TODO: remove the discounts on the cards in the game
    }
}
