package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoTest {
    SinglePlayer singlePlayer;

    @Before
    public void setUp() throws ModelException {
        CollectionsHelper.setTest();
        Player player = new Player("play", 1);
        singlePlayer = new SinglePlayer(player);
        // we pass the turn, so it's Lorenzo who's playing
        singlePlayer.getTurn().setMainActionOccurred();
        singlePlayer.nextTurn();
    }

    @Test
    public void testPerformLorenzoAction() throws ModelException {
        assertTrue(CollectionsHelper.isTest());
        // Faith
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        // Develop color purple
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2,singlePlayer.getDecksDevelop().get(Color.PURPLE).get(1).howManyCards());
        // Develop color blue
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2,singlePlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());

        CollectionsHelper.setSeedForTest(1);

        // Reshuffle
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(3, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        // Faith
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(5, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        // Develop color blue
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(0,singlePlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());
        // Develop color gold
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2,singlePlayer.getDecksDevelop().get(Color.GOLD).get(1).howManyCards());
        // Develop color purple
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(0,singlePlayer.getDecksDevelop().get(Color.PURPLE).get(1).howManyCards());
        // Reshuffle
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(6, singlePlayer.getLorenzo().getFaithTrack().getPosition());

        // Faith
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(8, singlePlayer.getLorenzo().getFaithTrack().getPosition());
        // Develop color blue
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2,singlePlayer.getDecksDevelop().get(Color.BLUE).get(2).howManyCards());
        // Develop color gold
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(0,singlePlayer.getDecksDevelop().get(Color.GOLD).get(1).howManyCards());
        // Develop color purple
        singlePlayer.getLorenzo().performLorenzoAction(singlePlayer);
        assertEquals(2,singlePlayer.getDecksDevelop().get(Color.PURPLE).get(2).howManyCards());
    }
}