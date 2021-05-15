package it.polimi.ingsw.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to decouple the shuffle of the market tray from the Collections library and make it easier to test
 */

public interface MarbleDispenserInterface extends Serializable {
    public ArrayList<Marble> getMarbles();
}
