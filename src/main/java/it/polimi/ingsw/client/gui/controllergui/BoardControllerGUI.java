package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.FaithTrackComponent;
import it.polimi.ingsw.client.gui.componentsgui.LeaderSlotComponent;
import it.polimi.ingsw.client.gui.componentsgui.SlotDevelopComponent;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BoardControllerGUI implements ControllerGUI, Observer {
    private static final Logger logger = LogManager.getLogger(BoardControllerGUI.class);

    private Stage stage;
    private Parent root;
    private GUI ui;

    public FaithTrackComponent faithTrackComponent;
    public SlotDevelopComponent slotDevelopComponent1;
    public SlotDevelopComponent slotDevelopComponent2;
    public SlotDevelopComponent slotDevelopComponent3;
    public Button activateNormalBtn;

    public Button optional1Btn;
    public Button optional2Btn;

    public Button marketBtn;
    public Button developBtn;
    public Button flushBtn;
    public Label messageLbl;
    public LeaderSlotComponent leader1;
    public LeaderSlotComponent leader2;

    public void resetDefault(){
        optional1Btn.setOnMouseClicked(mouseEvent -> {
            logger.debug("event handler added by cleanup");
        });
        optional1Btn.setDisable(false);
        optional1Btn.setVisible(true);

        optional2Btn.setOnMouseClicked(mouseEvent -> {
            logger.debug("event handler added by cleanup");
        });
        optional2Btn.setDisable(false);
        optional2Btn.setVisible(true);
        flushBtn.setDisable(true);
    }

    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        this.ui = ui;
        this.stage = stage;
        this.root = root;
        marketBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toMarket(stage, ui);
        });
        developBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });

        setUpOnState();
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    private void setUpOnState(){
        switch(ui.getLocalGame().getState()){
            case PREP_RESOURCES:
                Platform.runLater(() -> {
                    resetDefault();
                    optional1Btn.setDisable(false);
                    optional1Btn.setVisible(true);
                    optional1Btn.setText("Choose Init Resources");
                    optional1Btn.setOnMouseClicked(mouseEvent -> {
                        BuildGUI.getInstance().toChooseInitRes(stage, ui);
                        logger.debug("optional1Btn clicked");
                    });
                });
                break;
            case PREP_LEADERS:
                Platform.runLater(() -> {
                    optional1Btn.setDisable(false);
                    optional1Btn.setVisible(true);
                    optional1Btn.setText("Remove Leaders");
                    optional1Btn.setOnMouseClicked(mouseEvent -> {
                        BuildGUI.getInstance().toRemoveLeaders(stage, ui);
                        logger.debug("optional1Btn clicked");
                    });
                });
                break;
            case READY:
                break;
            case OVER:
                break;
            default:
                logger.error("Invalid state: " + ui.getLocalGame().getState());
                break;
        }
    }
}
