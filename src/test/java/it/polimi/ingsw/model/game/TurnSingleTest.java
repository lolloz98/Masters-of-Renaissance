package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnSingleTest {
    SinglePlayer singlePlayer;
    TurnSingle turnSingle;

    @Before
    public void setUp() {
        Player player = new Player("player", 0);
        singlePlayer = new SinglePlayer(player);
        turnSingle = new TurnSingle(false);
    }

    @Test
    public void TestNextTurnNoEnd() {
        // test the normal cycle of player and lorenzo
        assertEquals(false, turnSingle.isLorenzoPlaying());
        TurnSingle turnSingle1 = turnSingle.nextTurn(singlePlayer);
        assertEquals(true, turnSingle1.isLorenzoPlaying());
        TurnSingle turnSingle2 = turnSingle1.nextTurn(singlePlayer);
        assertEquals(false, turnSingle2.isLorenzoPlaying());
    }

    @Test
    public void TestNextTurnEnd() {
        // test the end of the game
        assertEquals(false, turnSingle.isLorenzoPlaying());
        TurnSingle turnSingle1 = turnSingle.nextTurn(singlePlayer);
        assertEquals(true, turnSingle1.isLorenzoPlaying());
        singlePlayer.setLastTurn(true);
        TurnSingle turnSingle2 = turnSingle1.nextTurn(singlePlayer);
        assertEquals(null, turnSingle2);
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void TestPerformMainAction(){
        // if i try to setMainActionOccurred twice, an exception should be thrown
        turnSingle.setMainActionOccurred();
        turnSingle.setMainActionOccurred();
    }
}