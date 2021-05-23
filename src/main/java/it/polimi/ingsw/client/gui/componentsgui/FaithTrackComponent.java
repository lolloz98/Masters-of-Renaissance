package it.polimi.ingsw.client.gui.componentsgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaithTrackComponent extends AnchorPane {
    private static final Logger logger = LogManager.getLogger(FaithTrackComponent.class);

    @FXML
    private ImageView vatican1;
    @FXML
    private ImageView vatican2;
    @FXML
    private ImageView vatican3;
    @FXML
    private ImageView faith0;
    @FXML
    private ImageView faith1;
    @FXML
    private ImageView faith2;
    @FXML
    private ImageView faith3;
    @FXML
    private ImageView faith4;
    @FXML
    private ImageView faith5;
    @FXML
    private ImageView faith6;
    @FXML
    private ImageView faith7;
    @FXML
    private ImageView faith8;
    @FXML
    private ImageView faith9;
    @FXML
    private ImageView faith10;
    @FXML
    private ImageView faith11;
    @FXML
    private ImageView faith12;
    @FXML
    private ImageView faith13;
    @FXML
    private ImageView faith14;
    @FXML
    private ImageView faith15;
    @FXML
    private ImageView faith16;
    @FXML
    private ImageView faith17;
    @FXML
    private ImageView faith18;
    @FXML
    private ImageView faith19;
    @FXML
    private ImageView faith20;
    @FXML
    private ImageView faith21;
    @FXML
    private ImageView faith22;
    @FXML
    private ImageView faith23;
    @FXML
    private ImageView faith24;

    private final Image lorenzo;
    private final Image player;
    private final Image overlapped;
    private final List<ImageView> vaticans = new ArrayList<>();
    private final List<ImageView> faith = new ArrayList<>();
    private int currentFaith = 0;
    private int currentLorenzo = 0;

    public int getCurrentLorenzo() {
        return currentLorenzo;
    }

    public void setCurrentLorenzo(int currentLorenzo) {
        changeImagePre(true);
        this.currentLorenzo = currentLorenzo;
        changeImagePost(true);
    }

    public int getCurrentFaith() {
        return currentFaith;
    }

    private void changeImagePre(boolean lorenzoMoving){
        if(currentFaith == currentLorenzo){
            if (lorenzoMoving) {
                faith.get(currentFaith).setImage(player);
            } else {
                faith.get(currentFaith).setImage(lorenzo);
            }
        } else{
            faith.get(currentFaith).setImage(null);
        }
    }

    private void changeImagePost(boolean lorenzoMoved){
        if(currentFaith == currentLorenzo){
            faith.get(currentFaith).setImage(overlapped);
        } else{
            if (lorenzoMoved) {
                faith.get(currentLorenzo).setImage(lorenzo);
            } else {
                faith.get(currentFaith).setImage(player);
            }
        }
    }

    public void setCurrentFaith(int currentFaith) {
        changeImagePre(false);
        this.currentFaith = currentFaith;
        changeImagePost(false);
    }

    public List<ImageView> getVaticans() {
        return vaticans;
    }

    public FaithTrackComponent() {
        logger.debug("drawing faith track component");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/faithtrack.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        // todo change these images
        player = new Image("/png/punchboard/calamaio.png");
        lorenzo = new Image("/png/punchboard/croce.png");
        overlapped = new Image("/png/punchboard/cerchio1.png");

        vaticans.add(vatican1);
        vaticans.add(vatican2);
        vaticans.add(vatican3);

        faith.add(faith0);
        faith.add(faith1);
        faith.add(faith2);
        faith.add(faith3);
        faith.add(faith4);
        faith.add(faith5);
        faith.add(faith6);
        faith.add(faith7);
        faith.add(faith8);
        faith.add(faith9);
        faith.add(faith10);
        faith.add(faith11);
        faith.add(faith12);
        faith.add(faith13);
        faith.add(faith14);
        faith.add(faith15);
        faith.add(faith16);
        faith.add(faith17);
        faith.add(faith18);
        faith.add(faith19);
        faith.add(faith20);
        faith.add(faith21);
        faith.add(faith22);
        faith.add(faith23);
        faith.add(faith24);

        setCurrentFaith(0);
        setCurrentLorenzo(0);
//        for(ImageView i: faith){
//            i.setImage(new Image("/png/punchboard/calamaio.png"));
//        }
    }
}
