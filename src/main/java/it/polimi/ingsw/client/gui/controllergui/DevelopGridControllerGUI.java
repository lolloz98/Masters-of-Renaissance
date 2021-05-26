package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.File;

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

        backBtn.setOnMouseClicked((this::back));

        buydevelopBtn.setOnMouseClicked(this::buyDevelop);

        updateGrid();

    }

    private void updateGrid() {
        //add top cards to the grid
        LocalDevelopCard[][] topCards=ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards();
        String path;
        Image cardImage;
        Button button;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(int i=0;i<4;i++){
            for(int j=2;j>=0;j--){
                ImageView imgView = new ImageView();
                path = String.format("png/cards_front/%03d.png", topCards[i][2-j].getId());
                File cardImageFile=new File(path);
                cardImage=new Image(cardImageFile.toURI().toString());
                imgView.setImage(cardImage);
                imgView.setFitHeight(218);
                imgView.setFitWidth(167);
                button = new Button();
                int finalJ = j;
                int finalI = i;
                button.setOnMouseClicked((mouseEvent) -> {
                    buyDevelop(topCards[finalI][3-finalJ]);
                });
                button.setPrefWidth(167);
                button.setPrefHeight(218);
                button.setDisable(true);
                button.setGraphic(imgView);
                develop_grid.add(button,i,j);
            }
        }
    }

    //activates the buttons on the grid
    private void activateChooseCardButtons(){
        for(Node n:develop_grid.getChildren()){
            Button b=(Button) n;
            b.setDisable(false);
        }
    }

    //todo
    private void buyDevelop(LocalDevelopCard card){
        //todo go to choose resource scene
        //ui.getGameHandler().dealWithMessage(new BuyDevelopCardMessage(ui.getLocalGame().getGameId(),ui.getLocalGame().getMainPlayer().getId(),card.getLevel(),card.getColor(),));
    }


    public void back(MouseEvent actionEvent) {
        BuildGUI.getInstance().toBoard(stage, ui);
    }

    public void buyDevelop(MouseEvent mouseEvent) {
        activateChooseCardButtons();
    }
}
