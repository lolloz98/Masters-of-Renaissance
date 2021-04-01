package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class Game <T extends Turn> {
    private boolean lastRound;
    private boolean gameOver;
    private int id;
    private MarketTray marketTray;
    private T turn;
    private TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;
    private Deck<LeaderCard> deckLeader;

    public Game(){
        // TODO initialization of develop card decks
        // TODO initialization of leader card
        this.marketTray = new MarketTray(new MarbleDispenserCollection());
    }

    public boolean isLastTurn() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public T getTurn() {
        return turn;
    }

    public void setTurn(T turn) {
        this.turn = turn;
    }

    public abstract void checkEndConditions();
    public abstract void nextTurn();

    /**
     * Draw a card from the deck of develop cards with a certain color and level
     *
     * @param color color of the card to draw
     * @param level level of the card to draw
     * @return the drawn card
     * @throws EmptyDeckException if deck is empty
     */
    public DevelopCard drawDevelopCard(Color color, int level){
        return decksDevelop.get(color).get(level).drawCard();
    }

    /**
     * Use the market tray to get resources
     *
     * @param index index of the matrix that indicates where to push the marble
     * @param onRow if true pushes on the row (from right), if false pushes on the column (from the bottom)
     * @return an ArrayList containing the possible combinations of resources that the player can get, based on the leader cards he has activated
     * @throws MatrixIndexOutOfBoundException the combination of onRow and index is not valid
     */
    public ArrayList<TreeMap<Resource, Integer>> useMarketTray(boolean onRow, int index) throws MatrixIndexOutOfBoundException {
        return marketTray.pushMarble(onRow, index);
    }
}
