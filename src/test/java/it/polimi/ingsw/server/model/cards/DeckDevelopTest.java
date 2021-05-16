package it.polimi.ingsw.server.model.cards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DeckDevelopTest {

    DeckDevelop deck1,deck2;
    ArrayList<DevelopCard> cards;

    @Before
    public void setUp() throws Exception {
        TreeMap<Resource, Integer> toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        Production production = new Production(toGive, toGain);
        cards = new ArrayList<>() {
            {
                add(new DevelopCard(new TreeMap<>() {{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 1);
                }}, production, Color.GREEN, 2, 4, 0));
            }
        };

        deck1 = new DeckDevelop(cards, 2, Color.GREEN);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ArrayList<DevelopCard> developCards = new ArrayList<>();
        String path;

        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 3);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));//"cost": "GOLD": 2
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 7);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));//"cost": {"GOLD": 1,"SERVANT": 1,"ROCK": 1 },
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 11);
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));//"cost": {"GOLD": 3 },

        deck2= new DeckDevelop(developCards, 1, Color.BLUE);
    }

    @Test
    public void testApplyDiscount1() throws ModelException {
        deck1.applyDiscount(Resource.GOLD, 1);
        while(!deck1.isEmpty()){
            DevelopCard tmp = deck1.drawCard();
            if(tmp.getCurrentCost().get(Resource.GOLD) != null) assertTrue(tmp.isDiscounted());
            else assertFalse(tmp.isDiscounted());
        }
        deck1.applyDiscount(Resource.SHIELD, 2); // Nothing happens -> no cards in deck
    }

    @Test
    public void testApplyDiscount2() throws ModelException {
        deck2.applyDiscount(Resource.SERVANT, 1);
        while(!deck2.isEmpty()){
            DevelopCard tmp = deck2.drawCard();
            if(tmp.getCurrentCost().get(Resource.SERVANT) != null) assertTrue(tmp.isDiscounted());
            else assertFalse(tmp.isDiscounted());
        }

        deck2.applyDiscount(Resource.SHIELD, 2); // Nothing happens -> no cards in deck

        deck2.applyDiscount(Resource.GOLD, 1);
        while(!deck2.isEmpty()){
            DevelopCard tmp = deck2.drawCard();
            if(tmp.getCurrentCost().get(Resource.GOLD) != null) assertTrue(tmp.isDiscounted());
            else assertFalse(tmp.isDiscounted());
        }
    }

    @Test
    public void testRemoveDiscounts1() throws ModelException {
        deck1.removeDiscounts(); // nothing happens: no discounts applied
        assertFalse(deck1.isDiscounted());
        deck1.applyDiscount(Resource.GOLD, 1);
        assertTrue(deck1.isDiscounted());
        deck1.removeDiscounts();
        assertFalse(deck1.isDiscounted());
        while(!deck1.isEmpty()){
            assertFalse(deck1.drawCard().isDiscounted());
        }
    }

    @Test
    public void testRemoveDiscounts2() throws ModelException {
        deck2.removeDiscounts(); // nothing happens: no discounts applied
        assertFalse(deck2.isDiscounted());
        deck2.applyDiscount(Resource.GOLD, 1);
        assertTrue(deck2.isDiscounted());
        deck2.removeDiscounts();
        assertFalse(deck2.isDiscounted());
        while(!deck2.isEmpty()){
            assertFalse(deck2.drawCard().isDiscounted());
        }
    }

    @Test
    public void testRemoveDiscount1() throws ModelException {
        assertFalse(deck1.isDiscounted());
        deck1.removeDiscount(Resource.GOLD); // nothing happens: no discounts applied
        assertFalse(deck1.isDiscounted());

        deck1.applyDiscount(Resource.GOLD, 1);
        assertTrue(deck1.isDiscounted());

        deck1.removeDiscount(Resource.ROCK);
        assertTrue(deck1.isDiscounted());

        deck1.removeDiscount(Resource.GOLD);
        assertFalse(deck1.isDiscounted());
    }

    @Test
    public void testRemoveDiscount2() throws ModelException {
        assertFalse(deck2.isDiscounted());
        deck2.removeDiscount(Resource.GOLD); // nothing happens: no discounts applied
        assertFalse(deck2.isDiscounted());

        deck2.applyDiscount(Resource.GOLD, 1);
        deck2.applyDiscount(Resource.SERVANT, 1);
        assertTrue(deck2.isDiscounted());

        deck2.removeDiscount(Resource.ROCK);
        assertTrue(deck2.isDiscounted());

        deck2.removeDiscount(Resource.SERVANT);
        assertTrue(deck2.isDiscounted());

        deck2.removeDiscount(Resource.GOLD);
        assertFalse(deck2.isDiscounted());
    }
}