package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;

/**
 * LeaderCard with effect of creating a new Production in the board of the player.
 * To be activated, it requires the fulfillment (and payment) of RequirementLevelDevelop.
 */
public final class ProductionLeaderCard extends LeaderCard<RequirementLevelDevelop> {
    private static final long serialVersionUID = 1004L;

    private final Production production;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getWhichProd() {
        return whichProd;
    }

    private int whichProd = -1;

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
        Board board;
        if ((game instanceof SinglePlayer)) {
            board = ((SinglePlayer) game).getPlayer().getBoard();
        } else {
            board = ((MultiPlayer) game).getTurn().getCurrentPlayer().getBoard();
        }
        board.discoverProductionLeader(this);
        whichProd = board.getProductionLeaders().size() + 4;
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
