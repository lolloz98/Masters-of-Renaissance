package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.History;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.ApplyProductionMessage;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class ProductionGiveResGUI extends ControllerGUI implements Observer {
    public Label promptLbl;
    public Spinner normalGoldSpnr;
    public Spinner leaderGoldSpnr;
    public Spinner strongShieldSpnr;
    public Spinner leaderShieldSpnr;
    public Spinner strongServantSpnr;
    public Spinner normalShieldSpnr;
    public Spinner strongGoldSpnr;
    public Spinner leaderServantSpnr;
    public Spinner normalServantSpnr;
    public Spinner strongRockSpnr;
    public Spinner leaderRockSpnr;
    public Spinner normalRockSpnr;
    public Label normalLabel;
    public Label leaderLabel;
    public GridPane gridPane;
    private TreeMap<WarehouseType,TreeMap<Resource,Spinner<Integer>>> spinners;
    public Button confirmBtn;
    public Button backBtn;
    public Label messageLbl;
    public ImageView cardImg;
    private LocalCard card;
    int whichProd;


    private static final Logger logger = LogManager.getLogger(BuyCardSceneControllerGUI.class);

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                if(ui.getLocalGame().getState() ==  LocalGameState.DESTROYED) HelperGUI.handleGameDestruction(stage, ui);
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
        });
    }

    @Override
    public void notifyUpdate() {
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

    public void setUp(Stage stage, Parent root, GUI ui, LocalCard card, int whichProd) {
        setUp(stage, root, ui);
        this.card = card;
        this.whichProd = whichProd;
        cardImg.setImage(card.getImage());

        backBtn.setOnMouseClicked(mouseEvent -> {
            removeObservers();
            BuildGUI.getInstance().toBoard(stage, ui);
        });

        confirmBtn.setOnMouseClicked(e -> confirm());

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

        TreeMap<Resource, Integer> toGain  = new TreeMap<>();
        if(card instanceof LocalProductionLeader){
            toGain = ((LocalProductionLeader) card).getProduction().getResToGain();
        }else if(card instanceof LocalDevelopCard){
            toGain = ((LocalDevelopCard) card).getProduction().getResToGain();
        }
        // check if there are any optional resources to be chosen
        if(toGain.containsKey(Resource.ANYTHING)) {
            removeObservers();
            BuildGUI.getInstance().toActivateProduction(stage, ui, card, whichProd, toGiveFromWarehouseType);
        }
        else{
            try {
                ui.getGameHandler().dealWithMessage(new ApplyProductionMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), whichProd, toGiveFromWarehouseType, toGain));
            } catch (IOException e) {
                logger.error("something happened while dealing with message");
            }
        }
    }
}

