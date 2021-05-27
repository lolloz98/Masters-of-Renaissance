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
    public Button buyL1GreenBtn;
    public Button buyL2GreenBtn;
    public Button buyL3GreenBtn;
    public Button buyL1BlueBtn;
    public Button buyL2BlueBtn;
    public Button buyL3BlueBtn;
    public Button buyL1YellowBtn;
    public Button buyL2YellowBtn;
    public Button buyL3YellowBtn;
    public Button buyL1PurpleBtn;
    public Button buyL2PurpleBtn;
    public Button buyL3PurpleBtn;
    private Button[][] matrixBtns;
    private boolean cardSelected;


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
        setLocalVariables(stage,root,ui);
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getLocalDevelopmentGrid().overrideObserver(this);

        cardSelected=false;

        matrixBtns = new Button[][]{
                {buyL1GreenBtn, buyL2GreenBtn, buyL3GreenBtn},
                {buyL1BlueBtn, buyL2BlueBtn, buyL3BlueBtn},
                {buyL1YellowBtn, buyL2YellowBtn, buyL3YellowBtn},
                {buyL1PurpleBtn, buyL2PurpleBtn, buyL3PurpleBtn}
        };

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
        LocalDevelopCard[][] topCards = ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView imgView = new ImageView();
                imgView.setImage(topCards[i][j].getImage());
                imgView.setFitHeight(218);
                imgView.setFitWidth(167);
                int finalJ = j;
                int finalI = i;

                matrixBtns[i][j].setOnMouseClicked(mouseEvent -> {
                    buyDevelop(finalI,finalJ);
                });

                matrixBtns[i][j].setDisable(true);
                matrixBtns[i][j].setGraphic(imgView);

            }
        }


    }

    //activates the buttons on the grid
    private void activateChooseCardButtons() {
        for (Node n : develop_grid.getChildren()) {
            if (n instanceof Button) {
                //logger.debug("the node is a button");
                Button b = (Button) n;
                //logger.debug("disabling the button");
                b.setDisable(false);
            }

        }
    }

    public void disableOtherCardButtons(int selectedI,int selectedJ){
        for(int i=0;i<4;i++){
            for(int j=0;j<3;j++){
                if(i!=selectedI||j!=selectedJ)
                    matrixBtns[i][j].setDisable(true);
            }
        }
    }

    //todo
    private void buyDevelop(int i,int j) {
        if(!cardSelected) {
            cardSelected=true;
            disableOtherCardButtons(i,j);
            Platform.runLater(() -> matrixBtns[i][j].setStyle("-fx-effect: dropshadow(three-pass-box, rgba(199,17,17,0.8), 20, 0, 0, 0)"));

            //todo go to choose resource scene
            BuildGUI.getInstance().toBuyDevelop(stage, ui, ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards()[i][j]);
            //ui.getGameHandler().dealWithMessage(new BuyDevelopCardMessage(ui.getLocalGame().getGameId(),ui.getLocalGame().getMainPlayer().getId(),card.getLevel(),card.getColor(),));
        }
    }

}
