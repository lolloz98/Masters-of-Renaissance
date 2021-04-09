package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.SinglePlayer;

/**
 * LeaderCard with effect of creating a new Production in the board of the player.
 * To be activated, it requires the fulfillment (and payment) of RequirementLevelDevelop.
 */
public final class ProductionLeaderCard extends LeaderCard<RequirementLevelDevelop> {
    private final Production production;

    public ProductionLeaderCard(int victoryPoints, RequirementLevelDevelop requirement, Production production, int id) {
        super(victoryPoints, requirement, id);
        this.production = production;
    }

    /**
     * This method does nothing on this Leader
     *
     * @param game current game
     */
    @Override
    public void applyEffect(Game<?> game) {
        // the effect of this card is applied only on activation. this method does nothing
    }

    /**
     * Add this Leader to the board productionLeaders
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        if ((game instanceof SinglePlayer)) {
            ((SinglePlayer) game).getPlayer().getBoard().discoverProductionLeader(this);
        } else {
            ((MultiPlayer) game).getTurn().getCurrentPlayer().getBoard().discoverProductionLeader(this);
        }
    }

    /**
     * This method does nothing on this Leader
     *
     * @param game current game
     */
    @Override
    public void removeEffect(Game<?> game) {
        // On this type of leader removeEffect does nothing
    }

    public Production getProduction() {
        return production;
    }
}
