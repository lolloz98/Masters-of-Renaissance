package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class GameTest {
    private static final Logger logger = LogManager.getLogger(GameTest.class);
    Game<? extends Turn> game;

    @Before
    public void setUp() throws WrongColorDeckException, WrongLevelDeckException, EmptyDeckException {
        game = new Game<>(){

            @Override
            public Player getPlayer(int playerId) {
                return null;
            }

            @Override
            public void checkEndConditions() {

            }

            @Override
            public void nextTurn() throws GameIsOverException, MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {

            }

            @Override
            public void distributeLeader() throws EmptyDeckException {

            }
        };
    }

    @Test
    public void testLoadDecksDevelop() throws EmptyDeckException {
        TreeSet<Integer> ids = new TreeSet<>();
        TreeMap<Color, Integer> cs = new TreeMap<>(){{
           for(Color c: Color.values()) put(c, 0);
        }};
        for(Color c: Color.values()) {
            for(int j = 1; j < 4; j++) {
                for (int i = 0; i < 4; i++) {
                    // logger.debug(game.getDecksDevelop().get(c).get(j).topCard().getId());
                    if (ids.contains(game.getDecksDevelop().get(c).get(j).topCard().getId())) fail();
                    else {
                        ids.add(game.getDecksDevelop().get(c).get(j).drawCard().getId());
                        cs.replace(c, cs.get(c) + 1);
                    }
                }
                try{
                    game.getDecksDevelop().get(c).get(j).topCard();
                    fail();
                }catch (EmptyDeckException ignore){}
            }
        }
        for(int i = 1; i < 49; i++){
            // logger.debug(i);
            assertTrue(ids.contains(i));
        }
        for(Color c: cs.keySet()) assertEquals(12, (int)cs.get(c));
    }

    @Test
    public void testLoadDecksLeader() throws EmptyDeckException {
        TreeSet<Integer> s = new TreeSet<>();
        for(int i = 0; i < 16; i++) {
            if(s.contains(game.getDeckLeader().topCard().getId())) fail();
            else s.add(game.getDeckLeader().drawCard().getId());
        }
        for(int i = 0; i < 16; i++){
            assertTrue(s.contains(i + 49));
        }
    }

    @Test
    public void testDrawDevelopCard() throws EmptyDeckException, LevelOutOfBoundException {
        assertFalse(game.isADeckDevelopEmpty());
        DevelopCard c = game.drawDevelopCard(Color.PURPLE, 2);
        assertEquals(Color.PURPLE, c.getColor());
        assertEquals(2, c.getLevel());

        c = game.drawDevelopCard(Color.PURPLE, 2);
        assertEquals(Color.PURPLE, c.getColor());
        assertEquals(2, c.getLevel());

        c = game.drawDevelopCard(Color.PURPLE, 2);
        assertEquals(Color.PURPLE, c.getColor());
        assertEquals(2, c.getLevel());

        c = game.drawDevelopCard(Color.PURPLE, 2);
        assertEquals(Color.PURPLE, c.getColor());
        assertEquals(2, c.getLevel());

        try{
            game.drawDevelopCard(Color.PURPLE, 2);
            fail();
        } catch (EmptyDeckException ignore){}

        assertTrue(game.isADeckDevelopEmpty());

        c = game.drawDevelopCard(Color.GREEN, 3);
        assertEquals(Color.GREEN, c.getColor());
        assertEquals(3, c.getLevel());

        c = game.drawDevelopCard(Color.GOLD, 1);
        assertEquals(Color.GOLD, c.getColor());
        assertEquals(1, c.getLevel());

        try{
            game.drawDevelopCard(Color.PURPLE, 4);
            fail();
        }catch(LevelOutOfBoundException ignore){}
    }
}