package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class RequirementResourceTest {
    Requirement requirement;
    TreeMap<Color, Integer> requiredDevelop;
    Player player;
    Resource res = Resource.ROCK;
    Game<?> game;

    @Before
    public void setUp() throws ModelException {
        requiredDevelop = new TreeMap<>(){{
            put(Color.BLUE, 2);
            put(Color.GOLD, 1);
        }};
        requirement = new RequirementResource(res);
        player = new Player("lorenzo", 0);
        game = new SinglePlayer(player);
    }

    @Test
    public void getRequiredDevelop() {
        assertEquals(res, ((RequirementResource)requirement).getRes());
        assertEquals(5, ((RequirementResource)requirement).getQuantity());
    }

    @Test
    public void testCheckRequirement() throws ModelException {
        assertFalse(requirement.checkRequirement(player));

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
            put(Resource.SERVANT, 5);
            put(Resource.SHIELD, 5);
        }}, game);

        assertFalse(requirement.checkRequirement(player));

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.ROCK, 2);
        }}, game);

        assertTrue(requirement.checkRequirement(player));

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.ROCK, 1);
        }}, game);

        assertTrue(requirement.checkRequirement(player));
    }

    @Test
    public void testCheckRequirement2() throws ModelException {
        player.getBoard().storeInNormalDepot(new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 3);
        }});

        assertFalse(requirement.checkRequirement(player));

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.ROCK, 1);
        }}, game);

        assertFalse(requirement.checkRequirement(player));

        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.ROCK, 1);
        }}, game);

        assertTrue(requirement.checkRequirement(player));
    }
}