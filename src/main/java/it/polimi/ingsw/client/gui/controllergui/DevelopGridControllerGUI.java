package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class DevelopGridControllerGUI extends ControllerGUI implements Observer {
    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;

        ui.getLocalGame().overrideObserver(this);
    }

    public void buyDevelop(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) {
    }
}
