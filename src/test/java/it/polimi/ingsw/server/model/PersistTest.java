package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class PersistTest {
    private static final Logger logger = LogManager.getLogger(Persist.class);
    private static Set<Integer> gameIds;

    @BeforeClass
    public static void setUp(){
        gameIds = new TreeSet<>();
    }

    public void persistGame(Game<?> game, int gameId) throws IOException {
        Persist.getInstance().persist(game, gameId);
        gameIds.add(gameId);
    }

    @Test
    public void testPersistAndRetrieveMulti() throws ControllerException, IOException, InvalidArgumentException, PlayersOutOfBoundException, WrongColorDeckException, WrongLevelDeckException, EmptyDeckException {
        int gameId = MessageControllerTestHelper.toReadyMulti();
        MultiPlayer game = (MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        persistGame(game, gameId);
        try {
            MultiPlayer gameRetrieved = (MultiPlayer) Persist.getInstance().retrieve(gameId);
            assertEquals(game, gameRetrieved);
        } catch (NoSuchGameException e) {
            logger.error("exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    public void testPersistAndRetrieveSingle() throws ControllerException, IOException, InvalidArgumentException, PlayersOutOfBoundException, WrongColorDeckException, WrongLevelDeckException, EmptyDeckException {
        int gameId = MessageControllerTestHelper.toReadySingle();
        SinglePlayer game = (SinglePlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        persistGame(game, gameId);
        try {
            SinglePlayer gameRetrieved = (SinglePlayer) Persist.getInstance().retrieve(gameId);
            assertEquals(game, gameRetrieved);
        } catch (NoSuchGameException e) {
            logger.error("exception: " + e.getMessage());
            fail();
        }
    }

    @AfterClass
    public static void removeSavedGames(){
        for(Integer i: gameIds){
            Persist.getInstance().remove(i);
        }
    }
}