package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.Observable;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class ControllerGUI implements Observer {
    public abstract void setUp(Stage stage, Parent root, GUI ui);
    public void setLocalVariables(Stage stage, Parent root, GUI ui){
        this.ui = ui;
        this.stage = stage;
        this.root = root;
        if(ui.getLocalGame() != null) {
            ui.getLocalGame().overrideObserver(this);
            ui.getLocalGame().getError().addObserver(this);
        }
    }
    protected GUI ui;
    protected Stage stage;
    protected Parent root;
}
