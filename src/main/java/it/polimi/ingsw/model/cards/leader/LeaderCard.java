package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.model.exception.ActivateDiscardedCardException;
import it.polimi.ingsw.model.exception.AlreadyActiveLeaderException;
import it.polimi.ingsw.model.exception.RequirementNotSatisfiedException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;

public abstract class LeaderCard<T extends Requirement> implements Card, VictoryPointCalculator {
    private final int victoryPoints;
    private final T requirement;
    private boolean isActive;
    private boolean isDiscarded;
    private final int id;

    public LeaderCard(int victoryPoints, T requirement, int id){
        this.isActive = false;
        this.isDiscarded = false;
        this.victoryPoints = victoryPoints;
        this.requirement = requirement;
        this.id = id;
    }

    @Override
    public int getId(){
        return id;
    }

    public boolean isActive(){
        return isActive;
    }

    /**
     * set isActive to true and use the effect of the card
     *
     * @param game current game, it is affected by this method
     * @param player player who activates the card, it can be affected by this method
     */
    public void activate(Game<?> game, Player player){
        if(!checkRequirement(player)) throw new RequirementNotSatisfiedException();
        if(isActive) throw new AlreadyActiveLeaderException();
        if(isDiscarded) throw new ActivateDiscardedCardException();

        isActive = true;
        applyEffectNoCheckOnActive(game);
    }

    public void discard(){
        isDiscarded = true;
    }

    /**
     * if the card isActive do the effect of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void applyEffect(Game<?> game);

    /**
     * apply effect of this card without checking if it is active
     *
     * @param game current game: it is affected by this method
     */
    protected abstract void applyEffectNoCheckOnActive(Game<?> game);

    /**
     * if the card isActive remove the effects of the card (or do nothing, it depends on the concrete card)
     *
     * @param game current game, it can be affected by this method
     */
    public abstract void removeEffect(Game<?> game);

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    /**
     * @param player player on whom to check the requirements
     * @return true if the player can activate the card
     */
    public boolean checkRequirement(Player player){
        return requirement.checkRequirement(player);
    }
}
