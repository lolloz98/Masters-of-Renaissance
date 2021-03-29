package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Depot;
import it.polimi.ingsw.model.player.Player;

public final class DepotLeaderCard extends LeaderCard<RequirementResource> {
    private final Depot depot;

    public DepotLeaderCard(int victoryPoints, RequirementResource requirement, Depot depot) {
        super(victoryPoints, requirement);
        this.depot = depot;
    }

    @Override
    public void applyEffect(Game game) {
        // It does nothing on this type of leader
    }

    @Override
    protected void applyEffectNoCheckOnActive(Game game) {
        // TODO: add a new depot to the player.
    }

    @Override
    public void activate(Game game, Player player) {
        super.activate(game, player);
        // TODO: remove the due resources from the playerBoard
    }

    @Override
    public void removeEffect(Game game) {
        // It does nothing on this type of leader
    }

    public Depot getDepot(){
        return depot;
    }
}
