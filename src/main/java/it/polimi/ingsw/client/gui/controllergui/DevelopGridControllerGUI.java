package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.gui.componentsgui.StrongBoxComponent;
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
    public Label chooseACardLbl;
    public DepotComponent depotCmp;
    public StrongBoxComponent strongBoxCmp;
    private final ImageView[][] matrixImg=new ImageView[4][3];


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
        ui.getLocalGame().getError().addObserver(this);


        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toBoard(stage, ui);
        });

        buydevelopBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(()-> chooseACardLbl.setText("Choose a card to buy!"));
            activateChooseCardButtons();

        });

        updateGrid();

    }

    private void updateGrid() {
        buydevelopBtn.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");

        depotCmp.setImages(ui.getLocalGame().getMainPlayer().getLocalBoard().getResInNormalDepot());
        strongBoxCmp.updateRes(ui.getLocalGame().getMainPlayer().getLocalBoard().getResInStrongBox());
        //add top cards to the grid
        LocalDevelopCard[][] topCards = ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView imgView = new ImageView();
                if (topCards[i][j] != null) {
                    int finalJ = j;
                    int finalI = i;
                    imgView.setImage(topCards[i][j].getImage());
                    imgView.setOnMouseClicked(mouseEvent -> {
                        buyDevelop(finalI,finalJ);
                    });
                } else {//update empty card image
                    String path;
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    path = Objects.requireNonNull(classLoader.getResource("png/empty_card.png")).getPath();
                    File file = new File(path);
                    Image emptyCardImage = new Image(file.toURI().toString());
                    imgView.setImage(emptyCardImage);
                }
                imgView.setFitHeight(215);
                imgView.setFitWidth(165);

                imgView.setDisable(true);

                matrixImg[i][j]=imgView;

                develop_grid.add(imgView,i,2-j);

            }
        }


    }

    //activates the buttons on the grid
    private void activateChooseCardButtons() {
        buydevelopBtn.setDisable(true);
        for (Node n : develop_grid.getChildren()) {
            if (n instanceof ImageView) {
                //logger.debug("the node is a button");
                ImageView card = (ImageView) n;
                //logger.debug("disabling the button");
                card.setDisable(false);
            }

        }
    }


    private void buyDevelop(int i,int j) {
        BuildGUI.getInstance().toBuyDevelop(stage, ui, ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards()[i][j]);
    }

}
