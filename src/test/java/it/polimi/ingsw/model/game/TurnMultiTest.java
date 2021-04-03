package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;
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
    public void TestNextTurnNoEnd(){
        // test the normal cycle of players
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti.getCurrentPlayer());
        TurnMulti turnMulti1 = turnMulti.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(1), turnMulti1.getCurrentPlayer());
        TurnMulti turnMulti2 = turnMulti1.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(2), turnMulti2.getCurrentPlayer());
        TurnMulti turnMulti3 = turnMulti2.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti3.getCurrentPlayer());
    }

    @Test
    public void TestNextTurnLastRound(){
        // from when last round is set to true, there should be enough turns to go back to the player 0
        assertEquals(multiPlayer.getPlayers().get(0), turnMulti.getCurrentPlayer());
        multiPlayer.setLastRound(true);
        TurnMulti turnMulti1 = turnMulti.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(1), turnMulti1.getCurrentPlayer());
        TurnMulti turnMulti2 = turnMulti1.nextTurn(multiPlayer);
        assertEquals(multiPlayer.getPlayers().get(2), turnMulti2.getCurrentPlayer());
        TurnMulti turnMulti3 = turnMulti2.nextTurn(multiPlayer);
        assertEquals(null, turnMulti3);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void TestPerformMainAction(){
        // if i try to setMainActionOccurred twice, an exception should be thrown
        turnMulti.setMainActionOccurred();
        turnMulti.setMainActionOccurred();
    }
}