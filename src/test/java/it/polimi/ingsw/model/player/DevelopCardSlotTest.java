package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DevelopCardSlotTest {
    private ArrayList<DevelopCard> cardsInSlot ;
    private ArrayList<DevelopCard> developCards;

    @Before
    public void setUp() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        developCards = new ArrayList<>();
        String path;
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 1);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 3);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 17);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 24);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 37);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 48);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));


    }

    @Test
    public void addDevelopCardTest1(){

    }

}