package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Board;
import it.polimi.ingsw.model.player.Depot;
import it.polimi.ingsw.model.player.Player;

import java.util.TreeMap;

public final class DepotLeaderCard extends LeaderCard<RequirementResource> {
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
     * Add this Leader to the board depotLeaders and remove from the board the resources due because of the requirement
     *
     * @param game current game: it is affected by this method
     */
    @Override
    protected void applyEffectNoCheckOnActive(Game<?> game) {
        // TODO: check
        Board board =
                ((game instanceof SinglePlayer) ?
                        ((SinglePlayer) game).getPlayer() : ((MultiPlayer) game).getTurn().getCurrentPlayer())
                        .getBoard();
        board.removeResources(new TreeMap<>() {{
            put(getRequirement().getRes(), getRequirement().getQuantity());
        }});
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
