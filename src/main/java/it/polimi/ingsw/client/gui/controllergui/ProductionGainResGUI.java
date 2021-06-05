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
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class ProductionGainResGUI extends ControllerGUI implements Observer {
    public Label promptLbl;
    public Spinner strongShieldSpnr;
    public Spinner strongServantSpnr;
    public Spinner strongGoldSpnr;
    public Spinner strongRockSpnr;
    public GridPane gridPane;
    public Button confirmBtn;
    public Button backBtn;
    public Label messageLbl;
    public ImageView cardImg;
    private LocalCard card;
    int whichProd;
    private TreeMap<WarehouseType, TreeMap<Resource, Spinner<Integer>>> spinners;
    TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive;
    TreeMap<Resource, Integer> toGain;


    private static final Logger logger = LogManager.getLogger(BuyCardSceneControllerGUI.class);

    @Override
    public void notifyError() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                if(ui.getLocalGame().getState() ==  LocalGameState.DESTROYED) HelperGUI.handleGameDestruction(stage, ui);
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage() + "\nGo back and choose again the resources to give.");
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

    public void setUp(Stage stage, Parent root, GUI ui, LocalCard card, int whichProd, TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive) {
        setUp(stage, root, ui);
        this.card = card;
        this.whichProd = whichProd;
        this.resToGive = resToGive;
        toGain = new TreeMap<>();
        if(card instanceof LocalProductionLeader){
            toGain = ((LocalProductionLeader) card).getProduction().getResToGain();
        }else if(card instanceof LocalDevelopCard){
            toGain = ((LocalDevelopCard) card).getProduction().getResToGain();
        }

        promptLbl.setText("Choose the optional resources.\nTo be chosen:" + toGain.get(Resource.ANYTHING) + ".");


        cardImg.setImage(card.getImage());

        backBtn.setOnMouseClicked(mouseEvent -> {
            removeObservers();
            BuildGUI.getInstance().toActivateProduction(stage, ui, card, whichProd);
        });

        confirmBtn.setOnMouseClicked(e -> confirm());

        //creating useful map to handle the scene
        spinners = new TreeMap<>() {{
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
                if (t == WarehouseType.STRONGBOX) {
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
                    spinners.get(t).get(r).setValueFactory(valueFactory);
                } else {
                    spinners.get(t).get(r).setVisible(false);
                }
            }
        }
    }


    public void confirm() {
        //logger.debug("in confirm method");
        //create the to give map
        TreeMap<Resource, Integer> toSwap = new TreeMap<>();

        for (Resource r : spinners.get(WarehouseType.STRONGBOX).keySet()) {
            Spinner<Integer> spinner = spinners.get(WarehouseType.STRONGBOX).get(r);
            if (!spinner.isDisabled() && spinner.getValue() != 0)
                toSwap.put(r, spinner.getValue());
        }

        if(toSwap.size() == toGain.get(Resource.ANYTHING)){
            TreeMap<Resource, Integer> toSend = new TreeMap<>(toGain);
            toSend.remove(Resource.ANYTHING);
            for(Resource r: toSwap.keySet()){
                toSend.put(r, toSend.getOrDefault(r, 0) + toSwap.get(r));
            }
            try {
                ui.getGameHandler().dealWithMessage(new ApplyProductionMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), whichProd, resToGive, toSend));
            } catch (IOException e) {
                logger.error("something happened while dealing with message");
            }
        }
        else{
            messageLbl.setText("You have chosen the wrong amount of resources. You have to choose " + toGain.get(Resource.ANYTHING) + " resources in total.");
        }
    }
}

