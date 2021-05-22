package it.polimi.ingsw.client.gui.controllergui.creation;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.gui.controllergui.ControllerGUI;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreateOrJoinGUI implements ControllerGUI {
    public Button createBtn;
    public Button JoinBtn;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        createBtn.setOnMouseClicked(mouseEvent -> BuildGUI.getInstance().toCreateGame(stage, ui));
        JoinBtn.setOnMouseClicked(mouseEvent -> BuildGUI.getInstance().toJoinGame(stage, ui));
    }
}