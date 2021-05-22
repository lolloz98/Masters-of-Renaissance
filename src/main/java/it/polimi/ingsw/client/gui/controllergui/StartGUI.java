package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class StartGUI implements ControllerGUI {
    private static final Logger logger = LogManager.getLogger(StartGUI.class);

    public Button localBtn;
    public Button remoteBtn;

    public void setUp(Stage stage, Parent root, GUI ui){
        localBtn.setOnMouseClicked(mouseEvent -> {
            logger.debug(localBtn + " clicked");
            BuildGUI.getInstance().toStartLocal(stage, ui);
        });

        remoteBtn.setOnMouseClicked(mouseEvent -> {
            logger.debug(localBtn + " clicked");
            BuildGUI.getInstance().toStartRemote(stage, ui);
        });
    }

}
