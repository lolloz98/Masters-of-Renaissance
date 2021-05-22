package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.LocalSingleGameHandler;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class StartLocalGUI implements ControllerGUI, Observer {
    private static final Logger logger = LogManager.getLogger(StartLocalGUI.class);
    public TextField nickname;
    public Button createGameBtn;
    private Stage stage;
    private GUI ui;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        ui.setGameHandler(new LocalSingleGameHandler());
        ui.newSinglePlayer();
        ui.getLocalGame().overrideObserver(this);
        createGameBtn.setOnMouseClicked(mouseEvent -> {
            String nick = nickname.getText();
            new Thread(() ->{
                try {
                    ui.getGameHandler().dealWithMessage(new CreateGameMessage(1, nick));
                } catch (IOException e) {
                    logger.debug("something wrong happened while dealing with a message: " + e);
                }
            }).start();
        });
    }

    @Override
    public void notifyUpdate() {
        BuildGUI.getInstance().toBoard(stage, ui);
    }

    @Override
    public void notifyError() {
        logger.debug("error notification");
    }
}
