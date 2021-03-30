package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;

import java.util.ArrayList;
import java.util.TreeMap;

public class Game {
    private boolean isEnding;
    private int id;
    private MarketTray marketTray;
    private TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;


    public Game(){
        // initialization of develop card decks
        // initialization of leader card
    }

    public void checkEndConditions(){

    }

    public void endGame(){

    }

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
