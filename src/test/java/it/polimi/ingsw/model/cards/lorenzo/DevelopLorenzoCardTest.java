package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DevelopLorenzoCardTest {

    private static final Logger logger = LogManager.getLogger(DevelopLorenzoCardTest.class);

    LorenzoCard card;
    SinglePlayer singlePlayer;
    Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player("single", 1);

        singlePlayer = new SinglePlayer(player);

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.GOLD, 60);
            put(Resource.ROCK, 60);
            put(Resource.SERVANT, 60);
            put(Resource.SHIELD, 60);
        }}, singlePlayer);
        // logger.debug("#cards in deckDevelop Blue lv 1: " + singlePlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());
        player.getBoard().buyDevelopCard(singlePlayer, Color.BLUE, 1, 1);
        player.getBoard().buyDevelopCard(singlePlayer, Color.GOLD, 2, 1);
        player.getBoard().buyDevelopCard(singlePlayer, Color.GREEN, 1, 2);
    }

    @Test
    public void testApplyEffect() {
        card = new DevelopLorenzoCard(0, Color.BLUE);
        card.applyEffect(singlePlayer);
        // two cards removed
        assertEquals(1, singlePlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());

        card.applyEffect(singlePlayer);
        assertEquals(0, singlePlayer.getDecksDevelop().get(Color.BLUE).get(1).howManyCards());
        assertEquals(3, singlePlayer.getDecksDevelop().get(Color.BLUE).get(2).howManyCards());

        card = new DevelopLorenzoCard(0, Color.PURPLE);
        card.applyEffect(singlePlayer);
        assertEquals(2, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(1).howManyCards());

        card.applyEffect(singlePlayer);
        card.applyEffect(singlePlayer);
        card.applyEffect(singlePlayer);
        card.applyEffect(singlePlayer);
        assertEquals(0, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(1).howManyCards());
        assertEquals(0, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(2).howManyCards());
        assertEquals(2, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(3).howManyCards());
        card.applyEffect(singlePlayer);
        assertEquals(0, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(3).howManyCards());
        card.applyEffect(singlePlayer);
        assertEquals(0, singlePlayer.getDecksDevelop().get(Color.PURPLE).get(3).howManyCards());
    }
}