package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;

public class StartRemote implements ControllerGUI {
    private static final Logger logger = LogManager.getLogger(StartRemote.class);

    public TextField serverIp;
    public TextField portNumber;
    public Button connectBtn;
    public Label errorMsg;

    @Override
    public void setUp(Stage stage, GUI ui) {
        connectBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> {
                logger.debug("making errorMsg invisible");
                errorMsg.setVisible(false);});

            String ip = serverIp.getText();
            try {
                int port = Integer.parseInt(portNumber.getText());
                Thread worker = new Thread(() -> {
                    try {
                        logger.debug("Connecting to server");
                        ui.setGameHandler(new ServerListener(ip, port));
                        BuildGUI.getInstance().toCreateGame(stage, ui);
                    } catch (IllegalArgumentException | IOException e) {
                        Platform.runLater(() -> {
                            logger.debug("making errorMsg visible");
                            errorMsg.setText("Something went wrong, please try again");
                            errorMsg.setVisible(true);
                        });
                    }
                });

                worker.start();
            }catch (NumberFormatException e){
                Platform.runLater(() -> {
                    logger.debug("making errorMsg visible");
                    errorMsg.setText("The port must be a number!");
                    errorMsg.setVisible(true);
                });
            }
        });
    }
}
