package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoTest {
    SinglePlayer singlePlayer;

    @Before
    public void setUp(){
        CollectionsHelper.setTest();
        singlePlayer = new SinglePlayer(new Player("play", 1));
    }

    @Test
    public void testPerformLorenzoAction(){
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