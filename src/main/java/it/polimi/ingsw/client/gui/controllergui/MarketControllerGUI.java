package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.exceptions.InvalidMarketIndexException;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class MarketControllerGUI extends ControllerGUI implements Observer {
    public GridPane market_grid;
    public ImageView free_marble;
    public Button developmentBtn;
    public DepotComponent depotCmp;
    public Button boardBtn;
    public Button pushA;
    public Button pushB;
    public Button pushC;
    public Button push1;
    public Button push2;
    public Button push3;
    public Button push4;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;
        market_grid.setHgap(1);
        market_grid.setVgap(1);
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        developmentBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });
        boardBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toBoard(stage, ui);
        });
        pushA.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "A");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        pushB.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "B");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        pushC.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "C");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        push1.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "1");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        push2.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "2");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        push3.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "3");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });
        push4.setOnMouseClicked(mouseEvent -> {
            // todo send push
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), "4");
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (InvalidMarketIndexException | IOException e) {
                e.printStackTrace();
            }
        });

        setUpState();
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    private void setUpState() {
        resetDefault();
        Resource[][] marbleMatrix = ui.getLocalGame().getLocalMarket().getMarbleMatrix();
        Image marbleImage;
        String path;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        path = Objects.requireNonNull(classLoader.getResource("png/punchboard/marbles/" + ui.getLocalGame().getLocalMarket().getFreeMarble() + ".png")).getPath();
        File freeMarbleFile = new File(path);
        marbleImage = new Image(freeMarbleFile.toURI().toString());
        free_marble.setImage(marbleImage);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                ImageView imgView = new ImageView();
                path = Objects.requireNonNull(classLoader.getResource("png/punchboard/marbles/" + marbleMatrix[y][x] + ".png")).getPath();
                File file = new File(path);
                marbleImage = new Image(file.toURI().toString());
                imgView.setImage(marbleImage);
                imgView.setFitHeight(56);
                imgView.setFitWidth(56);
                market_grid.add(imgView, x, y);
            }
        }
        // print resComb if it's my turn
        boolean myTurn = true;
        if (ui.getLocalGame() instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) ui.getLocalGame();
            if (localMulti.getLocalTurn().getCurrentPlayer() != localMulti.getMainPlayer()) {
                myTurn = false;
            }
        }
        if (myTurn && ui.getLocalGame().getLocalMarket().getResCombinations().size() != 0) {
            VBox vboxRes = new VBox();
            Label resCombText = new Label("Click on the button corresponding to the combination of resources you want to get:");
            resCombText.setLayoutX(520);
            resCombText.setLayoutY(290);
            vboxRes.getChildren().add(resCombText);
            GridPane resCombGrid = new GridPane();
            ArrayList<TreeMap<Resource, Integer>> resComb = ui.getLocalGame().getLocalMarket().getResCombinations();
            for (int i = 0; i < resComb.size(); i++) {
                int x = 0; // x of the position in the resCombGrid
                Button flushButton = new Button();
                flushButton.setText("flush");
                flushButton.setOnMouseClicked(mouseEvent -> {
                    // todo go to flush screen passing resComb.get(i)
                });
                for(Resource res : resComb.get(i).keySet())
                    for(int j = 0; j<resComb.get(i).get(res); j++){
                        x++;
                        ImageView imgView = new ImageView();
                        path = Objects.requireNonNull(classLoader.getResource("png/punchboard/marbles/" + res + ".png")).getPath();
                        File file = new File(path);
                        marbleImage = new Image(file.toURI().toString());
                        imgView.setImage(marbleImage);
                        imgView.setFitHeight(56);
                        imgView.setFitWidth(56);
                        resCombGrid.add(imgView, i, x);
                    }
            }
        }
    }

    public void resetDefault() {
        // reset buttons
    }
}
