package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.source.tree.Tree;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class Game <T extends Turn> {
    private boolean gameOver;
    private int id;
    private final MarketTray marketTray;
    private T turn;
    private TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;
    private Deck<LeaderCard> deckLeader;

    public Game(){
        try {
            loadDecksDevelop();
        }
        catch (FileNotFoundException e){
            // TODO
        }
        // TODO initialization of leader card
        this.marketTray = new MarketTray(new MarbleDispenserCollection());
    }

    public TreeMap<Color, TreeMap<Integer, DeckDevelop>> getDecksDevelop() {
        return decksDevelop;
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
     * Setup method for the 12 DeckDevelop in the game, loading the cards from the json folder, using gson library.
     *
     * @throws FileNotFoundException if there the files are not present
     */
    private void loadDecksDevelop() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ArrayList<DevelopCard> developCards = new ArrayList<>();
        String path;
        for(int i = 1; i < 49; i++) {
            path = String.format("json_file/cards/develop/%03d.json", i);
            developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        }
        this.decksDevelop = new TreeMap<>(){{
            for(Color color : Color.values()) {
                put(color, new TreeMap<>() {{
                    for (int j = 1; j < 4; j++) {
                        int finalJ = j; // necessary to use j in lambda functions
                        DeckDevelop deckDevelop = new DeckDevelop(
                                developCards.stream().filter(c -> c.getColor()==color).filter(c -> c.getLevel()==finalJ).collect(Collectors.toCollection(ArrayList::new)),
                                j, color);
                        deckDevelop.shuffle();
                        put(j, deckDevelop);
                    }
                }});
            }
        }};
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
