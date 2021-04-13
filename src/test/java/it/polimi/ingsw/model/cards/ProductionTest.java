package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
import it.polimi.ingsw.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.exception.ProductionAlreadyActivatedException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.player.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.WarehouseType;
import it.polimi.ingsw.model.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ProductionTest {
    private static final Logger logger = LogManager.getLogger(ProductionTest.class);

    TreeMap<Resource, Integer> toGive;
    TreeMap<Resource, Integer> toGain;
    TreeMap<WarehouseType, TreeMap<Resource, Integer>> chosenByPlayerGive;
    TreeMap<Resource, Integer> chosenByPlayerGain;
    Production production;
    Board board;
    static boolean isSinglePlayer = false;
    Game<?> game;
    SinglePlayer singlePlayer;
    MultiPlayer multiPlayer;

    @BeforeClass
    public static void infoOnGame(){
        if(isSinglePlayer){
            logger.info("testing with singlePlayer game");
        }else{
            logger.info("testing with MultiPlayer game");
        }
    }

    @Before
    public void setUp() {
        toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};


        toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        production = new Production(toGive, toGain);

        chosenByPlayerGive = new TreeMap<>(){
            {
                put(WarehouseType.STRONGBOX,new TreeMap<>() {{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 1);
                    put(Resource.SERVANT, 2);
                }});
            }};


        chosenByPlayerGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.GOLD, 1);
        }};

        singlePlayer = new SinglePlayer(new Player("john", 0));

        multiPlayer = new MultiPlayer(new ArrayList<>(){{
            add(new Player("marco", 0));
            add(new Player("lorenzo", 1));
        }});

        game = (isSinglePlayer)? singlePlayer: multiPlayer;

        board = new Board();
        for(int i = 0; i < 3; i++)
            assertTrue(board.getResInDepot(i).isEmpty());
        assertTrue(board.getResourcesInStrongBox().isEmpty());
        assertTrue(board.getDepotLeaders().isEmpty());
    }

    @Test
    public void testWhatResourceToGive() {
        assertEquals(toGive, production.whatResourceToGive());
    }

    @Test
    public void testWhatResourceToGain() {
        assertEquals(toGain, production.whatResourceToGain());
    }

    @Test
    public void testApplyProduction() {
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            fail();
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        } catch(NotEnoughResourcesException ignore){}

        // put in the strongBox
        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            assertTrue(Utility.checkTreeMapEquality(new TreeMap<>(), board.getResourcesInStrongBox()));
            production.flushGainedToBoard(board, singlePlayer);
            assertTrue(Utility.checkTreeMapEquality(chosenByPlayerGain, board.getResourcesInStrongBox()));
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }

        // put in the strongBox
        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            // reapply production before flushing
            board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            fail();
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        } catch(ProductionAlreadyActivatedException ignored){}


        board = new Board();

        chosenByPlayerGive.replace(WarehouseType.STRONGBOX, new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 2);
        }});

        // Faith can't take the place of ANYTHING
        chosenByPlayerGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 1);
        }};

        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            fail();
        } catch (InvalidResourcesByPlayerException ignored) {}


        board = new Board();

        toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 1);
        }};

        production = new Production(toGive, toGain);

        chosenByPlayerGive.replace(WarehouseType.STRONGBOX, new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 2);
        }});

        chosenByPlayerGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 1);
        }};

        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            assertTrue(Utility.checkTreeMapEquality(new TreeMap<>(), board.getResourcesInStrongBox()));
            production.flushGainedToBoard(board, game);

            chosenByPlayerGain.remove(Resource.FAITH);
            assertTrue(Utility.checkTreeMapEquality(chosenByPlayerGain, board.getResourcesInStrongBox()));
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }

        // todo: more testing. and test also when done board.gainRes first
    }

    @Test
    public void testCheckResToGiveForActivation() {
        try {
            assertTrue(production.checkResToGiveForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 3);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertFalse(production.checkResToGiveForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 4);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertTrue(production.checkResToGiveForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 2);
                        put(Resource.ROCK, 2);
                        put(Resource.SHIELD, 1);
                    }})
            );

            assertTrue(production.checkResToGiveForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 2);
                        put(Resource.ROCK, 1);
                        put(Resource.SHIELD, 1);
                        put(Resource.SERVANT, 1);
                    }})
            );
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }
    }

    @Test
    public void testCheckResToGainForActivation() {
        try {
            assertFalse(production.checkResToGainForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 3);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertFalse(production.checkResToGainForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 4);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertTrue(production.checkResToGainForActivation(new TreeMap<>() {{
                        put(Resource.ROCK, 1);
                        put(Resource.SHIELD, 2);
                    }})
            );

            assertTrue(production.checkResToGainForActivation(new TreeMap<>() {{
                        put(Resource.GOLD, 1);
                        put(Resource.SHIELD, 2);
                    }})
            );
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }
    }

    @Test
    public void testCheckResException(){
        try{
            assertFalse(production.checkResToGainForActivation(new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.FAITH, 2);
            }}));
        }catch (InvalidResourcesByPlayerException ignored){ }

        try{
            production.checkResToGainForActivation(new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 2);
                put(Resource.ANYTHING, 1);
            }});
            fail();
        }catch (InvalidResourcesByPlayerException ignored){ }

        try{
            production.checkResToGiveForActivation(new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 1);
                put(Resource.SHIELD, 1);
                put(Resource.FAITH, 1);
            }});
            fail();
        }catch (InvalidResourcesByPlayerException ignored){ }
    }

    @Test
    public void testFlushGainedToBoard() {
        // Check out testApplyProduction
    }

    @Test
    public void testGetGainedResources() {
        // put in the strongBox
        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            assertTrue(Utility.checkTreeMapEquality(production.getGainedResources(), chosenByPlayerGain));
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }


        board = new Board();

        toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 1);
        }};

        production = new Production(toGive, toGain);

        chosenByPlayerGive.replace(WarehouseType.STRONGBOX, new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.SERVANT, 2);
        }});

        chosenByPlayerGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.FAITH, 1);
        }};

        board.flushGainedResources(Utility.getTotalResources(chosenByPlayerGive), game);
        try {
            production.applyProduction(chosenByPlayerGive, chosenByPlayerGain, board);
            assertTrue(Utility.checkTreeMapEquality(new TreeMap<>(), board.getResourcesInStrongBox()));
            assertTrue(Utility.checkTreeMapEquality(production.getGainedResources(), chosenByPlayerGain));
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }
    }
}