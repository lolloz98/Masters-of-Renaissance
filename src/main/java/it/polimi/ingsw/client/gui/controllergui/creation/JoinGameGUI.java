package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import it.polimi.ingsw.client.gui.controllergui.HelperGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class JoinGameGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(JoinGameGUI.class);

    public TextField gameId;
    public TextField nickname;
    public Button joinGameBtn;
    public Label errorLbl;

    @Override
    public void notifyUpdate() {
        logger.debug("in notify update");
        if(ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS){
            BuildGUI.getInstance().toWait(stage, ui);
        }else if(ui.getLocalGame().getState() == LocalGameState.DESTROYED){
            HelperGUI.handleGameDestruction(stage, ui);
        }
        else {
            BuildGUI.getInstance().toBoard(stage, ui);
        }
    }

    @Override
    public void notifyError() {
        logger.debug("error notification");
        Platform.runLater(() -> {
            joinGameBtn.setDisable(false);
            errorLbl.setVisible(true);
        });
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        joinGameBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> {
                joinGameBtn.setDisable(true);
                errorLbl.setVisible(false);
            });
            String nick = nickname.getText();
            try {
                JoinGameMessage joinGameMessage = InputHelper.getJoinGameMessage(gameId.getText(), nick);
                new Thread(() -> {
                    try {
                        ui.newMultiPlayer();
                        ui.getLocalGame().overrideObserver(this);
                        ui.getLocalGame().getError().addObserver(this);
                        ui.getGameHandler().dealWithMessage(joinGameMessage);
                    } catch (IOException e) {
                        logger.debug("something wrong happened while dealing with a message: " + e);
                        Platform.runLater(() -> {joinGameBtn.setDisable(false);});
                    }
                }).start();
            }catch (IllegalArgumentException e){
                logger.debug("something went wrong: " + e);
                Platform.runLater(() -> joinGameBtn.setDisable(false));
            }
        });
    }
}
