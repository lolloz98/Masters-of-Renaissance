package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.ImageCache;
import it.polimi.ingsw.client.localmodel.Observable;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.server.model.utility.PairId;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ChooseInitResGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(ChooseInitResGUI.class);

    public Label numberOfResLbl;
    public Button chooseBtn;
    public ComboBox<String> comboBox;
    private final String ROCK = "ROCK";
    private final String SHIELD = "SHIELD";
    private final String SERVANT = "SERVANT";
    private final String GOLD = "GOLD";

    @Override
    public void notifyUpdate() {
        logger.debug("In notify update of choose init res");
        Platform.runLater(this::setView);
    }

    @Override
    public void notifyError() {

    }

    private void setView(){
        synchronized (ui.getLocalGame()) {
            int initRes = ui.getLocalGame().getMainPlayer().getLocalBoard().getInitialRes();
            logger.debug("initial resources: " + initRes);
            if(initRes == 0){
                ui.getLocalGame().getMainPlayer().getLocalBoard().removeObservers();

                BuildGUI.getInstance().toBoard(stage, ui);
            }
            numberOfResLbl.setText("Number of resources yet to be chosen: " + initRes);
        }
    }
    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        ui.getLocalGame().getMainPlayer().getLocalBoard().overrideObserver(this);

        setView();
        comboBox.getItems().addAll(
                ROCK,
                SERVANT,
                GOLD,
                SHIELD);
        comboBox.setValue(ROCK);
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        logger.debug("item: " + item);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(new ImageView(getImageFromString(item)));
                        }
                    }
                };
            }
        });

        chooseBtn.setOnMouseClicked(mouseEvent -> {
            Resource res = Resource.NOTHING;
            switch (comboBox.getValue()){
                case ROCK:
                    res = Resource.ROCK;
                    break;
                case SERVANT:
                    res = Resource.SERVANT;
                    break;
                case GOLD:
                    res = Resource.GOLD;
                    break;
                case SHIELD:
                    res = Resource.SHIELD;
                    break;
                default:
                    logger.error("resource not found");
            }
            ChooseOneResPrepMessage chooseOneResPrepMessage = new ChooseOneResPrepMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), res);
            try {
                ui.getGameHandler().dealWithMessage(chooseOneResPrepMessage);
            } catch (IOException e) {
                logger.error("error while sending request");
            }
        });
    }

    private Image getImageFromString(String str){
        switch (str){
            case ROCK:
                return ImageCache.ROCKIMG;
            case SERVANT:
                return ImageCache.SERVANTIMG;
            case GOLD:
                return ImageCache.GOLDIMG;
            case SHIELD:
                return ImageCache.SHIELDIMG;
            default:
                logger.error("resource not found");
        }
        return ImageCache.NOTHINGIMG;
    }
}
