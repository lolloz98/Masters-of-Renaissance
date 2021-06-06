package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.exceptions.InvalidMarketIndexException;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.gui.componentsgui.ImageCache;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    public GridPane resCombGrid;
    public Label messageLbl;
    public Button boardBtn;
    public Button pushA;
    public Button pushB;
    public Button pushC;
    public Button push1;
    public Button push2;
    public Button push3;
    public Button push4;
    private ArrayList<Button> btnList;

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        // todo set observers
        market_grid.setHgap(1);
        market_grid.setVgap(1);
        ui.getLocalGame().getLocalMarket().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        messageLbl.setText("");
        ArrayList<String> msgList = new ArrayList<>() {{
            add("A");
            add("B");
            add("C");
            add("1");
            add("2");
            add("3");
            add("4");
        }};
        btnList = new ArrayList<>() {{
            add(pushA);
            add(pushB);
            add(pushC);
            add(push1);
            add(push2);
            add(push3);
            add(push4);
        }};
        developmentBtn.setOnMouseClicked(mouseEvent -> {
            removeObserved();
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });
        boardBtn.setOnMouseClicked(mouseEvent -> {
            removeObserved();
            BuildGUI.getInstance().toBoard(stage, ui);
        });
        int i = 0;
        for (Button btn : btnList) {
            int finalI = i;
            btn.setOnMouseClicked(mouseEvent -> {
                try {
                    UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(ui.getLocalGame(), msgList.get(finalI));
                    Platform.runLater(() -> {
                        messageLbl.setText("Please wait");
                        setEnabled(false);
                    });
                    ui.getGameHandler().dealWithMessage(useMarketMessage);
                } catch (InvalidMarketIndexException | IOException e) {
                    e.printStackTrace();
                }
            });
            i++;
        }
        setUpState();
    }

    @Override
    public void notifyUpdate() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                setUpState();
            }
        });
    }

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                if(ui.getLocalGame().getState() ==  LocalGameState.DESTROYED) HelperGUI.handleGameDestruction(stage, ui);
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
        });
    }

    private void setUpState() {
        resetDefault();
        Resource[][] marbleMatrix = ui.getLocalGame().getLocalMarket().getMarbleMatrix();
        Image marbleImage;
        String path;
        depotCmp.setImages(ui.getLocalGame().getMainPlayer().getLocalBoard().getResInNormalDepot());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ImageCache.setMarbleInView(ui.getLocalGame().getLocalMarket().getFreeMarble(), free_marble);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                ImageView imgView = new ImageView();
                ImageCache.setMarbleInView(marbleMatrix[y][x], imgView);
                imgView.setFitHeight(56);
                imgView.setFitWidth(56);
                market_grid.add(imgView, x, y);
            }
        }
        // print resComb if it's my turn
        boolean myTurn = true;
        if (ui.getLocalGame() instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) ui.getLocalGame();
            if (localMulti.getLocalTurn().getCurrentPlayer().getId() != localMulti.getMainPlayer().getId()) {
                myTurn = false;
                setEnabled(false);
            }
        }
        if (myTurn && ui.getLocalGame().getLocalMarket().getResCombinations().size() != 0) {
            Label resCombText = new Label("Click on the button corresponding to the combination of resources you want to get:");
            resCombText.setLayoutX(520);
            resCombText.setLayoutY(290);
            ArrayList<TreeMap<Resource, Integer>> resComb = ui.getLocalGame().getLocalMarket().getResCombinations();
            for (int i = 0; i < resComb.size(); i++) {
                int x = 0; // x of the position in the resCombGrid
                Button flushButton = new Button();
                flushButton.setText("flush");
                int finalI = i;
                flushButton.setOnMouseClicked(mouseEvent -> {
                    removeObserved();
                    BuildGUI.getInstance().toFlushRes(stage, ui, resComb.get(finalI));
                });
                resCombGrid.setHgap(2);
                resCombGrid.setVgap(2);
                resCombGrid.add(flushButton, 0, i);
                for (Resource res : resComb.get(i).keySet())
                    for (int j = 0; j < resComb.get(i).get(res); j++) {
                        x++;
                        ImageView imgView = new ImageView();
                        marbleImage = new Image("/png/punchboard/marbles/" + res + ".png");
                        imgView.setImage(marbleImage);
                        imgView.setFitHeight(56);
                        imgView.setFitWidth(56);
                        resCombGrid.add(imgView, x, i);
                    }
            }
        }
    }

    private void resetDefault() {
        setEnabled(true);
        messageLbl.setText("");
    }

    private void setEnabled(boolean bool) {
        for(Button b : btnList)
            b.setDisable(!bool);
    }

    private void removeObserved() {
        ui.getLocalGame().getLocalMarket().removeObservers();
        ui.getLocalGame().getError().removeObserver();
    }
}
