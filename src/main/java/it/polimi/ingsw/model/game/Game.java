package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.leader.*;
import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.exception.LevelOutOfBoundException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;
import it.polimi.ingsw.model.exception.TooManyLeaderResourcesException;
import it.polimi.ingsw.model.player.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class which represents the state of the game. It's abstract as the game must be either SinglePlayer or MultiPlayer.
 */

public abstract class Game <T extends Turn> {
    private boolean gameOver;
    private final MarketTray marketTray;
    protected T turn;
    private TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;
    private Deck<LeaderCard<? extends Requirement>> deckLeader;

    public Game(){
        try {
            loadDecksDevelop();
            loadDeckLeader();
        }
        catch (FileNotFoundException e){
            // TODO
        }
        this.marketTray = new MarketTray(new MarbleDispenserCollection());
        this.gameOver = false;
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

    public Deck<LeaderCard<? extends Requirement>> getDeckLeader() {
        return deckLeader;
    }

    public MarketTray getMarketTray() {
        return marketTray;
    }

    public abstract void checkEndConditions();

    public abstract void nextTurn();

    public abstract void distributeLeader();

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
            path = String.format("src/main/resources/json_file/cards/develop/%03d.json", i);
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
     * Setup method for the Leader Deck in the game, loading the cards from the json folder, using gson library.
     *
     * @throws FileNotFoundException if there the files are not present
     */
    private void loadDeckLeader() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ArrayList<LeaderCard<? extends Requirement>> leaderCards = new ArrayList<>();
        String path;
        int i;
        for(i = 49; i < 53; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DiscountLeaderCard.class));
        }
        for(i = 53; i < 57; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));
        }
        for(i = 57; i < 61; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), MarbleLeaderCard.class));
        }
        for(i = 61; i < 65; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), ProductionLeaderCard.class));
        }
        this.deckLeader = new Deck<>(leaderCards);
        this.deckLeader.shuffle();
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
        if(level<1 || level>3) throw new LevelOutOfBoundException();
        return decksDevelop.get(color).get(level).drawCard();
    }

    /**
     * @return if one of the develop decks is empty
     */
    public boolean isADeckDevelopEmpty(){
        for(Color color : Color.values())
            for(int level = 1; level < 4; level++)
                if (decksDevelop.get(color).get(level).isEmpty()) return true;
        return false;
    }

    /**
     * Helper method to distribute leaders
     *
     * @param player to distribute the cards to
     */
    protected void distributeLeaderToPlayer(Player player){
        player.getBoard().addLeaderCards(deckLeader.distributeCards(4));
    }
}
