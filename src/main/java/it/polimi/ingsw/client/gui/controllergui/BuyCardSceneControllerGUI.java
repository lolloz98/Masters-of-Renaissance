package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private List<Spinner<Integer>> goldSpinners;
    private List<Spinner<Integer>> servantSpinners;
    private List<Spinner<Integer>> shieldSpinners;
    private List<Spinner<Integer>> rockSpinners;
    private TreeMap<Resource,List<Spinner<Integer>>> spinnerMap;
    public Button backBtn;
    public Button confirmBtn;
    public Label messageLbl;
    public Label discountMessageLbl;
    LocalDevelopCard cardToBuy;


    public void setUp(Stage stage, Parent root, GUI ui, LocalDevelopCard toBuyCard){
        setUp(stage,root,ui);
        cardImg.setImage(toBuyCard.getImage());
        cardToBuy=toBuyCard;

        backBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });

        confirmBtn.setDisable(true);



        goldSpinners=new ArrayList<>(){{
            add(normalGoldSpnr);
            add(leaderGoldSpnr);
            add(strongGoldSpnr);
        }};

        servantSpinners= new ArrayList<>(){{
            add(normalServantSpnr);
            add(leaderServantSpnr);
            add(strongServantSpnr);
        }};

        shieldSpinners= new ArrayList<>(){{
            add(normalShieldSpnr);
            add(leaderShieldSpnr);
            add(strongShieldSpnr);
        }};

        rockSpinners= new ArrayList<>(){{
            add(normalRockSpnr);
            add(leaderRockSpnr);
            add(strongRockSpnr);
        }};

        spinnerMap=new TreeMap<>(){{
            put(Resource.GOLD,goldSpinners);
            put(Resource.SHIELD,shieldSpinners);
            put(Resource.SERVANT,servantSpinners);
            put(Resource.ROCK,rockSpinners);
        }};

        //configure the spinners with values of 0-100
        for(Resource r:spinnerMap.keySet()){
            for(Spinner<Integer> spinner:spinnerMap.get(r)) {
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
                spinner.setValueFactory(valueFactory);
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
        //disable the sliders of the resources that isn't in the cost
        for(Resource r: new Resource[]{Resource.GOLD, Resource.SERVANT, Resource.SHIELD, Resource.ROCK}){
            if(!cost.containsKey(r)){
                switch (r){
                    case GOLD:
                        goldSpinners.forEach((x)->{x.setDisable(true);});
                        break;
                    case ROCK:
                        rockSpinners.forEach(x->x.setDisable(true));
                        break;
                    case SERVANT:
                        servantSpinners.forEach(x->x.setDisable(true));
                        break;
                    case SHIELD:
                        shieldSpinners.forEach(x->x.setDisable(true));
                        break;
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

    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
    }

    public void confirm(ActionEvent actionEvent) {

    }
}
