package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Depot;

/**
 * LeaderCard with effect of creating a new Depot in the board of the player.
 * To be activated, it requires the fulfillment (and payment) of RequirementResource.
 */
public final class DepotLeaderCard extends LeaderCard<RequirementResource> {
    private static final long serialVersionUID = 1000L;
    private final Depot depot;

    public DepotLeaderCard(int victoryPoints, RequirementResource requirement, Depot depot, int id) {
        super(victoryPoints, requirement, id);
        this.depot = depot;
    }

    /**
     * This method does nothing on this Leader
     *
     * @param game current game
     */
    @Override
    public void applyEffect(Game<?> game) {
        // It does nothing on this type of leader
    }

    /**
     * Add this Leader to the board depotLeaders
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        Board board =
                ((game instanceof SinglePlayer) ?
                        ((SinglePlayer) game).getPlayer() : ((MultiPlayer) game).getTurn().getCurrentPlayer())
                        .getBoard();

        board.discoverDepotLeader(this);
    }

    /**
     * This method does nothing on this Leader
     *
     * @param game current game
     */
    @Override
    public void removeEffect(Game<?> game) {
        // It does nothing on this type of leader
    }

    public Depot getDepot() {
        return depot;
    }
}
