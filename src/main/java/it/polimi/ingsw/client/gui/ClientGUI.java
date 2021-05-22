package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.controllergui.StartGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ClientGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
        Parent root = fxmlLoader.load();
        StartGUI controller = fxmlLoader.getController();
        controller.startGUI(stage);

        stage.setTitle("Master of Renaissance");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
