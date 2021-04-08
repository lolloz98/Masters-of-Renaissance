package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LorenzoTest {
    SinglePlayer singlePlayer;

    @Before
    public void setUp(){
        singlePlayer = new SinglePlayer(new Player("play", 1));
    }




}