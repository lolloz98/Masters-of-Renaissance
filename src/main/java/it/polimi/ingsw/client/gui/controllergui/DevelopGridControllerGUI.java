package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.gui.componentsgui.StrongBoxComponent;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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

/**
 * controller that controls the develop grid scene.
 */
public class DevelopGridControllerGUI extends ControllerGUI implements Observer {
    public Pane developPane;
    public GridPane developGrid;
    public Button buyDevelopBtn;
    public Button backBtn;
    public Label chooseACardLbl;
    public DepotComponent depotCmp;
    public StrongBoxComponent strongBoxCmp;


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
        if (ui.getLocalGame().getState() == LocalGameState.DESTROYED) HelperGUI.handleGameDestruction(stage, ui);
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getMainPlayer().getLocalBoard().overrideObserver(this);
        ui.getLocalGame().getLocalDevelopmentGrid().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);

/**
 * go back to board
 */
        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toBoard(stage, ui);
        });

        buyDevelopBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> chooseACardLbl.setText("Choose a card to buy!"));
            activateChooseCardButtons();

        });

        updateGrid();

    }

    /**
     * updates the grid with the cards.
     */
    private void updateGrid() {
        buyDevelopBtn.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");

        LocalPlayer seen = ui.getLocalGame().getPlayerById(ui.getWhoIAmSeeingId());

        depotCmp.setImages(seen.getLocalBoard().getResInNormalDepot());
        strongBoxCmp.updateRes(seen.getLocalBoard().getResInStrongBox());

        //add top cards to the grid
        LocalDevelopCard[][] topCards = ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards();
        int[][] numberOfCards = ui.getLocalGame().getLocalDevelopmentGrid().getDevelopCardsNumber();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                //generating images
                ImageView imgView = new ImageView();
                if (topCards[i][j] != null) {
                    int finalJ = j;
                    int finalI = i;
                    imgView.setImage(topCards[i][j].getImage());
                    imgView.setOnMouseClicked(mouseEvent -> {
                        buyDevelop(finalI, finalJ);
                    });
                } else {//update empty card image
                    String path;
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    path = Objects.requireNonNull(classLoader.getResource("png/empty_card.png")).getPath();
                    File file = new File(path);
                    Image emptyCardImage = new Image(file.toURI().toString());
                    imgView.setImage(emptyCardImage);
                }

                imgView.setFitHeight(190);
                imgView.setFitWidth(168);
                imgView.setDisable(true);

                developGrid.add(imgView, i, 2 - j);

                GridPane.setValignment(imgView, VPos.TOP);

                //adding info about how many cards has the deck
                Pane deckInfoPane = new Pane();
                Label infoLbl = new Label(numberOfCards[i][j] + " cards on this deck");
                deckInfoPane.getChildren().add(infoLbl);
                deckInfoPane.setMaxHeight(28);
                deckInfoPane.setPrefHeight(28);
                developGrid.add(deckInfoPane, i, 2 - j);
                GridPane.setValignment(deckInfoPane, VPos.BOTTOM);
                GridPane.setHalignment(deckInfoPane, HPos.CENTER);
                GridPane.setFillWidth(deckInfoPane, false);


            }
        }


    }

    /**
     * enabling all the card buttons
     */
    private void activateChooseCardButtons() {
        buyDevelopBtn.setDisable(true);
        for (Node n : developGrid.getChildren()) {
            if (n instanceof ImageView) {
                //logger.debug("the node is a button");
                ImageView card = (ImageView) n;
                //logger.debug("disabling the button");
                card.setDisable(false);
            }

        }
    }

    /**
     * helper method that passes to the next scene
     *
     * @param i,j indexes of the develop card matrix
     */
    private void buyDevelop(int i, int j) {
        ui.getLocalGame().removeAllObservers();
        BuildGUI.getInstance().toBuyDevelop(stage, ui, ui.getLocalGame().getLocalDevelopmentGrid().getTopDevelopCards()[i][j]);
    }

}
