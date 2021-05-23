package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.FaithTrackComponent;
import it.polimi.ingsw.client.gui.componentsgui.SlotDevelopComponent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class BoardControllerGUI implements ControllerGUI {
    public Button activateNormalBtn;
    public FaithTrackComponent faithTrackComponent;
    public SlotDevelopComponent slotDevelopComponent1;
    public SlotDevelopComponent slotDevelopComponent2;
    public SlotDevelopComponent slotDevelopComponent3;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {

    }
}
