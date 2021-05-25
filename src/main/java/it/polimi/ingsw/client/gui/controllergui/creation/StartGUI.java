package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartGUI extends ControllerGUI {
    private static final Logger logger = LogManager.getLogger(StartGUI.class);

    public Button localBtn;
    public Button remoteBtn;

    public void setUp(Stage stage, Parent root, GUI ui){
        localBtn.setOnMouseClicked(mouseEvent -> {
            logger.debug(localBtn + " clicked");
            BuildGUI.getInstance().toStartLocal(stage, ui);
        });

        remoteBtn.setOnMouseClicked(mouseEvent -> {
            logger.debug(remoteBtn + " clicked");
            BuildGUI.getInstance().toStartRemote(stage, ui);
        });
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }
}
