package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WinnersGUI extends ControllerGUI {
    public AnchorPane singlePlayer;
    public Label singlePlayerLbl;
    public AnchorPane multiPlayer;
    public VBox vboxMulti;
    public Button backBtn;

    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        if (ui.getLocalGame() instanceof LocalMulti) {
            singlePlayer.setVisible(false);
            int i = 1;
            for(LocalPlayer player: ((LocalMulti) ui.getLocalGame()).getWinners()) {
                vboxMulti.getChildren().add(new Label(i + ": id: " + player.getId() + " ; name: " + player.getName()));
                i++;
            }

        } else {
            multiPlayer.setVisible(false);
            if (((LocalSingle) ui.getLocalGame()).isMainPlayerWinner()) {
                singlePlayerLbl.setText("You Won");
                singlePlayerLbl.setTextFill(Color.GREEN);
            }else{
                singlePlayerLbl.setText("Lorenzo Won");
                singlePlayerLbl.setTextFill(Color.RED);
            }
        }

        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toBoard(stage, ui);
        });
    }
}
