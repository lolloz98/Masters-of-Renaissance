package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.source.tree.Tree;
import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
import it.polimi.ingsw.model.cards.leader.Requirement;
import it.polimi.ingsw.model.cards.leader.RequirementResource;
import it.polimi.ingsw.model.exception.InvalidTypeOfResourceToDepotExeption;
import it.polimi.ingsw.model.exception.TooManyResourcesToAddException;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class BoardTest {
    private ArrayList<Board> multiplayerBoards;
    private ArrayList<DevelopCard> developCards;
    private ArrayList<LeaderCard<? extends Requirement>> depotLeaderCards;
    private MultiPlayer multiPlayer;
    private Board board;

    @Before
    public void setUp() throws Exception{
        multiPlayer=new MultiPlayer(new ArrayList<>(){{
            add(new Player("first", 1));
            add(new Player("second", 2));
            add(new Player("third", 3));
            add(new Player("fourth", 4));
        }});
        board=new Board();
        multiplayerBoards=new ArrayList<>();
        for(int i=0;i<4;i++){
            multiplayerBoards.add(multiPlayer.getPlayers().get(i).getBoard());
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        developCards = new ArrayList<>();
        depotLeaderCards=new ArrayList<>();
        String path;

        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 53); //resource type: ROCK
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));
        path = String.format("src/main/resources/json_file/cards/leader/%03d.json", 55);//resource type: SHIELD
        depotLeaderCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DepotLeaderCard.class));

        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 1);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 3);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 4);//level 1
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 17);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 24);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 27);//level 2
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 37);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 46);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
        path = String.format("src/main/resources/json_file/cards/develop/%03d.json", 48);//level 3
        developCards.add(gson.fromJson(new JsonReader(new FileReader(path)), DevelopCard.class));
    }




    /**TODO:
    @Test
    public void buyDevelopCardTest(){

    }*/

    @Test(expected = InvalidTypeOfResourceToDepotExeption.class)
    public void storeInNormalDepotsExceptionTest1(){
        TreeMap<Resource,Integer> toAdd=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ANYTHING,1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsExceptionTest2(){
        TreeMap<Resource,Integer> toAdd=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SHIELD,1);
            put(Resource.SERVANT,1);
            put(Resource.ROCK,1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInNormalDepotsTest1(){
        TreeMap<Resource,Integer> toAdd=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SHIELD,1);
            put(Resource.SERVANT,1);
        }};
        board.storeInNormalDepot(toAdd);

        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD,1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.GOLD,1);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SERVANT,1);
        }}, board.getResInDepot(2));

        toAdd=new TreeMap<>(){{
            put(Resource.SHIELD,1);
            put(Resource.SERVANT,1);
        }};
        board.storeInNormalDepot(toAdd);
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.GOLD,1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD,2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SERVANT,2);
        }}, board.getResInDepot(2));

        toAdd=new TreeMap<>(){{
            put(Resource.SERVANT,1);
        }};
        board.storeInNormalDepot(toAdd);
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.GOLD,1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD,2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SERVANT,3);
        }}, board.getResInDepot(2));

        toAdd=new TreeMap<>(){{
            put(Resource.ROCK,1);
        }};
        board.storeInNormalDepot(toAdd);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInDepotLeaderExceptionTest1(){
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot

        TreeMap<Resource,Integer> toGain=new TreeMap<>(){{
            put(Resource.SERVANT, 1);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test(expected = TooManyResourcesToAddException.class)
    public void storeInDepotLeaderExceptionTest2(){
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot

        TreeMap<Resource,Integer> toGain=new TreeMap<>(){{
            put(Resource.ROCK, 3);
        }};
        board.storeInDepotLeader(toGain);
    }

    @Test
    public void storeInDepotLeaderTest1(){
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(0));//adding a ROCK depot
        board.discoverDepotLeader((DepotLeaderCard) depotLeaderCards.get(1));//adding a SHIELD depot

        assertEquals(2,board.getDepotLeaders().size());
        assertEquals(Resource.ROCK,board.getDepotLeaders().get(0).getDepot().getTypeOfResource());
        assertEquals(Resource.SHIELD,board.getDepotLeaders().get(1).getDepot().getTypeOfResource());

        TreeMap<Resource,Integer> toGain=new TreeMap<>(){{
            put(Resource.ROCK, 1);
            put(Resource.SHIELD,2);
        }};
        board.storeInDepotLeader(toGain);

        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.ROCK, 1);
        }}, board.getResInLeaderDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD, 2);
        }}, board.getResInLeaderDepot(1));

        //TODO: continue
    }

    @Test
    public void enoughResInNormalDepotsTest1(){
        TreeMap<Resource,Integer> toAdd=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SHIELD,2);
            put(Resource.SERVANT,3);
        }};
        board.storeInNormalDepot(toAdd);

        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.GOLD,1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD,2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SERVANT,3);
        }}, board.getResInDepot(2));

        TreeMap<Resource,Integer> toGive=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SERVANT,1);
        }};
        assertTrue(board.enoughResInNormalDepots(toGive));

        //assert that the method has not side effects
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.GOLD,1);
        }}, board.getResInDepot(0));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SHIELD,2);
        }}, board.getResInDepot(1));
        assertEquals(new TreeMap<Resource,Integer>(){{
            put(Resource.SERVANT,3);
        }}, board.getResInDepot(2));

        toGive=new TreeMap<>(){{
            put(Resource.GOLD,2);
            put(Resource.SERVANT,1);
        }};
        assertFalse(board.enoughResInNormalDepots(toGive));

        toGive=new TreeMap<>(){{
            put(Resource.ROCK,2);
        }};
        assertFalse(board.enoughResInNormalDepots(toGive));

        toGive=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.SHIELD,2);
            put(Resource.SERVANT,3);
        }};
        assertTrue(board.enoughResInNormalDepots(toGive));

    }



}