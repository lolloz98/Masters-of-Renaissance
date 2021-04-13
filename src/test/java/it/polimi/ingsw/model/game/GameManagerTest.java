package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.NoSuchGameException;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameManagerTest {
    GameManager gameManager;

    @Before
    public void setUp(){
        gameManager = GameManager.getInstance();
    }

    @Test
    public void testSingleton(){
        assertEquals(gameManager, gameManager.getInstance());
    }

    @Test
    public void testGetNewSinglePlayer() {
        Player player = new Player("player", 1);
        int id1 = gameManager.getNewSinglePlayer(player);
        int id2 = gameManager.getNewSinglePlayer(player);
        assertNotEquals(id1, id2);
        gameManager.getGameFromMap(id1).getTurn();
    }

    @Test
    public void testGetNewMultiPlayer(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        int id1 = gameManager.getNewMultiPlayer(players);
        int id2 = gameManager.getNewMultiPlayer(players);
        assertNotEquals(id1, id2);
    }

    @Test
    public void testDestroyGame(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        int id = gameManager.getNewMultiPlayer(players);
        try{
            gameManager.removeGame(id);
        } catch (NoSuchGameException e) {
            e.printStackTrace();
        }
        assertEquals(gameManager.getGameFromMap(id),null);
    }
}