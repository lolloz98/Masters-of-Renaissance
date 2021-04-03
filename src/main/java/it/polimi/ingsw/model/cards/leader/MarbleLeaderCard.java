package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;

public final class MarbleLeaderCard extends LeaderCard<RequirementColorsDevelop>{
    private final Resource targetRes;

    public MarbleLeaderCard(int victoryPoints, RequirementColorsDevelop requirement, Resource targetRes, int id) {
        super(victoryPoints, requirement, id);
        this.targetRes = targetRes;
    }

    @Override
    public void applyEffect(Game<?> game) {
        if(isActive()) applyEffectNoCheckOnActive(game);
    }

    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        // TODO: apply effect on the marbles of the market tray
    }

    @Override
    public void removeEffect(Game<?> game) {
        // TODO: remove the effect on the marbles of the market tray
    }
}
