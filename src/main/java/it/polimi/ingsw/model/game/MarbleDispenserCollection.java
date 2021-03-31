package it.polimi.ingsw.model.game;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementation of MarbleShufflerInferface that uses the Collections library.
 * It returns a random disposition of the market tray.
 */

public class MarbleDispenserCollection implements MarbleDispenserInterface {
    public ArrayList<Marble> getMarbles(){
        ArrayList<Marble> marbles = new ArrayList<>();
        int i, j;
        for (i = 0; i < 4; i++) marbles.add(new Marble(Resource.NOTHING));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SHIELD));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.ROCK));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SERVANT));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.GOLD));
        marbles.add(new Marble(Resource.FAITH));
        Collections.shuffle(marbles);
        return marbles;
    }
}
