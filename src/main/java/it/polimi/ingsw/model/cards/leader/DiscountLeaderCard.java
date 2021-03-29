package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

public final class DiscountLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    public DiscountLeaderCard(int victoryPoints, RequirementColorsDevelop requirement) {
        super(victoryPoints, requirement);
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
