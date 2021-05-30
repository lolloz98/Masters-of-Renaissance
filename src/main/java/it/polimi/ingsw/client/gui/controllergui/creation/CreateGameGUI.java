package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CreateGameGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(StartLocalGUI.class);
    public TextField nickname;
    public TextField playerNumbers;
    public Button createGameBtn;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        createGameBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> createGameBtn.setDisable(true));
            String nick = nickname.getText();
            try {
                CreateGameMessage createGameMessage = InputHelper.getCreateGameMessage(playerNumbers.getText(), nick);
                new Thread(() -> {
                    try {
                        if(createGameMessage.getPlayersNumber() == 1) ui.newSinglePlayer();
                        else ui.newMultiPlayer();

                        ui.getLocalGame().overrideObserver(this);
                        ui.getGameHandler().dealWithMessage(createGameMessage);
                    } catch (IOException e) {
                        logger.debug("something wrong happened while dealing with a message: " + e);
                        Platform.runLater(() -> createGameBtn.setDisable(false));
                    }
                }).start();
            }catch (IllegalArgumentException | InvalidNumberOfPlayersException e){
                logger.debug("something went wrong: " + e);
                Platform.runLater(() -> {createGameBtn.setDisable(false);});
            }
        });
    }

    @Override
    public void notifyUpdate() {
        logger.debug("in notify update");
        if(ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS){
            BuildGUI.getInstance().toWait(stage, ui);
        }else{
            BuildGUI.getInstance().toBoard(stage, ui);
        }
    }

    @Override
    public void notifyError() {
        logger.debug("error notification");
        Platform.runLater(() -> {createGameBtn.setDisable(false);});
    }
}

