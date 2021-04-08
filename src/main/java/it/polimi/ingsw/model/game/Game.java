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
    private static Set<Integer> usedId = Collections.synchronizedSet(new TreeSet<>());
    private boolean gameOver;
    private final int id;
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
        this.id = getNewId();
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

    public int getId() {
        return id;
    }

    public MarketTray getMarketTray() {
        return marketTray;
    }

    public abstract void checkEndConditions();

    public abstract void nextTurn();

    public abstract void distributeLeader();

    /**
     * @return the value of an id that doesn't correspond to any of the currently ongoing games
     */
    private synchronized static int getNewId(){
        int i = 0;
        while (usedId.contains(i)) {
            i++;
        }
        usedId.add(i);
        return i;
    }

    /**
     * Removes the id from the list of used ids
     */
    private synchronized static void removeId(int id){
        usedId.remove(id);
    }

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
            path = String.format("json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DiscountLeaderCard.class));
        }
        for(i = 53; i < 57; i++) {
            path = String.format("json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));
        }
        for(i = 57; i < 61; i++) {
            path = String.format("json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), MarbleLeaderCard.class));
        }
        for(i = 61; i < 64; i++) {
            path = String.format("json_file/cards/leader/%03d.json", i);
            leaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), ProductionLeaderCard.class));
        }
        this.deckLeader = new Deck<LeaderCard<? extends Requirement>>(leaderCards);
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

    /**
     * Adds a resource to the leaderResources in marketTray
     *
     * @param resource is the resource to add
     * @throws TooManyLeaderResourcesException if there are already 2 resource in the list
     */
    public void updateMarketWithLeader(Resource resource) {
        marketTray.addLeaderResource(resource);
    }

    /**
     * Removes all leaderResources from the marketTray
     */
    public void removeLeaderResFromMarket() {
        marketTray.removeLeaderResources();
    }

    /**
     * Checks if one of the develop decks is empty
     */
    public boolean isADeckDevelopEmpty(){
        for(Color color : Color.values())
            for(int level = 1; level < 4; level++)
                if (decksDevelop.get(color).get(level).isEmpty()) return true;
        return false;
    }

    /**
     * Destroy method
     */
    public void destroy() {
        removeId(this.id);
    }

    /**
     * Helper method to distribute
     */
    protected void distributeLeaderToPlayer(Player player){
        player.getBoard().addLeaderCards(deckLeader.distributeCards(4));
    }
}
