package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
import it.polimi.ingsw.model.cards.leader.Requirement;
import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MultiPlayerTest {
    MultiPlayer multiPlayer;

    @Before
    public void setUp(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        multiPlayer = new MultiPlayer(players);
    }

    @Test
    public void testDecksDevelop(){ // FIXME should be modified to exclude shuffle method
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
    public void testDeckLeader(){ // FIXME should be modified to exclude shuffle method
        // TODO test more
        Deck<LeaderCard<? extends Requirement>> dl = multiPlayer.getDeckLeader();
        for(int i=0; i<12; i++){
            dl.drawCard();
        }
    }

    @Test (expected = EmptyDeckException.class)
    public void testIsADeckDevelopEmptyTooManyCalls(){
        for(int i = 0; i <5; i++)
            multiPlayer.getDecksDevelop().get(Color.BLUE).get(2).drawCard();
    }

    @Test
    public void testIsADeckDevelopEmptyTrue(){
        for(int i = 0; i <4; i++)
            multiPlayer.getDecksDevelop().get(Color.BLUE).get(2).drawCard();
        assertTrue(multiPlayer.isADeckDevelopEmpty());
    }

    @Test
    public void testIsADeckDevelopEmptyFalse(){
        for(int i = 0; i <3; i++)
            multiPlayer.getDecksDevelop().get(Color.BLUE).get(2).drawCard();
        assertTrue(!multiPlayer.isADeckDevelopEmpty());
    }

    @Test
    public void testGetNewId(){
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(new Player("fourth", 1));
        players1.add(new Player("fifth", 2));
        players1.add(new Player("sixth", 3));
        MultiPlayer multiPlayer1 = new MultiPlayer(players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(new Player("Aniello", 1));
        players2.add(new Player("Lorenzo", 2));
        players2.add(new Player("Lorenzo", 3));
        MultiPlayer multiPlayer2 = new MultiPlayer(players2);
        assertNotEquals(multiPlayer2.getId(), multiPlayer1.getId());
        assertNotEquals(multiPlayer1.getId(), multiPlayer.getId());
        assertNotEquals(multiPlayer.getId(), multiPlayer2.getId());
        multiPlayer1.destroy();
        multiPlayer2.destroy();
    }


}