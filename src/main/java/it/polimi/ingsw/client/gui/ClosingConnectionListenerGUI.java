package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.HelperGUI;
import it.polimi.ingsw.client.gui.controllergui.creation.StartRemoteGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This should observe the serverListener. When it closes for some reasons, it will call notifyUpdate.
 * After the closing of the connection, we could go either to the start page or to the rejoin page (it depends
 * on the current status of the game)
 */
public class ClosingConnectionListenerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(ClosingConnectionListenerGUI.class);

    private final GUI ui;
    private final Stage stage;

    public ClosingConnectionListenerGUI(Stage stage, GUI ui) {
        this.ui = ui;
        this.stage = stage;
    }

    @Override
    public void notifyUpdate() {
        ui.getLocalGame().removeAllObservers();
        if (ui.getLocalGame() == null || ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS || ui.getLocalGame().getState() == LocalGameState.OVER || ui.getLocalGame().getState() == LocalGameState.DESTROYED) {
            logger.warn("Game is null or in WAITING_PLAYERS or in OVER or DESTROYED state, returning at start screen");
            HelperGUI.handleGameDestruction(stage, ui);
        } else {
            BuildGUI.getInstance().toRejoin(stage, ui);
        }
    }

    @Override
    public void notifyError() {
    }
}
