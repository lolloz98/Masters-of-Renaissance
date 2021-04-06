package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.lorenzo.*;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

/**
 * Concrete extension of the class Game. It has all the variables and methods needed to represent the state of a SinglePlayer game.
 */

public class SinglePlayer extends Game<TurnSingle>{
    private final Player player;
    private final Lorenzo lorenzo;
    private LorenzoDeck lorenzoDeck;
    private boolean lastTurn;
    private boolean hasPlayerWon;
    private int playerPoints;

    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    public boolean getHasPlayerWon() {
        return hasPlayerWon;
    }

    public void setHasPlayerWon(boolean playerWon) {
        this.hasPlayerWon = playerWon;
    }

    public SinglePlayer(Player player) {
        super();
        this.player = player;
        this.lorenzo = new Lorenzo();
        this.hasPlayerWon = false;
        this.playerPoints = 0;
        createLorenzoDeck();
    }

    public LorenzoDeck getLorenzoDeck() {
        return lorenzoDeck;
    }

    public Player getPlayer() {
        return player;
    }

    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    /**
     * Setup method for the Lorenzo Deck.
     */
    public void createLorenzoDeck(){
        ArrayList<LorenzoCard> lorenzoCards = new ArrayList<>();
        lorenzoCards.add(new DevelopLorenzoCard(65, Color.GOLD));
        lorenzoCards.add(new DevelopLorenzoCard(66, Color.PURPLE));
        lorenzoCards.add(new DevelopLorenzoCard(67, Color.BLUE));
        lorenzoCards.add(new DevelopLorenzoCard(68, Color.GREEN));
        lorenzoCards.add(new FaithLorenzoCard(69));
        lorenzoCards.add(new ReshuffleLorenzoCard(70));
        this.lorenzoDeck = new LorenzoDeck(lorenzoCards);
    }

    /**
     * Checks if end condition is met. If it is, the lastTurn is set to true,
     * nextTurn() in Turn will return null and the Game will end.
     */
    @Override
    public void checkEndConditions(){
        if((player.getBoard().getDevelopCardSlots().stream().mapToInt(x -> x.getCards().size()).sum() >= 6)
            || player.getBoard().getFaithtrack().isEndReached()){
            setLastTurn(true);
            setHasPlayerWon(true);
        }
        if(this.isADeckDevelopEmpty() || lorenzo.getFaithTrack().isEndReached()){
            setLastTurn(true);
        }
    }

    /**
     * Method that changes the turn. If the next turn is null, it means that the game is over.
     */
    @Override
    public void nextTurn(){
        TurnSingle turn = getTurn().nextTurn(this);
        if (turn == null){
            setGameOver(true);
            this.playerPoints = player.getBoard().getVictoryPoints();
        }
        else setTurn(turn);
    }
}
