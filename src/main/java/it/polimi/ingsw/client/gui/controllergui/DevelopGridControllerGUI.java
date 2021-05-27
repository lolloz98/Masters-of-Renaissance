package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.util.Objects;

public class DevelopGridControllerGUI extends ControllerGUI implements Observer {
    public Pane develop_pane;
    public GridPane develop_grid;
    public Button buydevelopBtn;
    public Button backBtn;
    public Label messageLbl;

    private static final Logger logger = LogManager.getLogger(DevelopGridControllerGUI.class);

    @Override
    public void notifyUpdate() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                updateGrid();
            }
        });
    }

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
        });
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getLocalDevelopmentGrid().overrideObserver(this);

        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toBoard(stage, ui);
        });

        buydevelopBtn.setOnMouseClicked(mouseEvent -> {
            activateChooseCardButtons();
        });

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
                path = Objects.requireNonNull(classLoader.getResource(String.format("png/cards_front/%03d.png", topCards[i][2-j].getId()))).getPath();
                File cardImageFile=new File(path);
                cardImage=new Image(cardImageFile.toURI().toString());
                imgView.setImage(cardImage);
                imgView.setFitHeight(218);
                imgView.setFitWidth(167);
                button = new Button();
                int finalJ = j;
                int finalI = i;
                button.setOnMouseClicked((mouseEvent) -> {
                    buyDevelop(topCards[finalI][2-finalJ]);
                });
//                button.setPrefWidth(167);
//                button.setPrefHeight(218);
                button.setVisible(true);
                button.setDisable(true);
                button.setGraphic(imgView);
//                BackgroundImage backgroundImage = new BackgroundImage( cardImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//                Background background = new Background(backgroundImage);
//                button.setBackground(background);
                develop_grid.add(button,i,j);
            }
        }
    }

    //activates the buttons on the grid
    private void activateChooseCardButtons(){
        for(Node n:develop_grid.getChildren()){
            if(n instanceof Button){
                //logger.debug("the node is a button");
                Button b=(Button) n;
                //logger.debug("disabling the button");
                b.setDisable(false);
            }

        }
    }

    //todo
    private void buyDevelop(LocalDevelopCard card){
        //todo go to choose resource scene
        //ui.getGameHandler().dealWithMessage(new BuyDevelopCardMessage(ui.getLocalGame().getGameId(),ui.getLocalGame().getMainPlayer().getId(),card.getLevel(),card.getColor(),));
    }

}
