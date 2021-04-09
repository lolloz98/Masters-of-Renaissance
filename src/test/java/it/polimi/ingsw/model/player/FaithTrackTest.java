package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.SinglePlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

//TODO:

public class FaithTrackTest {
    private SinglePlayer singlePLayer;
    private MultiPlayer multiPlayer;
    private ArrayList<FaithTrack> ft;

    @Before
    public void setUp() {
        ArrayList<Player> players = new ArrayList<>();
        ft = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        players.add(new Player("fourth", 4));
        for (int i = 0; i < players.size(); i++) {
            ft.add(players.get(i).getBoard().getFaithtrack());
        }
        multiPlayer = new MultiPlayer(players);
        singlePLayer = new SinglePlayer(players.get(0));
        int i;
        for (i = 0; i < 4; i++) {
            assertEquals(0, players.get(i).getBoard().getFaithtrack().getPosition());
            assertEquals(2, players.get(i).getBoard().getFaithtrack().getFigures()[0].getLevel());
            assertEquals(3, players.get(i).getBoard().getFaithtrack().getFigures()[1].getLevel());
            assertEquals(4, players.get(i).getBoard().getFaithtrack().getFigures()[2].getLevel());
        }
    }

    @Test
    public void moveTestMulti() {
        int s1 = 1, s2 = 2, s3 = 3, s4 = 5, s5 = 15;
        int i;
        ft.get(0).move(s1, multiPlayer);
        assertEquals(1, ft.get(0).getPosition());
        for (i = 0; i < 4; i++) {
            assertEquals(Figurestate.INACTIVE, ft.get(i).getFigures()[0].getState());
        }

        ft.get(0).move(s2, multiPlayer);
        assertEquals(3, ft.get(0).getPosition());
        ft.get(0).move(s3, multiPlayer);
        assertEquals(6, ft.get(0).getPosition());
        for (i = 0; i < 4; i++) {
            assertEquals(Figurestate.INACTIVE, ft.get(i).getFigures()[0].getState());
        }
        ft.get(0).move(s4, multiPlayer);
        assertEquals(11, ft.get(0).getPosition());
        assertEquals(Figurestate.ACTIVE, ft.get(0).getFigures()[0].getState());
        for (i = 1; i < 4; i++) {
            assertEquals(Figurestate.DISCARDED, ft.get(i).getFigures()[0].getState());
            assertEquals(0, ft.get(i).getPosition());
        }
        ft.get(1).move(9, multiPlayer);
        assertEquals(9, ft.get(1).getPosition());
        assertEquals(Figurestate.ACTIVE, ft.get(0).getFigures()[0].getState());
        for (i = 1; i < 4; i++) {
            assertEquals(Figurestate.DISCARDED, ft.get(i).getFigures()[0].getState());
        }
        for (i = 0; i < 4; i++) {
            for (int j = 1; j < 3; j++) {
                assertEquals(Figurestate.INACTIVE, ft.get(i).getFigures()[j].getState());
            }
        }
        ft.get(0).move(s1, multiPlayer);
        assertEquals(12, ft.get(0).getPosition());
        ft.get(1).move(s5, multiPlayer);
        assertEquals(24, ft.get(1).getPosition());
        assertTrue(ft.get(1).isEndReached());
    }

    @Test
    public void moveTestSingle() {

    }


    @After
    public void tearDown() {
    }


}
