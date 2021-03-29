package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.game.Game;

public final class ProductionLeaderCard extends LeaderCard<RequirementLevelDevelop>{
    private final Production production;

    public ProductionLeaderCard(int victoryPoints, RequirementLevelDevelop requirement, Production production) {
        super(victoryPoints, requirement);
        this.production = production;
    }

    @Override
    public void applyEffect(Game game) {
        // the effect of this card is applied only on activation. this method does nothing
    }

    @Override
    protected void applyEffectNoCheckOnActive(Game game) {
        // TODO: add a new production on the player board
    }

    @Override
    public void removeEffect(Game game) {
        // On this type of leader removeEffect does nothing
    }

    public Production getProduction() {
        return production;
    }
}
