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
    private FaithTrack ft;

    @Before
    public void setUp() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("first", 1));
        players.add(new Player("second", 2));
        players.add(new Player("third", 3));
        players.add(new Player("fourth", 4));
        multiPlayer = new MultiPlayer(players);
        singlePLayer=new SinglePlayer(players.get(0));
        int i;
        for(i=0;i<4;i++){
            assertEquals(0, players.get(i).getBoard().getFaithtrack().getPosition());
            assertEquals(2, players.get(i).getBoard().getFaithtrack().getFigures()[0].getLevel());
            assertEquals(3, players.get(i).getBoard().getFaithtrack().getFigures()[1].getLevel());
            assertEquals(4, players.get(i).getBoard().getFaithtrack().getFigures()[2].getLevel());
        }
        ft=players.get(0).getBoard().getFaithtrack();
    }

    @Test
    public void moveTestMulti(){
        int s1=1, s2=2, s3=3, s4=5;
        int i;
        multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().move(s1,multiPlayer);
       assertEquals(1,multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());
        for(i=0;i<4;i++)
            assertEquals(Figurestate.INACTIVE, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getFigures()[0].getState());
        multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().move(s2,multiPlayer);
        assertEquals(3,multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());
        multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().move(s3,multiPlayer);
        assertEquals(6,multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());
        for(i=0;i<4;i++){
            assertEquals(Figurestate.INACTIVE, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getFigures()[0].getState());
        }
        multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().move(s4,multiPlayer);
        assertEquals(11,multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getPosition());
        assertEquals(Figurestate.ACTIVE, multiPlayer.getPlayers().get(0).getBoard().getFaithtrack().getFigures()[0].getState());
        for(i=1;i<4;i++){
            assertEquals(Figurestate.DISCARDED, multiPlayer.getPlayers().get(i).getBoard().getFaithtrack().getFigures()[0].getState());
        }
        //fare controllo andando avanti con il ft di un altro player




    }

    @Test
    public void moveTestSingle(){

    }


    @After
    public void tearDown(){}



   }
