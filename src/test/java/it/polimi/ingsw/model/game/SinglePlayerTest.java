package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.exception.GameIsOverException;
import it.polimi.ingsw.model.player.DevelopCardSlot;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SinglePlayerTest {
    SinglePlayer singlePlayer;

    @Before
    public void setUp(){
        singlePlayer = new SinglePlayer(new Player("player", 1));
    }

    @Test
    public void testNextTurnLorenzoWinsDevelop(){
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.getTurn().isLorenzoPlaying());
        // draw all cards from a develop deck
        singlePlayer.getDecksDevelop().get(Color.BLUE).get(2).distributeCards(4);
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.isGameOver());
        assertFalse(singlePlayer.getHasPlayerWon());
    }

    @Test
    public void testNextTurnLorenzoWinsFaith(){
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.getTurn().isLorenzoPlaying());
        // lorenzo gets to the end of the track
        singlePlayer.getLorenzo().getFaithTrack().move(24, singlePlayer);
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.isGameOver());
        assertFalse(singlePlayer.getHasPlayerWon());
    }

    @Test
    public void testNextTurnPlayerWinsFaith(){
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        // player gets to the end of the faith track
        singlePlayer.getPlayer().getBoard().getFaithtrack().move(24, singlePlayer);
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.isGameOver());
        assertTrue(singlePlayer.getHasPlayerWon());
    }

    @Test
    public void testNextTurnPlayerWinsDevelop(){
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        // player buys 7 develop cards
        ArrayList<DevelopCardSlot> slots = singlePlayer.getPlayer().getBoard().getDevelopCardSlots();
        slots.get(0).addDevelopCard(singlePlayer.drawDevelopCard(Color.BLUE,1));
        slots.get(0).addDevelopCard(singlePlayer.drawDevelopCard(Color.GREEN,2));
        slots.get(0).addDevelopCard(singlePlayer.drawDevelopCard(Color.GREEN,3));
        slots.get(1).addDevelopCard(singlePlayer.drawDevelopCard(Color.GOLD,1));
        slots.get(1).addDevelopCard(singlePlayer.drawDevelopCard(Color.GOLD,2));
        slots.get(1).addDevelopCard(singlePlayer.drawDevelopCard(Color.GOLD,3));
        slots.get(2).addDevelopCard(singlePlayer.drawDevelopCard(Color.PURPLE,1));
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.isGameOver());
        assertTrue(singlePlayer.getHasPlayerWon());
    }

    @Test (expected = GameIsOverException.class)
    public void testNextTurnException(){
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.getTurn().isLorenzoPlaying());
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertFalse(singlePlayer.getTurn().isLorenzoPlaying());
        // player gets to the end of the faith track
        singlePlayer.getPlayer().getBoard().getFaithtrack().move(24, singlePlayer);
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
        assertTrue(singlePlayer.isGameOver());
        assertTrue(singlePlayer.getHasPlayerWon());
        // if i try to call nextTurn when the game is over i get an exception
        singlePlayer.nextTurn();
    }

    @Test
    public void testDistributeLeader(){
        singlePlayer.distributeLeader();
        assertEquals(singlePlayer.getPlayer().getBoard().getLeaderCards().size(), 4);
    }
}