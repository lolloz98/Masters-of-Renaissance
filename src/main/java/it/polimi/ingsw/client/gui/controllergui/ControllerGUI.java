package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class ControllerGUI {
    public abstract void setUp(Stage stage, Parent root, GUI ui);
    protected GUI ui;
    protected Stage stage;
    protected Parent root;
}
