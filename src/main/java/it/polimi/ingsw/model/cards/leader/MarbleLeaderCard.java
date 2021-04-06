package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;

public final class MarbleLeaderCard extends LeaderCard<RequirementColorsDevelop>{
    private final Resource targetRes;

    public MarbleLeaderCard(int victoryPoints, RequirementColorsDevelop requirement, Resource targetRes, int id) {
        super(victoryPoints, requirement, id);
        this.targetRes = targetRes;
    }

    /**
     * if active, call this.applyEffectNoCheckOnActive
     *
     * @param game current game, it can be affected by this method
     */
    @Override
    public void applyEffect(Game<?> game) {
        if(isActive()) applyEffectNoCheckOnActive(game);
    }

    /**
     * update the market with targetRes
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        // TODO: check
        game.updateMarketWithLeader(targetRes);
    }

    /**
     * if active, remove the effect of this card
     *
     * @param game current game, it can be affected by this method
     */
    @Override
    public void removeEffect(Game<?> game) {
        // TODO: check
        if (isActive())
            game.removeLeaderResFromMarket();
    }
}
