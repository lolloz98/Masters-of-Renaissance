package it.polimi.ingsw.server.model.cards.leader;

import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.exception.ActivateDiscardedCardException;
import it.polimi.ingsw.server.model.exception.AlreadyActiveLeaderException;
import it.polimi.ingsw.server.model.exception.RequirementNotSatisfiedException;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ProductionLeaderCardTest {
    private static final Logger logger = LogManager.getLogger(ProductionLeaderCardTest.class);

    private final static boolean isSinglePlayer = true;
    ProductionLeaderCard leaderCard;
    Production production;
    ArrayList<Player> players;
    Game<?> game;

    @BeforeClass
    public static void testInfo(){
        if(isSinglePlayer) logger.info("testing on single player");
        else logger.info("testing on multiplayer");
    }

    @Before
    public void setUp() throws Exception {
        TreeMap<Resource, Integer> toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};


        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        production = new Production(toGive, toGain);

        players = new ArrayList<>(){{
          add(new Player("Lorenzo", 0));
          add(new Player("Lorenzo", 1));
          add(new Player("Aniello", 2));
        }};

        game = (isSinglePlayer)? new MultiPlayer(players): new SinglePlayer(players.get(0));

        leaderCard = new ProductionLeaderCard(2, new RequirementLevelDevelop(Color.BLUE), production, 0);
    }

    private void satisfyReq(Player player){
        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.GOLD, 60);
            put(Resource.ROCK, 60);
            put(Resource.SERVANT, 60);
            put(Resource.SHIELD, 60);
        }}, game);

        player.getBoard().buyDevelopCardSmart(game, Color.GOLD, 1, 1);
        player.getBoard().buyDevelopCardSmart(game, Color.BLUE, 2, 1);
    }

    private void giveOwnership(Player player){
        player.getBoard().addLeaderCards(new ArrayList<>(){
            {add(leaderCard);}
        });
    }

    @Test
    public void testActivate() {
        Player player = players.get(0);
        try {
            leaderCard.activate(game, player);
            fail();
        }catch(RequirementNotSatisfiedException ignored){}

        satisfyReq(player);

        try {
            leaderCard.activate(game, player);
            fail();
        }catch(IllegalArgumentException ignored){}

        giveOwnership(player);

        assertFalse(leaderCard.isActive());
        assertTrue(player.getBoard().getProductionLeaders().isEmpty());

        leaderCard.activate(game, player);

        assertEquals(leaderCard, player.getBoard().getProductionLeaders().get(0));
        assertTrue(leaderCard.isActive());

        try {
            leaderCard.activate(game, player);
            fail();
        }catch(AlreadyActiveLeaderException ignored){}
    }

    @Test
    public void testDiscard() {
        assertFalse(leaderCard.isDiscarded());
        leaderCard.discard();
        assertTrue(leaderCard.isDiscarded());
        satisfyReq(players.get(0));
        giveOwnership(players.get(0));
        try{
            leaderCard.activate(game, players.get(0));
            fail();
        }catch (ActivateDiscardedCardException ignored){}
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
    public void testGetProduction() {
        assertEquals(production, leaderCard.getProduction());
    }
}