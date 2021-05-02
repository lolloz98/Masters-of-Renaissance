package it.polimi.ingsw.server.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.Deck;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.cards.leader.Requirement;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.exception.GameIsOverException;
import it.polimi.ingsw.server.model.exception.InvalidResourcesToKeepByPlayerException;
import it.polimi.ingsw.server.model.player.DevelopCardSlot;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MultiPlayerTest {
    MultiPlayer multiPlayer;

    @Before
    public void setUp(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        players.add(new Player("fourth", 4));
        multiPlayer = new MultiPlayer(players);
    }

    @Test
    public void testDecksDevelop(){ // maybe should be modified to exclude shuffle method
        DeckDevelop dd;
        DevelopCard dc;
        for(Color color : Color.values()) {
            for (int level = 1; level < 4; level++) {
                dd = multiPlayer.getDecksDevelop().get(color).get(level);
                for (int i = 0; i < dd.howManyCards(); i++) {
                    dc = dd.drawCard();
                    assertEquals(color, dc.getColor());
                    assertEquals(level, dc.getLevel());
                }
            }
        }
    }

    @Test
    public void testDeckLeader(){ // maybe should be modified to exclude shuffle method
        Deck<LeaderCard<? extends Requirement>> dl = multiPlayer.getDeckLeader();
        for(int i=0; i<12; i++){
            dl.drawCard();
        }
    }

    @Test (expected = EmptyDeckException.class)
    public void testIsADeckDevelopEmptyTooManyCalls(){
        for(int i = 0; i <5; i++)
            multiPlayer.drawDevelopCard(Color.BLUE, 2);
    }

    @Test (expected = EmptyDeckException.class)
    public void testIsADeckDevelopEmptyTooManyCalls2(){
        for(int i = 0; i <5; i++)
            multiPlayer.getDecksDevelop().get(Color.BLUE).get(2).drawCard();
    }

    @Test
    public void testIsADeckDevelopEmptyTrue(){
        for(int i = 0; i <4; i++)
            multiPlayer.drawDevelopCard(Color.GREEN, 1);
        assertTrue(multiPlayer.isADeckDevelopEmpty());
    }

    @Test
    public void testIsADeckDevelopEmptyFalse(){
        for(int i = 0; i <3; i++)
            multiPlayer.drawDevelopCard(Color.PURPLE, 3);
        assertFalse(multiPlayer.isADeckDevelopEmpty());
    }

    @Test
    public void testNextTurnEndOfFaithTrack(){
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        // player 0 gets to the end of the faith track
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getFaithtrack().move(24, multiPlayer);
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        // end of game is triggered
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertTrue(multiPlayer.isGameOver());
    }

    @Test
    public void testNextTurnDevelop(){
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        // player 0 buys 7 develop cards
        ArrayList<DevelopCardSlot> slots = multiPlayer.getPlayers().get(0).getBoard().getDevelopCardSlots();
        slots.get(0).addDevelopCard(multiPlayer.drawDevelopCard(Color.BLUE,1));
        slots.get(0).addDevelopCard(multiPlayer.drawDevelopCard(Color.GREEN,2));
        slots.get(0).addDevelopCard(multiPlayer.drawDevelopCard(Color.GREEN,3));
        slots.get(1).addDevelopCard(multiPlayer.drawDevelopCard(Color.GOLD,1));
        slots.get(1).addDevelopCard(multiPlayer.drawDevelopCard(Color.GOLD,2));
        slots.get(1).addDevelopCard(multiPlayer.drawDevelopCard(Color.GOLD,3));
        slots.get(2).addDevelopCard(multiPlayer.drawDevelopCard(Color.PURPLE,1));
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getFaithtrack().move(24, multiPlayer);
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        // end of game is triggered
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertTrue(multiPlayer.isGameOver());
    }

    @Test (expected = GameIsOverException.class)
    public void testNextTurnException(){
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(0), multiPlayer.getTurn().getCurrentPlayer());
        assertFalse(multiPlayer.isLastRound());
        // player 0 gets to the end of the faith track
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getFaithtrack().move(24, multiPlayer);
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(1), multiPlayer.getTurn().getCurrentPlayer());
        // end of game is triggered
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(2), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(multiPlayer.getPlayers().get(3), multiPlayer.getTurn().getCurrentPlayer());
        assertTrue(multiPlayer.isLastRound());
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertTrue(multiPlayer.isGameOver());
        // if i try to call nextTurn when the game is over i get an exception
        multiPlayer.nextTurn();
    }

    @Test
    public void testDistributeLeader(){
        multiPlayer.distributeLeader();
        for(Player p : multiPlayer.getPlayers()){
            assertEquals(p.getBoard().getLeaderCards().size(), 4);
        }
    }

    @Test
    public void testGetWinnersSingle() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        multiPlayer.distributeLeader();
        DevelopCard dc;
        try {
            dc = (gson.fromJson(new JsonReader(new FileReader("json_file/cards/develop/005.json")), DevelopCard.class));
        } catch (FileNotFoundException e) {
            dc = multiPlayer.drawDevelopCard(Color.BLUE, 1);
        }
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getDevelopCardSlots().get(0).addDevelopCard(dc);
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getFaithtrack().move(24,multiPlayer);
        try {
            multiPlayer.getTurn().getCurrentPlayer().getBoard().gainResourcesSmart(
                    new TreeMap<>(){{
                        put(Resource.GOLD, 1);
                    }},
                    new TreeMap<>(){{
                        put(Resource.GOLD, 1);
                    }},
                    multiPlayer
            );
        } catch (InvalidResourcesToKeepByPlayerException e) {
            e.printStackTrace();
        }
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        multiPlayer.getTurn().setMainActionOccurred();
        multiPlayer.nextTurn();
        assertEquals(new ArrayList<>(){{add(multiPlayer.getPlayers().get(0));}}, multiPlayer.getWinners());
    }
}