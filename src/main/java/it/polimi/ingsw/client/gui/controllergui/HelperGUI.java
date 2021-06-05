package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.gui.GUI;
import javafx.stage.Stage;

public final class HelperGUI {
    public static void handleGameDestruction(Stage stage, GUI ui){
        handleGameDestruction(stage, ui, "The game that you were playing has been destroyed");
    }

    public static void handleGameDestruction(Stage stage, GUI ui, String msg){
        ui.resetWhoIAmSeeingId();
        ui.getGameHandler().removeObservers();
        ((ServerListener) ui.getGameHandler()).closeConnection();
        ui.getLocalGame().removeAllObservers();
        BuildGUI.getInstance().toStartScene(stage, ui, msg);
    }
}
