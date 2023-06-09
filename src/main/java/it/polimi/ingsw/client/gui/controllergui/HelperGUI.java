package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.creation.JoinGameGUI;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class with useful methods for GUI
 */
public final class HelperGUI {
    private static final Logger logger = LogManager.getLogger(HelperGUI.class);


    public static void handleGameDestruction(Stage stage, GUI ui){
        handleGameDestruction(stage, ui, "The game that you were playing has been destroyed");
    }

    /**
     * after cleaning the data in ui, it takes you back to the start scene
     */
    public static void handleGameDestruction(Stage stage, GUI ui, String msg){
        ui.resetWhoIAmSeeingId();

        if(ui.getGameHandler() != null) {
            ui.getGameHandler().removeObservers();
            ((ServerListener) ui.getGameHandler()).closeConnection();
        }
        else logger.error("the gameHandler was null");

        if(ui.getLocalGame() != null) {
            ui.getLocalGame().removeAllObservers();
        }
        BuildGUI.getInstance().toStartScene(stage, ui, msg);
    }
}
