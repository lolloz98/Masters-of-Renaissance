package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.LocalSingleGameHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class StartGUI {
    private static final Logger logger = LogManager.getLogger(StartGUI.class);

    public Button localBtn;
    public Button remoteBtn;

    public void startGUI(Stage stage){
        localBtn.setOnMouseClicked(mouseEvent -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/start_local.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("File not found: " + e);
            }
        });
    }

}
