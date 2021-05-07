package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ControllerManagerTest {
    ControllerManager controllerManager;

    @Before
    public void setUp(){
        controllerManager = ControllerManager.getInstance();
    }

    @Test
    public void testSingleton(){
        assertEquals(controllerManager, ControllerManager.getInstance());
    }

    @Test
    public void testNewSinglePlayer() throws ControllerException {
        int id= controllerManager.reserveIdForNewGame(new CreateGameMessage(1, "aniello")).getFirst();
        ControllerActions controller = controllerManager.getControllerFromMap(id);
        // TODO check if player in singlePlayer is correct
    }

    @Test
    public void testNewMultiPlayer()  {
        int id = 0;
        try {
            id = controllerManager.reserveIdForNewGame(new CreateGameMessage(3,"creator")).getFirst();
        } catch (ControllerException e) {
            fail();
        }
        try {
            controllerManager.joinGame(new JoinGameMessage(id,"second"));
            controllerManager.joinGame(new JoinGameMessage(id,"third"));
        } catch (ControllerException e) {
            e.printStackTrace();
            fail();;
        }
        MultiPlayer multiPlayer = (MultiPlayer) controllerManager.getControllerFromMap(id).getGame();
            // TODO check if players in multiPlayer are correct

    }

    @Test (expected = ControllerException.class)
    public void testNewMultiPlayerTooManyPlayers() throws  ControllerException {
        int id= controllerManager.reserveIdForNewGame(new CreateGameMessage(3,"first")).getFirst();
        controllerManager.joinGame(new JoinGameMessage(id,"second"));
        controllerManager.joinGame(new JoinGameMessage(id,"Third"));
        controllerManager.joinGame(new JoinGameMessage(id,"fourth"));
    }

}