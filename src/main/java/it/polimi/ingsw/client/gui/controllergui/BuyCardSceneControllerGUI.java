package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.TreeMap;

/**
 * controller of the buy card scene
 */
public class BuyCardSceneControllerGUI extends ControllerGUI implements Observer {
    public ImageView cardImg;
    /**
     * activated if the card has a discount
     */
    public Label discountLbl;
    /**
     * combobox to choose in which slot we want to store the card
     */
    public ComboBox<String> slotToStoreComboBox;
    /**
     * spinner to choose the resource quantity
     */
    public Spinner<Integer> leaderShieldSpnr;
    public Spinner<Integer> normalGoldSpnr;
    public Spinner<Integer> leaderGoldSpnr;
    public Spinner<Integer> strongShieldSpnr;
    public Spinner<Integer> normalShieldSpnr;
    public Spinner<Integer> strongServantSpnr;
    public Spinner<Integer> strongGoldSpnr;
    public Spinner<Integer> leaderServantSpnr;
    public Spinner<Integer> normalServantSpnr;
    public Spinner<Integer> strongRockSpnr;
    public Spinner<Integer> leaderRockSpnr;
    public Spinner<Integer> normalRockSpnr;
    private TreeMap<WarehouseType, TreeMap<Resource, Spinner<Integer>>> spinners;
    public Button backBtn;
    public Button confirmBtn;
    public Label messageLbl;
    public Label discountMessageLbl;
    LocalDevelopCard cardToBuy;

    private static final Logger logger = LogManager.getLogger(BuyCardSceneControllerGUI.class);

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                if (ui.getLocalGame().getState() == LocalGameState.DESTROYED)
                    HelperGUI.handleGameDestruction(stage, ui);
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
        });
    }

    @Override
    public void notifyUpdate() {
        removeObservers();
        BuildGUI.getInstance().toBoard(stage, ui);
    }

    public void removeObservers() {
        ui.getLocalGame().removeObservers();
        ui.getLocalGame().getMainPlayer().getLocalBoard().removeObservers();
        ui.getLocalGame().getError().removeObserver();
        ui.getLocalGame().getLocalTurn().getHistoryObservable().removeObservers();
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getMainPlayer().getLocalBoard().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        ui.getLocalGame().getLocalTurn().getHistoryObservable().overrideObserver(this);
    }

    public void setUp(Stage stage, Parent root, GUI ui, LocalDevelopCard toBuyCard) {
        setUp(stage, root, ui);
        cardImg.setImage(toBuyCard.getImage());
        cardToBuy = toBuyCard;

        backBtn.setOnMouseClicked(mouseEvent -> {
            removeObservers();
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });

        confirmBtn.setOnMouseClicked(e -> confirm());
        confirmBtn.setDisable(true);

        slotToStoreComboBox.setOnAction(e -> {
            confirmBtn.setDisable(false);
            confirmBtn.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        });

        //creating useful map to handle the scene
        spinners = new TreeMap<>() {{
            put(WarehouseType.NORMAL, new TreeMap<>() {{
                put(Resource.GOLD, normalGoldSpnr);
                put(Resource.ROCK, normalRockSpnr);
                put(Resource.SERVANT, normalServantSpnr);
                put(Resource.SHIELD, normalShieldSpnr);
            }});
            put(WarehouseType.LEADER, new TreeMap<>() {{
                put(Resource.GOLD, leaderGoldSpnr);
                put(Resource.ROCK, leaderRockSpnr);
                put(Resource.SERVANT, leaderServantSpnr);
                put(Resource.SHIELD, leaderShieldSpnr);
            }});
            put(WarehouseType.STRONGBOX, new TreeMap<>() {{
                put(Resource.GOLD, strongGoldSpnr);
                put(Resource.ROCK, strongRockSpnr);
                put(Resource.SERVANT, strongServantSpnr);
                put(Resource.SHIELD, strongShieldSpnr);
            }});
        }};

        //configure the spinners with values of 0-100
        for (WarehouseType t : spinners.keySet()) {
            for (Resource r : spinners.get(t).keySet()) {
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
                spinners.get(t).get(r).setValueFactory(valueFactory);
            }
        }

        slotToStoreComboBox.getItems().addAll("First", "Second", "Third");

        setUpState();

    }

    public void setUpState() {
        disableSliders();

        if (cardToBuy.isDiscounted()) {
            discountLbl.setText("This card is discounted! \nthe actual cost is: " + cardToBuy.getCost());
        }
    }

    /**
     * helper method to disable all the spinners related to a resource type that is not in the cost.
     */
    private void disableSliders() {
        TreeMap<Resource, Integer> cost = cardToBuy.getCost();
        //disable the sliders of the resources that isn't in the cost map
        for (Resource r : new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}) {
            if (!cost.containsKey(r)) {
                for (WarehouseType t : spinners.keySet()) {
                    spinners.get(t).get(r).setDisable(true);
                }
            }
        }
    }


    public void confirm() {
        //logger.debug("in confirm method");
        //create the to give map
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toGiveFromWarehouseType = new TreeMap<>();
        TreeMap<Resource, Integer> toGive;

        for (WarehouseType type : spinners.keySet()) {
            toGive = new TreeMap<>();
            for (Resource r : spinners.get(type).keySet()) {
                Spinner<Integer> spinner = spinners.get(type).get(r);
                if (!spinner.isDisabled() && spinner.getValue() != 0)
                    toGive.put(r, spinner.getValue());
            }

            toGiveFromWarehouseType.put(type, toGive);
        }
        //logger.debug("to give built");

        int slotToStore = InputHelper.getSlotToStore(slotToStoreComboBox.getValue());
        //logger.debug("slot to store code passed");

        try {
            ui.getGameHandler().dealWithMessage(new BuyDevelopCardMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), cardToBuy.getLevel(), cardToBuy.getColor(), slotToStore, toGiveFromWarehouseType));
        } catch (IOException e) {
            logger.error("Error while handling request: " + e);
        }

    }
}
