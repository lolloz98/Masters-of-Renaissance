package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SinglePlayerTest {
    SinglePlayer singlePlayer;

    @Before
    public void setUp(){
        singlePlayer = new SinglePlayer(new Player("first", 1));
    }

    @Test
    public void testNextTurnLorenzoWins(){

    }


}