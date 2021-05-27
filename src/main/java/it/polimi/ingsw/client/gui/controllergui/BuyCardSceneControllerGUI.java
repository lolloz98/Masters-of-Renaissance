package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BuyCardSceneControllerGUI extends ControllerGUI implements Observer {
    public ImageView cardImg;
    public ComboBox<String> slotToStoreComboBox;
    public Slider normalDepotGoldSldr;
    public Slider normalDepotServantSldr;
    public Slider normalDepotShieldSldr;
    public Slider normalDepotRockSldr;
    public Slider leaderDepotGoldSldr;
    public Slider leaderDepotServantSldr;
    public Slider leaderDepotShieldSldr;
    public Slider leaderDepotRockSldr;
    public Slider strongBoxGoldSldr;
    public Slider strongBoxServantSldr;
    public Slider strongBoxShieldSldr;
    public Slider strongBoxRockSldr;
    private List<Slider> goldSliders;
    private List<Slider> servantSliders;
    private List<Slider> shieldSliders;
    private List<Slider> rockSliders;


    public void setUp(Stage stage, Parent root, GUI ui, LocalDevelopCard toBuyCard){
        setUp(stage,root,ui);
        cardImg.setImage(toBuyCard.getImage());

        goldSliders=new ArrayList<>(){{
            add(normalDepotGoldSldr);
            add(leaderDepotGoldSldr);
            add(strongBoxGoldSldr);
        }};

        servantSliders= new ArrayList<>(){{
            add(normalDepotServantSldr);
            add(leaderDepotServantSldr);
            add(strongBoxServantSldr);
        }};

        shieldSliders= new ArrayList<>(){{
            add(normalDepotShieldSldr);
            add(leaderDepotShieldSldr);
            add(strongBoxShieldSldr);
        }};

        rockSliders= new ArrayList<>(){{
            add(normalDepotRockSldr);
            add(leaderDepotRockSldr);
            add(strongBoxRockSldr);
        }};

        slotToStoreComboBox.getItems().addAll("First", "Second", "Third");
    }


    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().overrideObserver(this);
    }
}
