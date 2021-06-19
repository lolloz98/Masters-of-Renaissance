package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.Observable;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Base class for controllers of GUI.
 */
public abstract class ControllerGUI {
    public abstract void setUp(Stage stage, Parent root, GUI ui);
    public void setLocalVariables(Stage stage, Parent root, GUI ui){
        this.ui = ui;
        this.stage = stage;
        this.root = root;
    }
    protected GUI ui;
    protected Stage stage;
    protected Parent root;
}
