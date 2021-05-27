package it.polimi.ingsw.client.gui.controllergui;

import com.sun.source.tree.Tree;
import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BuyCardSceneControllerGUI extends ControllerGUI implements Observer {
    public ImageView cardImg;
    public ComboBox<String> slotToStoreComboBox;
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
    private TreeMap<WarehouseType,TreeMap<Resource,Spinner<Integer>>> spinners;
    public Button backBtn;
    public Button confirmBtn;
    public Label messageLbl;
    public Label discountMessageLbl;
    LocalDevelopCard cardToBuy;

    private static final Logger logger = LogManager.getLogger(BuyCardSceneControllerGUI.class);


    public void setUp(Stage stage, Parent root, GUI ui, LocalDevelopCard toBuyCard){
        setUp(stage,root,ui);
        cardImg.setImage(toBuyCard.getImage());
        cardToBuy=toBuyCard;

        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });

        confirmBtn.setOnMouseClicked(e->confirm());
        confirmBtn.setDisable(true);

        slotToStoreComboBox.setOnAction(e-> confirmBtn.setDisable(false));

        //creating useful map to handle the scene
        spinners=new TreeMap<>(){{
            put(WarehouseType.NORMAL, new TreeMap<>(){{
                put(Resource.GOLD,normalGoldSpnr);
                put(Resource.ROCK,normalRockSpnr);
                put(Resource.SERVANT,normalServantSpnr);
                put(Resource.SHIELD,normalShieldSpnr);
            }});
            put(WarehouseType.LEADER,new TreeMap<>(){{
                put(Resource.GOLD,leaderGoldSpnr);
                put(Resource.ROCK,leaderRockSpnr);
                put(Resource.SERVANT,leaderServantSpnr);
                put(Resource.SHIELD,leaderShieldSpnr);
            }});
            put(WarehouseType.STRONGBOX,new TreeMap<>(){{
                put(Resource.GOLD,strongGoldSpnr);
                put(Resource.ROCK,strongRockSpnr);
                put(Resource.SERVANT,strongServantSpnr);
                put(Resource.SHIELD,strongShieldSpnr);
            }});
        }};

        //configure the spinners with values of 0-100
        for(WarehouseType t: spinners.keySet()){
            for(Resource r: spinners.get(t).keySet()) {
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
                spinners.get(t).get(r).setValueFactory(valueFactory);
            }
        }

        slotToStoreComboBox.getItems().addAll("First", "Second", "Third");

        setUpState();

    }

    public void setUpState(){
        disableSliders();
    }


    private void disableSliders(){
        TreeMap<Resource,Integer> cost=cardToBuy.getCost();
        //disable the sliders of the resources that isn't in the cost map
        for(Resource r: new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}){
            if(!cost.containsKey(r)){
                for(WarehouseType t:spinners.keySet()) {
                    spinners.get(t).get(r).setDisable(true);
                }
            }
        }
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
                messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
            }
        });
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
    }

    public void confirm() {
        logger.debug("in confirm method");
        //create the to give map
        TreeMap<WarehouseType,TreeMap<Resource,Integer>> toGiveFromWarehouseType=new TreeMap<>();
        TreeMap<Resource,Integer> toGive;

        for(WarehouseType type:spinners.keySet()){
            toGive=new TreeMap<>();
            for(Resource r: spinners.get(type).keySet()){
                Spinner<Integer> spinner=spinners.get(type).get(r);
                if(!spinner.isDisabled() && spinner.getValue()!=0)
                    toGive.put(r,spinner.getValue());
            }

            toGiveFromWarehouseType.put(type,toGive);
        }
        logger.debug("to give built");

        int slotToStore= InputHelper.getSlotToStore(slotToStoreComboBox.getValue());
        logger.debug("slot to store code passed");

        try {
            ui.getGameHandler().dealWithMessage(new BuyDevelopCardMessage(ui.getLocalGame().getGameId(),ui.getLocalGame().getMainPlayer().getId(), cardToBuy.getLevel(), cardToBuy.getColor(),slotToStore,toGiveFromWarehouseType));
        } catch (IOException e) {
            logger.error("Error while handling request: " + e);
        }
    }
}
