package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.exception.InvalidDevelopCardToSlotException;
import it.polimi.ingsw.model.exception.InvalidProductionSlotChosenException;
import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DevelopCardSlotTest {
    private DevelopCardSlot slot ;
    private ArrayList<DevelopCard> developCards;
    private Board board;
    private TreeMap<Resource,Integer> toGive;
    private TreeMap<Resource,Integer> toGain;

    @Before
    public void setUp() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        developCards = new ArrayList<>();
        String path;
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 1);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 3);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 17);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 24);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 37);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 48);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));

        slot=new DevelopCardSlot();

        board=new Board();
        toGain=new TreeMap<>();
        toGive=new TreeMap<>();

        SinglePlayer sp=new SinglePlayer(new Player("First", 1));
        TreeMap<Resource,Integer> resInDepots = new TreeMap<>(){{
            put(Resource.ROCK,2);
            put(Resource.SHIELD, 2);
        }};
        board.gainResourcesSmart(resInDepots, resInDepots, sp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDevelopCardTest(){
        assertTrue(slot.isEmpty());

        slot.addDevelopCard(developCards.get(0));//adding a card of level 1
        assertFalse(slot.isEmpty());
        assertEquals(1, slot.getCards().size());
        assertEquals(developCards.get(0), slot.getCards().get(0));

        slot.addDevelopCard(developCards.get(2));//adding a card of level 2
        assertEquals(2, slot.getCards().size());
        assertEquals(developCards.get(0), slot.getCards().get(0));
        assertEquals(developCards.get(2), slot.getCards().get(1));

        slot.addDevelopCard(developCards.get(5));//adding a card of level 3
        assertEquals(3, slot.getCards().size());
        assertEquals(developCards.get(0), slot.getCards().get(0));
        assertEquals(developCards.get(2), slot.getCards().get(1));
        assertEquals(developCards.get(5), slot.getCards().get(2));

        slot.addDevelopCard(developCards.get(4));//adding the fourth card on the slot, should throws exception
    }

    @Test(expected = InvalidDevelopCardToSlotException.class)
    public void addDevelopCardExceptionTest1(){
        slot.addDevelopCard(developCards.get(0));//adding a card of level 1

        slot.addDevelopCard(developCards.get(1));//adding another card of level 1, should throws exception
    }

    @Test(expected = InvalidDevelopCardToSlotException.class)
    public void addDevelopCardExceptionTest2(){
        slot.addDevelopCard(developCards.get(5));//adding a card of level 3, should throws exception
    }

    @Test(expected = InvalidDevelopCardToSlotException.class)
    public void addDevelopCardExceptionTest3(){
        slot.addDevelopCard(developCards.get(1));//adding a card of level 1
        slot.addDevelopCard(developCards.get(4));//adding a card of level 3,should throws exception
    }

    @Test(expected = InvalidProductionSlotChosenException.class)
    public void applyProductionExceptionTest1() throws InvalidResourcesByPlayerException, InvalidProductionSlotChosenException {
        toGain=developCards.get(1).getProduction().whatResourceToGive();
        toGive=developCards.get(1).getProduction().whatResourceToGain();
        slot.applyProduction(new TreeMap<>(){{
            put(WarehouseType.NORMAL, new TreeMap<>(toGive));
        }}, toGain, board);
    }

    @Test(expected = InvalidResourcesByPlayerException.class)
    public void applyProductionExceptionTest2() throws InvalidResourcesByPlayerException, InvalidProductionSlotChosenException{
        toGain=developCards.get(1).getProduction().whatResourceToGive();
        toGive=new TreeMap<>(){{
            put(Resource.SHIELD,5);
        }};
        slot.addDevelopCard(developCards.get(1));
        slot.applyProduction(new TreeMap<>(){{
            put(WarehouseType.NORMAL, new TreeMap<>(toGive));
        }}, toGain, board);
    }

    @Test
    public void applyProductionTest() throws  InvalidProductionSlotChosenException{
        slot.addDevelopCard(developCards.get(1));//added a card of level1
        slot.addDevelopCard(developCards.get(3));//added a card of level2
        toGive=developCards.get(3).getProduction().whatResourceToGive();
        toGain=developCards.get(3).getProduction().whatResourceToGain();

        try{
            slot.applyProduction(new TreeMap<>(){{
                put(WarehouseType.NORMAL, new TreeMap<>(toGive));
            }}, toGain, board);
        }catch (InvalidResourcesByPlayerException e){
            fail();
        }

        assertFalse(slot.getCards().get(0).getProduction().hasBeenActivated());
        assertTrue(slot.getCards().get(1).getProduction().hasBeenActivated());
    }
}