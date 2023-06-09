package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.exception.AlreadyAppliedLeaderCardException;
import it.polimi.ingsw.server.model.exception.AlreadyPresentLeaderResException;
import it.polimi.ingsw.server.model.exception.NoSuchResourceException;
import it.polimi.ingsw.server.model.exception.TooManyLeaderResourcesException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.enums.Resource;

/**
 * LeaderCard with effect of transforming the Marbles of type Resource.NOTHING in the market.
 * To be activated, it requires the fulfillment of RequirementColorsDevelop.
 */
public final class MarbleLeaderCard extends LeaderCard<RequirementColorsDevelop> {
    private static final long serialVersionUID = 1003L;

    private final Resource targetRes;
    private boolean hasBeenApplied = false;

    public MarbleLeaderCard(int victoryPoints, RequirementColorsDevelop requirement, Resource targetRes, int id) {
        super(victoryPoints, requirement, id);
        this.targetRes = targetRes;
    }

    /**
     * if active, call this.applyEffectNoCheckOnActive
     *
     * @param game current game, it can be affected by this method
     * @throws AlreadyAppliedLeaderCardException if the effect of this card has been applied and yet not removed
     */
    @Override
    public void applyEffect(Game<?> game) throws AlreadyAppliedLeaderCardException, AlreadyPresentLeaderResException, TooManyLeaderResourcesException {
        if (hasBeenApplied) throw new AlreadyAppliedLeaderCardException();
        if (isActive()) applyEffectNoCheckOnActive(game);
    }

    /**
     * update the market with targetRes
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) throws AlreadyPresentLeaderResException, TooManyLeaderResourcesException {
        hasBeenApplied = true;
        game.getMarketTray().addLeaderResource(targetRes);
    }

    /**
     * if active, remove the effect of this card
     *
     * @param game current game, it can be affected by this method
     */
    @Override
    public void removeEffect(Game<?> game) throws NoSuchResourceException {
        hasBeenApplied = false;
        if (isActive())
            game.getMarketTray().removeLeaderResource(targetRes);
    }

    public Resource getTargetRes() {
        return targetRes;
    }
}
