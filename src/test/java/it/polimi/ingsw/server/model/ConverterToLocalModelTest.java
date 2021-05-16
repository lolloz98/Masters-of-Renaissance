package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Depot;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.model.utility.Utility;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ConverterToLocalModelTest {
    private Gson gson;

    @Before
    public void setUp(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public void checkProduction(Production p, LocalProduction lp){
        assertEquals(p.getGainedResources(), lp.getResToFlush());
        assertEquals(p.whatResourceToGain(), lp.getResToGain());
        assertEquals(p.whatResourceToGive(), lp.getResToGive());
    }

    public void checkDevelopCard(DevelopCard dc, LocalDevelopCard ld){
        checkProduction(dc.getProduction(), ld.getProduction());
        assertEquals(dc.getColor(), ld.getColor());
        assertEquals(dc.getCost(), ld.getCost());
        assertEquals(dc.getId(), ld.getId());
        assertEquals(dc.getLevel(), ld.getLevel());
        assertEquals(dc.getVictoryPoints(), ld.getVictoryPoints());
    }

    public void checkDepots(ArrayList<Depot> ds, TreeMap<Integer, Resource> lds){
        TreeMap<Integer, Resource> tmp = new TreeMap<>();
        for(Depot d: ds){
            tmp.put(d.getStored(), d.getTypeOfResource());
        }
        assertEquals(tmp, lds);
    }

    public void checkLeaderCard(LeaderCard<?> l, LocalLeaderCard ll){
        assertEquals(l.getId(), ll.getId());
        assertEquals(l.isActive(), ll.isActive());
        assertEquals(l.isDiscarded(), ll.isDiscarded());
        assertEquals(l.getVictoryPoints(), ll.getVictoryPoints());
        // not complete, check the requirements as well
        if(l instanceof DepotLeaderCard){
            assertEquals(LocalDepotLeader.class, ll.getClass());
            DepotLeaderCard tmp = (DepotLeaderCard) l;
            LocalDepotLeader lTmp = (LocalDepotLeader) ll;
            checkDepots(new ArrayList<>(){{add(tmp.getDepot());}}, new TreeMap<>(){{
                put(lTmp.getNumberOfRes(), lTmp.getResType());
            }});
        } else if(l instanceof DiscountLeaderCard){
            assertEquals(LocalDiscountLeader.class, ll.getClass());
            DiscountLeaderCard tmp = (DiscountLeaderCard) l;
            LocalDiscountLeader lTmp = (LocalDiscountLeader) ll;
            assertEquals(tmp.getRes(), lTmp.getDiscountedRes());
            assertEquals(tmp.getQuantity(), lTmp.getQuantityToDiscount());
        } else if(l instanceof ProductionLeaderCard){
            assertEquals(LocalProductionLeader.class, ll.getClass());
            ProductionLeaderCard tmp = (ProductionLeaderCard) l;
            LocalProductionLeader lTmp = (LocalProductionLeader) ll;
            checkProduction(tmp.getProduction(), lTmp.getProduction());
        } else if(l instanceof MarbleLeaderCard){
            assertEquals(LocalMarbleLeader.class, ll.getClass());
            MarbleLeaderCard tmp = (MarbleLeaderCard) l;
            LocalMarbleLeader lTmp = (LocalMarbleLeader) ll;
            assertEquals(tmp.getTargetRes(), lTmp.getMarbleResource());
        }
    }

    @Test
    public void testConvertDevelopCard() throws FileNotFoundException {
        for(int i = 1; i < 49; i++) {
            String path = String.format("src/main/resources/json_file/cards/develop/%03d.json", i);
            DevelopCard dc = gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class);
            LocalDevelopCard ld = ConverterToLocalModel.convert(dc);
            checkDevelopCard(dc, ld);
        }
    }

    @Test
    public void testConvertProduction() throws ModelException {
        Production p = createProductionApplied();
        checkProduction(p, ConverterToLocalModel.convert(p));
    }

    @Test
    public void testConvertDevelopDecks() throws ModelException {
        SinglePlayer singlePlayer = new SinglePlayer(new Player("lollo", 0));
        ConverterToLocalModel.convert(singlePlayer.getDecksDevelop());
        // todo checks
    }

    @Test
    public void testConvertLeader() throws FileNotFoundException, UnexpectedControllerException {
        LeaderCard<?> ld;
        String path;
        int i;
        for(i = 49; i < 53; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            ld = gson.fromJson(new JsonReader(new FileReader(path)), DiscountLeaderCard.class);
            checkLeaderCard(ld, ConverterToLocalModel.convert(ld));
        }
        for(i = 53; i < 57; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            ld = gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class);
            checkLeaderCard(ld, ConverterToLocalModel.convert(ld));
        }
        for(i = 57; i < 61; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            ld = gson.fromJson(new JsonReader(new FileReader(path)), MarbleLeaderCard.class);
            checkLeaderCard(ld, ConverterToLocalModel.convert(ld));
        }
        for(i = 61; i < 65; i++) {
            path = String.format("src/main/resources/json_file/cards/leader/%03d.json", i);
            ld = gson.fromJson(new JsonReader(new FileReader(path)), ProductionLeaderCard.class);
            checkLeaderCard(ld, ConverterToLocalModel.convert(ld));
        }
    }

    //  todo: test all the convert methods

    public Production createProductionApplied() throws ModelException {
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

        TreeMap<WarehouseType, TreeMap<Resource, Integer>> chosenByPlayerGive = new TreeMap<>(){
            {
                put(WarehouseType.STRONGBOX,new TreeMap<>() {{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 1);
                    put(Resource.SERVANT, 2);
                }});
            }};


        TreeMap<Resource, Integer> chosenByPlayerGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.GOLD, 1);
        }};


        MultiPlayer game = new MultiPlayer(new ArrayList<>(){{
            add(new Player("marco", 0));
            add(new Player("lorenzo", 1));
        }});


        Board board = new Board();

        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
        assertTrue(Utility.checkTreeMapEquality(new TreeMap<>(), board.getResourcesInStrongBox()));
        return production;
    }
}