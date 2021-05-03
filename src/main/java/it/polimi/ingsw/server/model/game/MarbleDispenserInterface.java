package it.polimi.ingsw.server.model.game;

import java.util.ArrayList;

/**
 * Class to decouple the shuffle of the market tray from the Collections library and make it easier to test
 */

public interface MarbleDispenserInterface {
    public ArrayList<Marble> getMarbles();
}