package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class StartRemoteGUI implements ControllerGUI {
    private static final Logger logger = LogManager.getLogger(StartRemoteGUI.class);

    public TextField serverIp;
    public TextField portNumber;
    public Button connectBtn;
    public Label errorMsg;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
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
                        ServerListener serverListener = new ServerListener(ip, port);

                        ui.setGameHandler(serverListener);

                        BuildGUI.getInstance().toJoinOrCreate(stage, ui);
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
