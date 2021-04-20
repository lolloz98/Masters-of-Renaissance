package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.GameAlreadyStartedException;
import it.polimi.ingsw.model.exception.NoSuchReservedIdException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameManagerTest {
    GameManager gameManager;

    @Before
    public void setUp(){
        gameManager = GameManager.getInstance();
    }

    @Test
    public void testSingleton(){
        assertEquals(gameManager, GameManager.getInstance());
    }

    @Test
    public void testNewSinglePlayer(){
        int id = gameManager.reserveId(1, "test");
        SinglePlayer singlePlayer = (SinglePlayer) gameManager.getGameFromMap(id);
        // TODO check if player in singlePlayer is correct
    }

    @Test
    public void testNewMultiPlayer(){
        int id = gameManager.reserveId(3, "first");
        try {
            gameManager.addPlayerToGame(id, "second");
            gameManager.addPlayerToGame(id, "third");
            MultiPlayer multiPlayer = (MultiPlayer) gameManager.getGameFromMap(id);
            // TODO check if players in multiPlayer are correct
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
        } catch (NoSuchReservedIdException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = GameAlreadyStartedException.class)
    public void testNewMultiPlayerTooManyPlayers() throws GameAlreadyStartedException, NoSuchReservedIdException {
        int id = gameManager.reserveId(3, "first");
        gameManager.addPlayerToGame(id, "second");
        gameManager.addPlayerToGame(id, "third");
        gameManager.addPlayerToGame(id, "fourth");
    }

}