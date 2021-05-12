package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Depot;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DepotLeaderCardTest {
    private static final Logger logger = LogManager.getLogger(DepotLeaderCardTest.class);
    private final static boolean isSinglePlayer = true;
    DepotLeaderCard leaderCard;
    Depot depot;
    ArrayList<Player> players;
    Game<?> game;
    Resource requiredRes = Resource.ROCK;

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

        depot = new Depot(Resource.GOLD);

        leaderCard = new DepotLeaderCard(2, new RequirementResource(requiredRes), depot, 61);
    }

    private void satisfyReq(Player player) throws ModelException {
        player.getBoard().flushGainedResources(new TreeMap<>() {{
            put(requiredRes, 5);
        }}, game);
    }

    private void giveOwnership(Player player) {
        player.getBoard().addLeaderCards(new ArrayList<>() {
            {
                add(leaderCard);
            }
        });
    }

    @Test
    public void testActivate() throws ModelException {
        Player player = players.get(0);
        try {
            leaderCard.activate(game, player);
            fail();
        } catch (RequirementNotSatisfiedException ignored) {
        }

        satisfyReq(player);

        try {
            leaderCard.activate(game, player);
            fail();
        } catch (InvalidArgumentException ignored) {
        }

        giveOwnership(player);

        assertFalse(leaderCard.isActive());
        assertTrue(player.getBoard().getProductionLeaders().isEmpty());

        leaderCard.activate(game, player);

        assertEquals(leaderCard, player.getBoard().getDepotLeaders().get(0));
        assertTrue(leaderCard.isActive());

        satisfyReq(player);

        try {
            leaderCard.activate(game, player);
            fail();
        } catch (AlreadyActiveLeaderException ignored) {
        }
    }

    @Test
    public void testDiscard() throws ModelException {
        assertFalse(leaderCard.isDiscarded());
        leaderCard.discard();
        assertTrue(leaderCard.isDiscarded());
        satisfyReq(players.get(0));
        giveOwnership(players.get(0));
        try {
            leaderCard.activate(game, players.get(0));
            fail();
        } catch (ActivateDiscardedCardException ignored) {
        }
    }

    @Test
    public void testApplyEffect() {
        // in this card this method does nothing
        leaderCard.applyEffect(game);
    }

    @Test
    public void testRemoveEffect() {
        // in this card this method does nothing
        leaderCard.removeEffect(game);
    }

    @Test
    public void testGetDepot() {
        assertEquals(depot, leaderCard.getDepot());
    }
}