package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.MultiPlayerTest;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PersistTest {
    private static final Logger logger = LogManager.getLogger(Persist.class);

    @Test
    public void testPersistAndRetrieveMulti() throws ControllerException, IOException, InvalidArgumentException, PlayersOutOfBoundException, WrongColorDeckException, WrongLevelDeckException, EmptyDeckException {
        int gameId = MessageControllerTestHelper.toReadyMulti();
        MultiPlayer game = (MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        Persist.getInstance().persist(game, 0);
        try {
            MultiPlayer gameRetrieved = (MultiPlayer) Persist.getInstance().retrieve(0);
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
        Persist.getInstance().persist(game, 0);
        try {
            SinglePlayer gameRetrieved = (SinglePlayer) Persist.getInstance().retrieve(0);
            assertEquals(game, gameRetrieved);
        } catch (NoSuchGameException e) {
            logger.error("exception: " + e.getMessage());
            fail();
        }
    }

}