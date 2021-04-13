package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WarehouseType;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class RequirementLevelDevelopTest {
    Requirement requirement;
    TreeMap<Color, Integer> requiredDevelop;
    TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay = new TreeMap<>();
    Player player;
    Color color = Color.PURPLE;
    Game<?> game;

    @Before
    public void setUp(){
        requiredDevelop = new TreeMap<>(){{
            put(Color.BLUE, 2);
            put(Color.GOLD, 1);
        }};
        requirement = new RequirementLevelDevelop(color);
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
        assertEquals(color, ((RequirementLevelDevelop)requirement).getColor());
        assertEquals(2, ((RequirementLevelDevelop)requirement).getLevel());
    }

    @Test
    public void testCheckRequirement() {
        assertFalse(requirement.checkRequirement(player, toPay));

        player.getBoard().buyDevelopCardSmart(game, Color.BLUE, 1, 1);
        player.getBoard().buyDevelopCardSmart(game, Color.GOLD, 2, 1);
        player.getBoard().buyDevelopCardSmart(game, Color.GREEN, 1, 2);

        assertFalse(requirement.checkRequirement(player, toPay));

        player.getBoard().buyDevelopCardSmart(game, Color.PURPLE, 1, 0);
        assertFalse(requirement.checkRequirement(player, toPay));

        player.getBoard().buyDevelopCardSmart(game, Color.PURPLE, 2, 0);

        assertTrue(requirement.checkRequirement(player, toPay));
    }
}