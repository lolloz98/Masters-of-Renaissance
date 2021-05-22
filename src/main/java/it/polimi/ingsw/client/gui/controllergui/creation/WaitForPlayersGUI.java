package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WaitForPlayersGUI implements ControllerGUI, Observer {
    private static final Logger logger = LogManager.getLogger(WaitForPlayersGUI.class);
    private Stage stage;
    private GUI ui;
    private final List<Integer> displayed = new ArrayList<>();
    private Parent root;

    @Override
    public void notifyUpdate() {
        logger.debug("in notify update");
        if(ui.getLocalGame().getState() == LocalGameState.WAITINGPLAYERS)
            Platform.runLater(this::addLabels);
        else
            BuildGUI.getInstance().toBoard(stage, ui);
    }

    @Override
    public void notifyError() {
        logger.debug("in notify error");
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        ui.getLocalGame().overrideObserver(this);
        this.stage = stage;
        this.ui = ui;
        this.root = root;
        Label mainLabel = new Label("New Game ID: " + ui.getLocalGame().getGameId());
        ((VBox) root).getChildren().add(mainLabel);
        addLabels();
    }

    private Label getLabel(LocalPlayer p){
        return new Label("Player " + p.getName() + ", with ID: " + p.getId() + ",  JOINED");
    }

    private void addLabels(){
        for(LocalPlayer p: ui.getLocalGame().getLocalPlayers()){
            if(!displayed.contains(p.getId())){
                displayed.add(p.getId());
                ((VBox) root).getChildren().add(getLabel(p));
            }
        }
    }
}
