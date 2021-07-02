package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.ClosingConnectionListenerGUI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.messages.requests.RejoinMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RejoinGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(RejoinGUI.class);

    public Button rejoinBtn;
    public Label messageLbl;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        rejoinBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> {
                rejoinBtn.setDisable(true);
                messageLbl.setText("");
            });
            ServerListener old = (ServerListener) ui.getGameHandler();
            new Thread(() -> {
                try {
                    ui.setGameHandler(new ServerListener(old.getAddress(), old.getPort(), new Observer() {
                        @Override
                        public void notifyUpdate() {
                            try {
                                ui.getGameHandler().dealWithMessage(new RejoinMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId()));
                            } catch (IOException e) {
                                connectionFailed(e);
                            }
                        }
                        @Override
                        public void notifyError() {}
                    }), new ClosingConnectionListenerGUI(stage, ui));
                    ui.getGameHandler().setLocalGame(ui.getLocalGame());
                } catch (IOException e) {
                    connectionFailed(e);
                }
            }).start();
        });
    }

    private void connectionFailed(IOException e){
        logger.warn("error while connecting to the server: " + e);
        Platform.runLater(() -> {
            rejoinBtn.setDisable(false);
            messageLbl.setText("Error while connecting to the server!");
        });
    }

    @Override
    public void notifyUpdate() {
        logger.debug("Status retrieved successfully: to board");
        ui.getLocalGame().removeAllObservers();
        BuildGUI.getInstance().toBoard(stage, ui);
    }

    /**
     * We receive a notification of an error, only if we connect to the server but we cannot rejoin (for any reason)
     * the game.
     */
    @Override
    public void notifyError() {
        HelperGUI.handleGameDestruction(stage, ui);
    }
}
