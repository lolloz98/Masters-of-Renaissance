package it.polimi.ingsw.server.model.game;

import java.util.ArrayList;

/**
 * Implementation of MarbleShufflerInferface that that can be used for testing.
 * It returns a pre-determined disposition of the market tray.
 */

public class MarbleDispenserTester implements MarbleDispenserInterface {
    private static final long serialVersionUID = 1020L;

    public ArrayList<Marble> getMarbles(){
        ArrayList<Marble> marbles = new ArrayList<>();
        int i;
        for (i = 0; i < 4; i++) marbles.add(new Marble(Resource.NOTHING));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SHIELD));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.ROCK));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SERVANT));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.GOLD));
        marbles.add(new Marble(Resource.FAITH));
        return marbles;
    }
}
