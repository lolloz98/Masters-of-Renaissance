package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.ClosingConnectionListenerGUI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class StartRemoteGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(StartRemoteGUI.class);

    public TextField serverIp;
    public TextField portNumber;
    public Button connectBtn;
    public Label errorMsg;

    private void uiOnError(String message){
        Platform.runLater(() -> {
            logger.debug("making errorMsg invisible");
            errorMsg.setText(message);
            errorMsg.setVisible(true);
            connectBtn.setDisable(false);
        });
    }

    private void uiOnClick(){
        Platform.runLater(() -> {
            logger.debug("making errorMsg invisible");
            errorMsg.setVisible(false);
            connectBtn.setDisable(true);
        });
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        connectBtn.setOnMouseClicked(mouseEvent -> {
            uiOnClick();

            String ip = serverIp.getText();
            try {
                int port = Integer.parseInt(portNumber.getText());
                Thread worker = new Thread(() -> {
                    try {
                        logger.debug("Connecting to server");
                        ServerListener serverListener = new ServerListener(ip, port);
                        ui.setGameHandler(serverListener, new ClosingConnectionListenerGUI(stage, ui));

                        BuildGUI.getInstance().toJoinOrCreate(stage, ui);
                    } catch (IllegalArgumentException | IOException e) {
                        Platform.runLater(() -> {
                            uiOnError("Something went wrong, please try again");
                        });
                    }
                });

                worker.start();
            }catch (NumberFormatException e){
                Platform.runLater(() -> {
                    uiOnError("The port must be a number!");
                });
            }
        });
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }
}
