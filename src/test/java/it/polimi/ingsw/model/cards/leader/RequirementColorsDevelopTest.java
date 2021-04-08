package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class RequirementColorsDevelopTest {
    Requirement requirement;
    TreeMap<Color, Integer> requiredDevelop;
    Player player;
    Game<?> game;

    @Before
    public void setUp(){
        requiredDevelop = new TreeMap<>(){{
            put(Color.BLUE, 2);
            put(Color.GOLD, 1);
        }};
        requirement = new RequirementColorsDevelop(requiredDevelop);
        player = new Player("lorenzo", 0);
        game = new SinglePlayer(player);

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.GOLD, 60);
            put(Resource.ROCK, 60);
            put(Resource.SERVANT, 60);
            put(Resource.SHIELD, 60);
        }}, game);
    }

    @Test
    public void getRequiredDevelop() {
        assertEquals(requiredDevelop, ((RequirementColorsDevelop)requirement).getRequiredDevelop());
        TreeMap<Color, Integer> copy = new TreeMap<>(requiredDevelop);
        requiredDevelop.clear();
        assertEquals(copy, ((RequirementColorsDevelop)requirement).getRequiredDevelop());
    }

    @Test
    public void testCheckRequirement() {
        assertFalse(requirement.checkRequirement(player));

        player.getBoard().buyDevelopCard(game, Color.BLUE, 1, 1);
        player.getBoard().buyDevelopCard(game, Color.GOLD, 2, 1);
        player.getBoard().buyDevelopCard(game, Color.GREEN, 1, 2);

        assertFalse(requirement.checkRequirement(player));

        player.getBoard().buyDevelopCard(game, Color.BLUE, 1, 0);

        assertTrue(requirement.checkRequirement(player));
    }
}