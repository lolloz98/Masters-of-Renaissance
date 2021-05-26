package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.enums.Resource;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.TreeMap;

public class FlushResControllerGUI extends ControllerGUI implements Observer {


    public void setUp(Stage stage, Parent root, GUI ui, TreeMap<Resource, Integer> resComb) {
        setUp(stage, root, ui);

    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
    }
}
