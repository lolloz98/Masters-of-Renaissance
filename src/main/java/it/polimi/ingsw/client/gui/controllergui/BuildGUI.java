package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BuildGUI {
    private static final Logger logger = LogManager.getLogger(BuildGUI.class);

    private static BuildGUI INSTANCE = null;

    private BuildGUI(){}

    public static BuildGUI getInstance(){
        if(INSTANCE != null) return INSTANCE;
        return INSTANCE = new BuildGUI();
    }

    public void toStartScene(Stage stage, GUI ui) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
            try {
                logger.debug("To start scene");
                Parent root = fxmlLoader.load();
                StartGUI controller = fxmlLoader.getController();
                controller.setUp(stage, ui);

                stage.setTitle("Master of Renaissance");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toCreateGame(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/create_game.fxml"));
            try {
                Parent root = fxmlLoader.load();
                CreateGameControllerGUI controller = fxmlLoader.getController();
                controller.setUp(stage, ui);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toStartLocal(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start_local.fxml"));
            try {
                Parent root = fxmlLoader.load();
                StartLocal controller = fxmlLoader.getController();
                controller.setUp(stage, ui);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }

    public void toStartRemote(Stage stage, GUI ui){
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/start_remote.fxml"));
            try {
                Parent root = fxmlLoader.load();
                StartRemote controller = fxmlLoader.getController();
                controller.setUp(stage, ui);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.error("file not found: " + e);
            }
        });
    }
}
