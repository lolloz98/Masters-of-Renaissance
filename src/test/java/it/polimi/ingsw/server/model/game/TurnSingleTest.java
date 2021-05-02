package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.MainActionAlreadyOccurredException;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Before;
import org.junit.Test;

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
        assertFalse(turnSingle.isLorenzoPlaying());
        turnSingle.setMainActionOccurred();
        TurnSingle turnSingle1 = turnSingle.nextTurn(singlePlayer);
        assertTrue(turnSingle1.isLorenzoPlaying());
        turnSingle1.setMainActionOccurred();
        TurnSingle turnSingle2 = turnSingle1.nextTurn(singlePlayer);
        assertFalse(turnSingle2.isLorenzoPlaying());
    }

    @Test
    public void TestNextTurnEnd() {
        // test the end of the game
        assertFalse(turnSingle.isLorenzoPlaying());
        turnSingle.setMainActionOccurred();
        TurnSingle turnSingle1 = turnSingle.nextTurn(singlePlayer);
        assertTrue(turnSingle1.isLorenzoPlaying());
        turnSingle1.setMainActionOccurred();
        singlePlayer.getLorenzo().getFaithTrack().move(24, singlePlayer);
        TurnSingle turnSingle2 = turnSingle1.nextTurn(singlePlayer);
        assertFalse(turnSingle2.getIsPlayable());
    }

    @Test(expected = MainActionAlreadyOccurredException.class)
    public void TestPerformMainAction(){
        // if i try to setMainActionOccurred twice, an exception should be thrown
        turnSingle.setMainActionOccurred();
        turnSingle.setMainActionOccurred();
    }
}