package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.EndAlreadyReachedException;
import it.polimi.ingsw.model.exception.InvalidStepsException;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.SinglePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
        int s1 = 1, s2 = 2, s3 = 3, s4 = 5, s5 = 15,s6=9, illegalStep=-2;
        int i;
        ft.get(0).move(s1, multiPlayer);
        assertEquals(1, ft.get(0).getPosition());
        for (i = 0; i < 4; i++) {
            assertEquals(FigureState.INACTIVE, ft.get(i).getFigures()[0].getState());
        }
        ft.get(0).move(s2, multiPlayer);
        assertEquals(3, ft.get(0).getPosition());
        ft.get(0).move(s3, multiPlayer);
        assertEquals(6, ft.get(0).getPosition());
        for (i = 0; i < 4; i++) {
            assertEquals(FigureState.INACTIVE, ft.get(i).getFigures()[0].getState());
        }
        ft.get(0).move(s4, multiPlayer);
        assertEquals(11, ft.get(0).getPosition());
        assertEquals(FigureState.ACTIVE, ft.get(0).getFigures()[0].getState());
        for (i = 1; i < 4; i++) {
            assertEquals(FigureState.DISCARDED, ft.get(i).getFigures()[0].getState());
            assertEquals(0, ft.get(i).getPosition());
        }
        ft.get(1).move(s6, multiPlayer);
        assertEquals(9, ft.get(1).getPosition());
        assertEquals(FigureState.ACTIVE, ft.get(0).getFigures()[0].getState());
        for (i = 1; i < 4; i++) {
            assertEquals(FigureState.DISCARDED, ft.get(i).getFigures()[0].getState());
        }
        for (i = 0; i < 4; i++) {
            for (int j = 1; j < 3; j++) {
                assertEquals(FigureState.INACTIVE, ft.get(i).getFigures()[j].getState());
            }
        }
        ft.get(0).move(s1, multiPlayer);
        assertEquals(12, ft.get(0).getPosition());
        ft.get(1).move(s5, multiPlayer);
        assertEquals(24, ft.get(1).getPosition());
        assertTrue(ft.get(1).isEndReached());
        assertEquals(FigureState.ACTIVE,ft.get(0).getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,ft.get(0).getFigures()[1].getState());
        assertEquals(FigureState.DISCARDED,ft.get(0).getFigures()[2].getState());
        assertEquals(FigureState.DISCARDED,ft.get(1).getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,ft.get(1).getFigures()[1].getState());
        assertEquals(FigureState.ACTIVE,ft.get(1).getFigures()[2].getState());
        for(i=2;i<4;i++){
            for(int j=0;j<3;j++){
                assertEquals(FigureState.DISCARDED,ft.get(i).getFigures()[j].getState());
            }
        }
    }

    @Test(expected = InvalidStepsException.class)
    public void moveTestMultiInvalid() {
        int illegalStep=-2;
        ft.get(2).move(illegalStep, multiPlayer);
    }

    @Test(expected = InvalidStepsException.class)
    public void moveTestMultiInvalid2() {
        int illegalStep=0;
        ft.get(2).move(illegalStep, multiPlayer);
    }

    @Test
    public void moveTest1(){
        int i;
        int s1=1, s2=24;
        ft.get(0).move(s2,multiPlayer);
        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if(i==0)
                    assertEquals(FigureState.ACTIVE, ft.get(i).getFigures()[j].getState());
                else
                    assertEquals(FigureState.DISCARDED, ft.get(i).getFigures()[j].getState());
            }
        }
    }

    @Test(expected = EndAlreadyReachedException.class)
    public void moveTestEndAlreadyReached(){
        int s1=1, s2=24;
        ft.get(0).move(s2,multiPlayer);
        ft.get(0).move(s1,multiPlayer);
    }

    @Test
    public void moveTestSingle() {
        FaithTrack lorenzoFt=singlePLayer.getLorenzo().getFaithTrack();
        FaithTrack playerFt=ft.get(0);
        int s1 = 1, s2 = 2, s3 = 3, s4 = 5, s5 = 15,s6=9, s7=7;
        playerFt.move(s1,singlePLayer);
        lorenzoFt.move(s3,singlePLayer);
        assertEquals(1,playerFt.getPosition());
        assertEquals(3,lorenzoFt.getPosition());
        playerFt.move(s4,singlePLayer);
        lorenzoFt.move(s4,singlePLayer);
        assertEquals(6,playerFt.getPosition());
        assertEquals(8,lorenzoFt.getPosition());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[0].getState());
        assertEquals(FigureState.INACTIVE,playerFt.getFigures()[1].getState());
        assertEquals(FigureState.INACTIVE,lorenzoFt.getFigures()[1].getState());
        assertEquals(FigureState.INACTIVE,playerFt.getFigures()[2].getState());
        assertEquals(FigureState.INACTIVE,lorenzoFt.getFigures()[2].getState());
        playerFt.move(s6,singlePLayer);
        lorenzoFt.move(s7,singlePLayer);
        assertEquals(15,playerFt.getPosition());
        assertEquals(15,lorenzoFt.getPosition());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[0].getState());
        assertEquals(FigureState.INACTIVE,playerFt.getFigures()[1].getState());
        assertEquals(FigureState.INACTIVE,lorenzoFt.getFigures()[1].getState());
        assertEquals(FigureState.INACTIVE,playerFt.getFigures()[2].getState());
        assertEquals(FigureState.INACTIVE,lorenzoFt.getFigures()[2].getState());
        lorenzoFt.move(s4, singlePLayer);
        assertEquals(15,playerFt.getPosition());
        assertEquals(20,lorenzoFt.getPosition());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[1].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[1].getState());
        assertEquals(FigureState.INACTIVE,playerFt.getFigures()[2].getState());
        assertEquals(FigureState.INACTIVE,lorenzoFt.getFigures()[2].getState());
        lorenzoFt.move(s4,singlePLayer);
        assertEquals(15,playerFt.getPosition());
        assertEquals(24,lorenzoFt.getPosition());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[0].getState());
        assertEquals(FigureState.ACTIVE,playerFt.getFigures()[1].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[1].getState());
        assertEquals(FigureState.DISCARDED,playerFt.getFigures()[2].getState());
        assertEquals(FigureState.ACTIVE,lorenzoFt.getFigures()[2].getState());
    }

    @Test
    public void getVictoryPointsTest(){
        int s1 = 1, s2 = 2, s3 = 3, s4 = 5, s5 = 15, s6=9;
        ft.get(0).move(s1, multiPlayer);
        ft.get(0).move(s2, multiPlayer);
        ft.get(0).move(s3, multiPlayer);
        ft.get(0).move(s4, multiPlayer);
        ft.get(1).move(s6, multiPlayer);
        ft.get(0).move(s1, multiPlayer);
        ft.get(1).move(s5, multiPlayer);
        assertEquals(6+2+3,ft.get(0).getVictoryPoints());
        assertEquals(20+3+4, ft.get(1).getVictoryPoints());
        assertEquals(0, ft.get(2).getVictoryPoints());
        assertEquals(0, ft.get(3).getVictoryPoints());
    }
}
