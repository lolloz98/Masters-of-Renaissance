package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.File;
import java.util.Objects;

public class DevelopGridControllerGUI extends ControllerGUI implements Observer {
    public Pane develop_pane;
    public GridPane develop_grid;
    public Button buydevelopBtn;
    public Button backBtn;

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
        ui.getLocalGame().getLocalDevelopmentGrid().addObserver(this);


        updateGrid();

    }

    private void updateGrid() {
        //add top cards to the grid
        LocalDevelopCard[][] topCards=ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards();
        String path;
        Image cardImage;
        Button button;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(int i=2;i>=0;i++){
            for(int j=0;j<4;j++){
                ImageView imgView = new ImageView();
                path = String.format("png/cards_front/%03d.png", topCards[2-i][j].getId());
                File cardImageFile=new File(path);
                cardImage=new Image(cardImageFile.toURI().toString());
                imgView.setImage(cardImage);
                imgView.setFitHeight(218);
                imgView.setFitWidth(167);
                button = new Button();
                button.setOnMouseClicked(mouseEvent -> {
                    //todo
                });
                button.setPrefWidth(167);
                button.setPrefHeight(218);
                button.setDisable(true);
                button.setGraphic(imgView);
                develop_grid.add(button,i,j);
            }
        }
    }

    public void buyDevelop(ActionEvent actionEvent) {

    }

    public void back(ActionEvent actionEvent) {
        BuildGUI.getInstance().toBoard(stage, ui);
    }
}
