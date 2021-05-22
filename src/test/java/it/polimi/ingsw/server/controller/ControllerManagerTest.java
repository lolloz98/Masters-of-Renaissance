package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
        String name = "Aniello";
        int id= controllerManager.reserveIdForNewGame(new CreateGameMessage(1, name), new AnswerListener(null)).getFirst();
        ControllerActionsServer<?> controller = controllerManager.getControllerFromMap(id);
        assertNotNull(controller.getGame());
        assertEquals(name, ((SinglePlayer) controller.getGame()).getPlayer().getName());
    }

    @Test
    public void testNewMultiPlayer() throws ControllerException {
        int gameId = 0;
        ArrayList<String> names = new ArrayList<>(){{
            add("first");
            add("second");
            add("third");
        }};

        gameId = controllerManager.reserveIdForNewGame(new CreateGameMessage(3,names.get(0)), new AnswerListener(null)).getFirst();
        assertNull(controllerManager.getControllerFromMap(gameId).getGame());

        controllerManager.joinGame(new JoinGameMessage(gameId,names.get(1)));
        assertNull(controllerManager.getControllerFromMap(gameId).getGame());

        controllerManager.joinGame(new JoinGameMessage(gameId,names.get(2)));

        MultiPlayer multiPlayer = (MultiPlayer) controllerManager.getControllerFromMap(gameId).getGame();
        assertNotNull(multiPlayer);
        for(Player p: multiPlayer.getPlayers()){
            if(!names.contains(p.getName())) fail();
            names.remove(p.getName());
        }

        try{
            // try to get controller when gameId not yet reserved
            controllerManager.getControllerFromMap(gameId + 1);
            fail();
        }catch (ControllerException ignore){}
    }

    @Test (expected = ControllerException.class)
    public void testNewMultiPlayerTooManyPlayers() throws  ControllerException {
        int id= controllerManager.reserveIdForNewGame(new CreateGameMessage(3,"first"), new AnswerListener(null)).getFirst();
        controllerManager.joinGame(new JoinGameMessage(id,"second"));
        controllerManager.joinGame(new JoinGameMessage(id,"Third"));
        controllerManager.joinGame(new JoinGameMessage(id,"fourth"));
    }

}