package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.LocalGame;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MarketControllerGUI extends ControllerGUI implements Observer {
    public GridPane market_grid;
    public ImageView free_marble;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;

        ui.getLocalGame().overrideObserver(this);


    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }
}
