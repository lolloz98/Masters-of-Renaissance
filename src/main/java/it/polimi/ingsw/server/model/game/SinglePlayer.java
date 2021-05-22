package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.cards.lorenzo.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Player;
import java.util.ArrayList;

/**
 * Concrete extension of the class Game. It has all the variables and methods needed to represent the state of a SinglePlayer game.
 */

public class SinglePlayer extends Game<TurnSingle>{
    private static final long serialVersionUID = 1023L;

    private final Player player;
    private final Lorenzo lorenzo;
    private LorenzoDeck lorenzoDeck;
    private boolean lastTurn;
    private boolean hasPlayerWon;
    private int playerPoints;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SinglePlayer) {
            SinglePlayer t = (SinglePlayer) obj;
            return super.equals(obj) && player.equals(t.player) && lorenzo.equals(t.lorenzo)
                    && lorenzoDeck.equals(t.lorenzoDeck) && lastTurn == t.lastTurn
                    && hasPlayerWon == t.hasPlayerWon && playerPoints == t.playerPoints;
        }
        return false;
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    private void setLastTurn() {
        this.lastTurn = true;
    }

    public boolean getHasPlayerWon() {
        return hasPlayerWon;
    }

    public void setHasPlayerWon(boolean playerWon) {
        this.hasPlayerWon = playerWon;
    }

    public int getPlayerPoints() {
        return playerPoints;
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

    public SinglePlayer(Player player) throws EmptyDeckException, WrongColorDeckException, WrongLevelDeckException {
        super();
        this.turn = new TurnSingle(false);
        this.player = player;
        this.lorenzo = new Lorenzo();
        this.hasPlayerWon = false;
        this.playerPoints = 0;
        createLorenzoDeck();
    }

    /**
     * Setup method for the Lorenzo Deck.
     */
    private void createLorenzoDeck() throws EmptyDeckException {
        ArrayList<LorenzoCard> lorenzoCards = new ArrayList<>();
        lorenzoCards.add(new DevelopLorenzoCard(65, Color.GOLD));
        lorenzoCards.add(new DevelopLorenzoCard(66, Color.PURPLE));
        lorenzoCards.add(new DevelopLorenzoCard(67, Color.BLUE));
        lorenzoCards.add(new DevelopLorenzoCard(68, Color.GREEN));
        lorenzoCards.add(new FaithLorenzoCard(69));
        lorenzoCards.add(new ReshuffleLorenzoCard(70));
        this.lorenzoDeck = new LorenzoDeck(lorenzoCards);
    }

    @Override
    public Player getPlayer(int playerId) throws InvalidArgumentException {
        if(this.getPlayer().getPlayerId()!=playerId) throw new InvalidArgumentException("the player does not have id: " + playerId);
        return this.getPlayer();
    }

    /**
     * Checks if end condition is met. If it is, the lastTurn is set to true, nextTurn() in Turn will return null and the Game will end.
     */
    @Override
    public void checkEndConditions(){
        if((player.getBoard().getDevelopCardSlots().stream().mapToInt(x -> x.getCards().size()).sum() >= 6)
                || player.getBoard().getFaithtrack().isEndReached()){
            setLastTurn();
            setHasPlayerWon(true);
        }
        if(this.isAColorEmpty() || lorenzo.getFaithTrack().isEndReached()){
            setLastTurn();
        }
    }

    /**
     * Method that changes the turn. If the next turn is null, it means that the game is over.
     */
    @Override
    public void nextTurn() throws GameIsOverException, MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        if (isGameOver()) throw new GameIsOverException();
        TurnSingle turn = getTurn().nextTurn(this);
        if (!turn.getIsPlayable()){
            setGameOver(true);
            this.playerPoints = player.getBoard().getVictoryPoints();
        }
        else setTurn(turn);
    }

    /**
     * Distribute 4 cards to the players
     */
    @Override
    public void distributeLeader() throws EmptyDeckException {
        distributeLeaderToPlayer(player);
    }
}
