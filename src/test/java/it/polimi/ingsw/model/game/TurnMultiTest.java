package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.model.exception.MarketTrayNotEmptyException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;
import it.polimi.ingsw.model.exception.ProductionsResourcesNotFlushedException;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class TurnMultiTest {
    private TurnMulti turnMulti;
    private MultiPlayer multiPlayer;

    @Before
    public void setUp() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        multiPlayer = new MultiPlayer(players);
        turnMulti = new TurnMulti(players.get(0));
    }

    @Test
    public void testNextTurnNoEnd(){
        // test the normal cycle of players
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti.getCurrentPlayer());
        turnMulti.setMainActionOccurred();
        TurnMulti turnMulti1 = turnMulti.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(1), turnMulti1.getCurrentPlayer());
        turnMulti1.setMainActionOccurred();
        TurnMulti turnMulti2 = turnMulti1.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(2), turnMulti2.getCurrentPlayer());
        turnMulti2.setMainActionOccurred();
        TurnMulti turnMulti3 = turnMulti2.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti3.getCurrentPlayer());
    }

    @Test
    public void testNextTurnLastRound(){
        // from when last round is set to true, there should be enough turns to go back to the player 0
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti.getCurrentPlayer());
        turnMulti.setMainActionOccurred();
        multiPlayer.setLastRound(true);
        TurnMulti turnMulti1 = turnMulti.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(1), turnMulti1.getCurrentPlayer());
        turnMulti1.setMainActionOccurred();
        TurnMulti turnMulti2 = turnMulti1.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(2), turnMulti2.getCurrentPlayer());
        turnMulti2.setMainActionOccurred();
        TurnMulti turnMulti3 = turnMulti2.nextTurn(multiPlayer);
        assertEquals(null, turnMulti3);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testPerformMainAction(){
        // if i try to setMainActionOccurred twice, an exception should be thrown
        turnMulti.setMainActionOccurred();
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetProductionActivated(){
        turnMulti.setMainActionOccurred();
        turnMulti.setProductionsActivated(true);
    }

    @Test
    public void testSetProductionActivated1(){
        turnMulti.setProductionsActivated(true);
        turnMulti.setProductionsActivated(true);
    }

    @Test
    public void testSetProductionActivated2(){
        turnMulti.setProductionsActivated(true);
        turnMulti.setProductionsActivated(false);
        assertTrue(turnMulti.isMainActionOccurred());
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = ProductionsResourcesNotFlushedException.class)
    public void testSetProductionActivated3(){
        turnMulti.setProductionsActivated(true);
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = ProductionsResourcesNotFlushedException.class)
    public void testSetProductionActivated4(){
        turnMulti.setProductionsActivated(true);
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetProductionActivated5(){
        turnMulti.setMarketActivated(true);
        turnMulti.setProductionsActivated(true);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated(){
        turnMulti.setMainActionOccurred();
        turnMulti.setMarketActivated(true);
    }

    @Test
    public void testSetMarketActivated2(){
        turnMulti.setMarketActivated(true);
        turnMulti.setMarketActivated(false);
        assertTrue(turnMulti.isMainActionOccurred());
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = MarketTrayNotEmptyException.class)
    public void testSetMarketActivated3(){
        turnMulti.setMarketActivated(true);
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = MarketTrayNotEmptyException.class)
    public void testSetMarketActivated4(){
        turnMulti.setMarketActivated(true);
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated5(){
        turnMulti.setProductionsActivated(true);
        turnMulti.setMarketActivated(true);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated6(){
        turnMulti.setMarketActivated(true);
        turnMulti.setMarketActivated(true);
    }
}