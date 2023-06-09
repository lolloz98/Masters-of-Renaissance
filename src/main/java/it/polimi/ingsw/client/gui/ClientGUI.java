package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        GUI ui = new GUI();
        BuildGUI.getInstance().toStartScene(stage, ui);
    }
}
