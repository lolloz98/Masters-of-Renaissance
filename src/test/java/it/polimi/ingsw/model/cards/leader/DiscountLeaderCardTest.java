package it.polimi.ingsw.model.cards.leader;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.cards.DeckDevelop;
import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DiscountLeaderCardTest {
    private static final Logger logger = LogManager.getLogger(DepotLeaderCardTest.class);
    private final static boolean isSinglePlayer = false;
    DiscountLeaderCard leaderCard1;
    DiscountLeaderCard leaderCard2;
    ArrayList<Player> players;
    Game<?> game;
    TreeMap<Color, Integer> reqDevelop;
    Resource targetRes1 = Resource.ROCK;
    Resource targetRes2 = Resource.GOLD;

    @BeforeClass
    public static void testInfo(){
        if(isSinglePlayer) logger.info("testing on single player");
        else logger.info("testing on multiplayer");
    }

    @Before
    public void setUp() throws Exception {
        players = new ArrayList<>(){{
            add(new Player("Lorenzo", 0));
            add(new Player("Lorenzo", 1));
            add(new Player("Aniello", 2));
        }};

        game = (isSinglePlayer)? new MultiPlayer(players): new SinglePlayer(players.get(0));

        reqDevelop = new TreeMap<>(){{
            put(Color.PURPLE, 2);
            put(Color.GREEN, 1);
        }};

        leaderCard1 = new DiscountLeaderCard(2, new RequirementColorsDevelop(reqDevelop), targetRes1, 61);
        leaderCard2 = new DiscountLeaderCard(2, new RequirementColorsDevelop(reqDevelop), targetRes2, 62);
    }

    private void satisfyReq(Player player){
        player.getBoard().flushGainedResources(new TreeMap<>(){{
            put(Resource.GOLD, 60);
            put(Resource.ROCK, 60);
            put(Resource.SERVANT, 60);
            put(Resource.SHIELD, 60);
        }}, game);

        player.getBoard().buyDevelopCard(game, Color.PURPLE, 1, 1);
        player.getBoard().buyDevelopCard(game, Color.GREEN, 2, 1);
        player.getBoard().buyDevelopCard(game, Color.PURPLE, 3, 1);
    }

    private void giveOwnership(Player player, LeaderCard<?> l){
        player.getBoard().addLeaderCards(new ArrayList<>(){
            {add(l);}
        });
    }

    private void checkDiscountApplied(){
        for(Color color: game.getDecksDevelop().keySet()){
            TreeMap<Integer, DeckDevelop> decks = game.getDecksDevelop().get(color);
            for(int i: decks.keySet()){
                assertTrue(decks.get(i).isDiscounted());
            }
        }
    }

    private void checkNotDiscountApplied(){
        for(Color color: game.getDecksDevelop().keySet()){
            TreeMap<Integer, DeckDevelop> decks = game.getDecksDevelop().get(color);
            for(int i: decks.keySet()){
                assertFalse(decks.get(i).isDiscounted());
            }
        }
    }

    @Test
    public void testActivate() {
        Player player = players.get(0);
        try {
            leaderCard1.activate(game, player);
            fail();
        }catch(RequirementNotSatisfiedException ignored){}

        satisfyReq(player);

        try {
            leaderCard1.activate(game, player);
            fail();
        }catch(IllegalArgumentException ignored){}

        giveOwnership(player, leaderCard1);

        assertFalse(leaderCard1.isActive());
        assertTrue(player.getBoard().getProductionLeaders().isEmpty());

        leaderCard1.activate(game, player);
        checkDiscountApplied();

        assertTrue(leaderCard1.isActive());

        try {
            leaderCard1.activate(game, player);
            fail();
        }catch(AlreadyActiveLeaderException ignored){}

        giveOwnership(player, leaderCard2);

        leaderCard2.activate(game, player);
        checkDiscountApplied();

        leaderCard1.removeEffect(game);
        // removed effect of leader one, but effect of leader two still there
        checkDiscountApplied();
        leaderCard2.removeEffect(game);
        checkNotDiscountApplied();
    }

    @Test
    public void testDiscard() {
        assertFalse(leaderCard1.isDiscarded());
        leaderCard1.discard();
        assertTrue(leaderCard1.isDiscarded());
        satisfyReq(players.get(0));
        giveOwnership(players.get(0), leaderCard1);
        try{
            leaderCard1.activate(game, players.get(0));
            fail();
        }catch (ActivateDiscardedCardException ignored){}
    }

    @Test
    public void testApplyEffect() {
        Player player = players.get(0);
        satisfyReq(player);
        giveOwnership(player, leaderCard1);

        // it does nothing because the card has not been activated
        leaderCard1.applyEffect(game);
        // thus I can check that the discount has not been applied
        checkNotDiscountApplied();

        leaderCard1.activate(game, player);
        checkDiscountApplied();

        try{
            leaderCard1.applyEffect(game);
            fail();
        }catch (AlreadyAppliedLeaderCardException ignored){}

        leaderCard1.removeEffect(game);
        checkNotDiscountApplied();

        leaderCard1.applyEffect(game);
        checkDiscountApplied();
    }

    @Test
    public void testRemoveEffect() {
        Player player = players.get(0);
        satisfyReq(player);
        giveOwnership(player, leaderCard1);

        leaderCard1.activate(game, player);
        checkDiscountApplied();

        leaderCard1.removeEffect(game);
        checkNotDiscountApplied();
    }
}