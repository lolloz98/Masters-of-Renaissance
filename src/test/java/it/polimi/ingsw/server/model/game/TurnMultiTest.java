package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class TurnMultiTest {
    private TurnMulti turnMulti;
    private MultiPlayer multiPlayer;

    @Before
    public void setUp() throws ModelException {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        multiPlayer = new MultiPlayer(players);
        turnMulti = new TurnMulti(players.get(0));
    }

    @Test
    public void testNextTurnNoEnd() throws ModelException {
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
    public void testNextTurnLastRound() throws ModelException {
        // from when last round is set to true, there should be enough turns to go back to the player 0
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti.getCurrentPlayer());
        turnMulti.setMainActionOccurred();
        multiPlayer.getTurn().getCurrentPlayer().getBoard().getFaithtrack().move(24, multiPlayer);
        TurnMulti turnMulti1 = turnMulti.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(1), turnMulti1.getCurrentPlayer());
        turnMulti1.setMainActionOccurred();
        TurnMulti turnMulti2 = turnMulti1.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(2), turnMulti2.getCurrentPlayer());
        turnMulti2.setMainActionOccurred();
        TurnMulti turnMulti3 = turnMulti2.nextTurn(multiPlayer);
        assertFalse(turnMulti3.getIsPlayable());
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testPerformMainAction() throws ModelException {
        // if i try to setMainActionOccurred twice, an exception should be thrown
        turnMulti.setMainActionOccurred();
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetProductionActivated() throws ModelException {
        turnMulti.setMainActionOccurred();
        turnMulti.setProductionsActivated(true);
    }

    @Test
    public void testSetProductionActivated1() throws ModelException {
        turnMulti.setProductionsActivated(true);
        turnMulti.setProductionsActivated(true);
    }

    @Test
    public void testSetProductionActivated2() throws ModelException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        turnMulti.setProductionsActivated(true);
        turnMulti.setProductionsActivated(false);
        assertTrue(turnMulti.isMainActionOccurred());
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = ProductionsResourcesNotFlushedException.class)
    public void testSetProductionActivated3() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        turnMulti.setProductionsActivated(true);
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = ProductionsResourcesNotFlushedException.class)
    public void testSetProductionActivated4() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setProductionsActivated(true);
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetProductionActivated5() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setMarketActivated(true);
        turnMulti.setProductionsActivated(true);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setMainActionOccurred();
        turnMulti.setMarketActivated(true);
    }

    @Test
    public void testSetMarketActivated2() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        turnMulti.setMarketActivated(true);
        turnMulti.setMarketActivated(false);
        assertTrue(turnMulti.isMainActionOccurred());
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = MarketTrayNotEmptyException.class)
    public void testSetMarketActivated3() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        turnMulti.setMarketActivated(true);
        TurnMulti turnMulti2 = turnMulti.nextTurn(multiPlayer);
    }

    @Test(expected = MarketTrayNotEmptyException.class)
    public void testSetMarketActivated4() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setMarketActivated(true);
        turnMulti.setMainActionOccurred();
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated5() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setProductionsActivated(true);
        turnMulti.setMarketActivated(true);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void testSetMarketActivated6() throws MarketTrayNotEmptyException, MainActionAlreadyOccurredException, ProductionsResourcesNotFlushedException {
        turnMulti.setMarketActivated(true);
        turnMulti.setMarketActivated(true);
    }
}