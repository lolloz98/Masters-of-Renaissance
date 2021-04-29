package it.polimi.ingsw.client.localmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.exception.LevelOutOfBoundException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class LocalDevelopmentGrid extends LocalModelAbstract {
    private UI ui;
    private TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;

    public synchronized DeckDevelop getDevelopDeck(Color color, int level){
        if(level<1 || level>3) throw new LevelOutOfBoundException();
        return decksDevelop.get(color).get(level);
    }

    public synchronized void setDecksDevelop(TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop) {
        this.decksDevelop = decksDevelop;
        ui.notifyAction(this);
    }

    public LocalDevelopmentGrid(UI ui){
        this.ui=ui;
        // todo substitute with real constructor
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ArrayList<DevelopCard> developCards = new ArrayList<>();
        String path;
        for(int i = 1; i < 49; i++) {
            path = String.format("src/main/resources/json_file/cards/develop/%03d.json", i);
            try {
                developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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

}
