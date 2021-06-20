package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.gui.componentsgui.ImageCache;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class FlushResControllerGUI extends ControllerGUI implements Observer {
    /**
     * main grid of the page, showing all the resources and the menus to pick where to put them
     */
    public GridPane choseResGrid;

    /**
     * menu containing the options for the resource placement
     */
    private ArrayList<ComboBox<String>> comboBoxList;

    /**
     * Copy of the resComb tree, used to keep track of what is already been printed on screen
     */
    private TreeMap<Resource, Integer> resToFlush;

    /**
     * TreeMap containing the resources gained from the market.
     */
    private TreeMap<Resource, Integer> resComb;

    /**
     * keeps the resources in order in which they are showed on screen
     */
    private ArrayList<Resource> resList;

    /**
     * keeps the number of FAITH resources
     */
    private int faithNumber;

    /**
     * TreeMap containing the resources the player decided to keep, with the corresponding depots to be put in
     */
    private TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToKeep;

    public Label messageLbl;
    private Button backBtn;
    private Button confirmBtn;
    public DepotComponent depotCmp;

    public void setUp(Stage stage, Parent root, GUI ui, TreeMap<Resource, Integer> resComb) {
        setUp(stage, root, ui);
        this.resComb = resComb;
        ui.getLocalGame().getLocalMarket().overrideObserver(this);
        ui.getLocalGame().getMainPlayer().getLocalBoard().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        messageLbl.setText("");
        initState();
    }

    @Override
    public void notifyUpdate() {
        removeObserved();
        Platform.runLater(() -> BuildGUI.getInstance().toMarket(stage, ui));
    }

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                if(ui.getLocalGame().getState() ==  LocalGameState.DESTROYED) HelperGUI.handleGameDestruction(stage, ui);
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
            initState();
        });
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
    }

    /**
     * default setup method, gets called when this view gets accessed
     */
    public void initState() {
        depotCmp.setImages(ui.getLocalGame().getMainPlayer().getLocalBoard().getResInNormalDepot());
        choseResGrid.getChildren().clear();
        comboBoxList = new ArrayList<>();
        resToFlush = new TreeMap<>(resComb);
        resList = new ArrayList<>();
        resToKeep = new TreeMap<>();
        choseResGrid.setHgap(2);
        choseResGrid.setVgap(10);
        if (resToFlush.containsKey(Resource.FAITH)) {
            faithNumber = resToFlush.remove(Resource.FAITH);
            for (int i = 0; i < faithNumber; i++) {
                MapUtils.addToResMapWarehouse(resToKeep, Resource.FAITH, WarehouseType.NORMAL);
            }
        }
        int i = 0; // row in choseResGrid
        Resource res;
        while (!MapUtils.isMapEmpty(resToFlush)) {
            choseResGrid.add(new Label("Chose where to put "), 0, i);
            res = resToFlush.firstKey();
            resList.add(res);
            MapUtils.removeResFromMap(resToFlush, resToFlush.firstKey());
            ImageView resView = new ImageView();
            resView.setFitHeight(56);
            resView.setFitWidth(56);
            ImageCache.setMarbleInView(res, resView);
            choseResGrid.add(resView, 1, i);
            choseResGrid.add(new Label(" in: "), 2, i);
            comboBoxList.add(new ComboBox<>());
            comboBoxList.get(i).getItems().addAll(
                    "Store in normal depot",
                    "Store in a leader card",
                    "Don't keep it"
            );
            comboBoxList.get(i).setValue("Store in normal depot");
            choseResGrid.add(comboBoxList.get(i), 4, i);
            i++;
        }
        choseResGrid.add(new Label("Faith points: " + faithNumber), 0, i);
        i++;
        confirmBtn = new Button("Confirm");
        confirmBtn.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(()->setEnabled(false));
            int j = 0;
            for (ComboBox<String> c : comboBoxList) {
                switch (c.getValue()) {
                    case "Store in normal depot":
                        MapUtils.addToResMapWarehouse(resToKeep, resList.get(j), WarehouseType.NORMAL);
                        break;
                    case "Store in a leader card":
                        MapUtils.addToResMapWarehouse(resToKeep, resList.get(j), WarehouseType.LEADER);
                        break;
                    case "Don't keep it":
                        // nothing to do
                }
                j++;
            }
            FlushMarketResMessage flushMarketResMessage = new FlushMarketResMessage(
                    ui.getLocalGame().getGameId(),
                    ui.getLocalGame().getMainPlayer().getId(),
                    resComb,
                    resToKeep
            );
            try {
                ui.getGameHandler().dealWithMessage(flushMarketResMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        backBtn = new Button("Go back to the market");
        backBtn.setOnMouseClicked(mouseEvent -> {
            removeObserved();
            BuildGUI.getInstance().toMarket(stage, ui);
        });
        choseResGrid.add(confirmBtn, 1, i);
        choseResGrid.add(backBtn, 0, i);
    }

    /**
     * removes all observers from the game
     */
    private void removeObserved() {
        ui.getLocalGame().getLocalMarket().removeObservers();
        ui.getLocalGame().getError().removeObserver();
        ui.getLocalGame().getMainPlayer().getLocalBoard().removeObservers();
    }

    /**
     * sets state of the buttons
     * @param bool state of the buttons
     */
    private void setEnabled(boolean bool) {
        confirmBtn.setDisable(!bool);
        backBtn.setDisable(!bool);
    }
}
