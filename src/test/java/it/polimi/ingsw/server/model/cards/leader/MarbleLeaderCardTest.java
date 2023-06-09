package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MarbleLeaderCardTest {
    private static final Logger logger = LogManager.getLogger(MarbleLeaderCardTest.class);
    private final static boolean isSinglePlayer = false;
    MarbleLeaderCard leaderCard1;
    MarbleLeaderCard leaderCard2;
    ArrayList<Player> players;
    Game<?> game;
    TreeMap<Color, Integer> reqDevelop;
    Resource targetRes1 = Resource.ROCK;
    Resource targetRes2 = Resource.GOLD;

    @BeforeClass
    public static void testInfo() {
        if (isSinglePlayer) logger.info("testing on single player");
        else logger.info("testing on multiplayer");
    }

    @Before
    public void setUp() throws Exception {
        players = new ArrayList<>() {{
            add(new Player("Lorenzo", 0));
            add(new Player("Lorenzo", 1));
            add(new Player("Aniello", 2));
        }};

        game = (isSinglePlayer) ? new MultiPlayer(players) : new SinglePlayer(players.get(0));

        reqDevelop = new TreeMap<>() {{
            put(Color.PURPLE, 2);
            put(Color.GREEN, 1);
        }};

        leaderCard1 = new MarbleLeaderCard(2, new RequirementColorsDevelop(reqDevelop), targetRes1, 61);
        leaderCard2 = new MarbleLeaderCard(2, new RequirementColorsDevelop(reqDevelop), targetRes2, 61);
    }

    private void satisfyReq(Player player) throws ModelException {
        player.getBoard().flushGainedResources(new TreeMap<>() {{
            put(Resource.GOLD, 60);
            put(Resource.ROCK, 60);
            put(Resource.SERVANT, 60);
            put(Resource.SHIELD, 60);
        }}, game);

        player.getBoard().buyDevelopCardSmart(game, Color.PURPLE, 1, 1);
        player.getBoard().buyDevelopCardSmart(game, Color.GREEN, 2, 1);
        player.getBoard().buyDevelopCardSmart(game, Color.PURPLE, 1, 0);
    }

    private void giveOwnership(Player player, LeaderCard<?> l) {
        player.getBoard().addLeaderCards(new ArrayList<>() {
            {
                add(l);
            }
        });
    }

    @Test
    public void testActivate() throws ModelException {
        Player player = players.get(0);
        try {
            leaderCard1.activate(game, player);
            fail();
        } catch (RequirementNotSatisfiedException ignored) {
        }

        satisfyReq(player);

        try {
            leaderCard1.activate(game, player);
            fail();
        } catch (InvalidArgumentException ignored) {
        }

        giveOwnership(player, leaderCard1);

        assertFalse(leaderCard1.isActive());
        assertTrue(player.getBoard().getProductionLeaders().isEmpty());

        leaderCard1.activate(game, player);

        assertTrue(game.getMarketTray().isLeaderApplied());
        assertTrue(leaderCard1.isActive());

        try {
            leaderCard1.activate(game, player);
            fail();
        } catch (AlreadyActiveLeaderException ignored) {
        }

        giveOwnership(player, leaderCard2);

        leaderCard2.activate(game, player);
        assertTrue(game.getMarketTray().isLeaderApplied());

        leaderCard1.removeEffect(game);
        // removed effect of leader one, but effect of leader two still there
        assertTrue(game.getMarketTray().isLeaderApplied());
        leaderCard2.removeEffect(game);
        assertFalse(game.getMarketTray().isLeaderApplied());
    }

    @Test
    public void testDiscard() throws ModelException {
        assertFalse(leaderCard1.isDiscarded());
        leaderCard1.discard();
        assertTrue(leaderCard1.isDiscarded());
        satisfyReq(players.get(0));
        giveOwnership(players.get(0), leaderCard1);
        try {
            leaderCard1.activate(game, players.get(0));
            fail();
        } catch (ActivateDiscardedCardException ignored) {
        }
    }

    @Test
    public void testApplyEffect() throws ModelException {
        Player player = players.get(0);
        satisfyReq(player);
        giveOwnership(player, leaderCard1);

        // it does nothing because the card has not been activated
        leaderCard1.applyEffect(game);
        // thus I can check that the leader has not been applied on the market
        assertFalse(game.getMarketTray().isLeaderApplied());

        leaderCard1.activate(game, player);
        assertTrue(game.getMarketTray().isLeaderApplied());

        try {
            leaderCard1.applyEffect(game);
            fail();
        } catch (AlreadyAppliedLeaderCardException ignored) {
        }

        leaderCard1.removeEffect(game);
        assertFalse(game.getMarketTray().isLeaderApplied());

        leaderCard1.applyEffect(game);
        assertTrue(game.getMarketTray().isLeaderApplied());
    }

    @Test
    public void testRemoveEffect() throws ModelException {
        Player player = players.get(0);
        satisfyReq(player);
        giveOwnership(player, leaderCard1);

        leaderCard1.activate(game, player);
        assertTrue(game.getMarketTray().isLeaderApplied());

        leaderCard1.removeEffect(game);
        assertFalse(game.getMarketTray().isLeaderApplied());
    }
}