package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.cards.leader.LeaderCard;
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
        Deck<LeaderCard> dl = multiPlayer.getDeckLeader();
        for(int i=0; i<12; i++){
            dl.drawCard();
        }
    }
}