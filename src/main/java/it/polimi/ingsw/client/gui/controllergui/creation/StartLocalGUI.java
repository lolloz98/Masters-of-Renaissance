package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.LocalSingleGameHandler;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class StartLocalGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(StartLocalGUI.class);
    public TextField nickname;
    public Button createGameBtn;


    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        ui.setGameHandler(new LocalSingleGameHandler());
        ui.newSinglePlayer();
        ui.getLocalGame().overrideObserver(this);
        createGameBtn.setOnMouseClicked(mouseEvent -> {
            try {
                Platform.runLater(() -> createGameBtn.setDisable(true));
                CreateGameMessage createGameMessage = InputHelper.getCreateGameMessage("1", nickname.getText());
                new Thread(() -> {
                    try {
                        ui.getGameHandler().dealWithMessage(createGameMessage);
                    } catch (IOException e) {
                        Platform.runLater(() -> createGameBtn.setDisable(false));
                        logger.error("something wrong happened while dealing with a message: " + e);
                    }
                }).start();
            }catch (IllegalArgumentException | InvalidNumberOfPlayersException e){
                Platform.runLater(() -> createGameBtn.setDisable(false));
                logger.warn("Something wrong happened while creating the message: " + e);
            }
        });
    }

    @Override
    public void notifyUpdate() {
        BuildGUI.getInstance().toBoard(stage, ui);
    }

    @Override
    public void notifyError() {
        Platform.runLater(() -> createGameBtn.setDisable(false));
        logger.debug("error notification");
    }
}
