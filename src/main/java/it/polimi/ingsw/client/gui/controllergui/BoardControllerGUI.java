package it.polimi.ingsw.client.gui.controllergui;

import com.sun.javafx.collections.ObservableListWrapper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.DepotComponent;
import it.polimi.ingsw.client.gui.componentsgui.FaithTrackComponent;
import it.polimi.ingsw.client.gui.componentsgui.LeaderSlotComponent;
import it.polimi.ingsw.client.gui.componentsgui.SlotDevelopComponent;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.server.model.utility.PairId;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardControllerGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(BoardControllerGUI.class);
    public ChoiceBox<PairId<Integer, String>> chooseBoard;

    private final ObservableList<PairId<Integer, String>> myList = new ObservableListWrapper<>(new ArrayList<>());

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

    public DepotComponent depotCmp;

    public void resetDefault() {
        optional1Btn.setOnMouseClicked(mouseEvent -> {
            logger.debug("event handler added by cleanup");
        });
        optional1Btn.setDisable(true);
        optional1Btn.setVisible(false);

        optional2Btn.setOnMouseClicked(mouseEvent -> {
            logger.debug("event handler added by cleanup");
        });
        optional2Btn.setDisable(true);
        optional2Btn.setVisible(false);

        flushBtn.setDisable(true);

        messageLbl.setText("");

        setVisibleButtonsActions(true);

        setDisableProductions(false);
    }

    /**
     * called from buildGUI, to setup this view
     */
    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        marketBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toMarket(stage, ui);
        });
        developBtn.setOnMouseClicked(mouseEvent -> {
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });
        LocalGame<?> game = ui.getLocalGame();

        LocalPlayer currentPlayerOnScene = game.getPlayerById(ui.getWhoIAmSeeingId());
        List<LocalCard> leadersOnScene = currentPlayerOnScene.getLocalBoard().getLeaderCards();
        if(leadersOnScene.size() == 2){
            leader1.setCard(leadersOnScene.get(0));
            leader2.setCard(leadersOnScene.get(1));
        }

        if (game instanceof LocalSingle)
            chooseBoard.setVisible(false);
        else {
            for (LocalPlayer p : game.getLocalPlayers()) {
                myList.add(new PairId<>(p.getId(), p.getName()));
            }
            chooseBoard.setItems(myList);
            chooseBoard.setOnAction((event) -> {
                int selectedIndex = chooseBoard.getSelectionModel().getSelectedIndex();
                PairId<Integer, String> selectedItem = chooseBoard.getSelectionModel().getSelectedItem();
                ui.setWhoIAmSeeingId(selectedItem.getFirst());
                BuildGUI.getInstance().toBoard(stage, ui);
            });
        }


        setUpOnState();
    }

    @Override
    public void notifyUpdate() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                setUpOnState();
            }
        });
    }

    @Override
    public void notifyError() {
        messageLbl.setText(ui.getLocalGame().getError().getErrorMessage());
    }

    /**
     * set up the view depending on the localGame status
     */
    private void setUpOnState() {
        resetDefault();
        if (ui.getWhoIAmSeeingId() == ui.getLocalGame().getMainPlayer().getId()) {
            switch (ui.getLocalGame().getState()) {
                case PREP_RESOURCES:
                    setVisibleButtonsActions(false);
                    if (ui.getLocalGame().getMainPlayer().getLocalBoard().getInitialRes() != 0) {
                        emphasisOnButton(optional1Btn);
                        optional1Btn.setDisable(false);
                        optional1Btn.setVisible(true);
                        optional1Btn.setText("Choose Init Resources");
                        optional1Btn.setOnMouseClicked(mouseEvent -> {
                            logger.debug("optional1Btn clicked");
                            BuildGUI.getInstance().toChooseInitRes(stage, ui);
                        });
                    } else {
                        messageLbl.setText("Wait for other players to choose their resources");
                    }
                    break;
                case PREP_LEADERS:
                    setVisibleButtonsActions(false);
                    if (ui.getLocalGame().getMainPlayer().getLocalBoard().getLeaderCards().size() > 2) {
                        emphasisOnButton(optional1Btn);
                        optional1Btn.setDisable(false);
                        optional1Btn.setVisible(true);
                        optional1Btn.setText("Remove Leaders");
                        optional1Btn.setOnMouseClicked(mouseEvent -> {
                            logger.debug("optional1Btn clicked");
                            BuildGUI.getInstance().toRemoveLeaders(stage, ui);
                        });
                    } else {
                        messageLbl.setText("Wait for other players to remove their leaders");
                    }
                    break;
                case READY:
                    setUpReady();
                    break;
                case OVER:
                    break;
                default:
                    logger.error("Invalid state: " + ui.getLocalGame().getState());
                    break;
            }
        } else
            setUpViewsForOtherPlayersBoard();
    }

    public void setUpReady() {
        // main actions
        LocalGame<?> game = ui.getLocalGame();
        if (game.isMainPlayerTurn()) {
            // need to flush the market
            if (game.getLocalTurn().isMarketActivated()) {
                setDisableProductions(true);
                emphasisOnButton(marketBtn);
            } else if (game.getLocalTurn().isProductionsActivated()) {
                flushBtn.setDisable(false);
                emphasisOnButton(flushBtn);
            } else if (game.getLocalTurn().isMainActionOccurred()) {
                setDisableProductions(true);
                optional2Btn.setDisable(false);
                optional2Btn.setText("Pass Turn");
                optional2Btn.setVisible(true);
                emphasisOnButton(optional2Btn);
                optional2Btn.setOnMouseClicked(mouseEvent -> {
                    // todo send next turn
                });
            }
        } else {
            setVisibleButtonsActions(false);
        }
    }

    private void emphasisOnButton(Button btn) {
        btn.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
    }

    private void setVisibleButtonsActions(boolean bool) {
        slotDevelopComponent1.getActivateBtn().setVisible(bool);
        slotDevelopComponent2.getActivateBtn().setVisible(bool);
        slotDevelopComponent3.getActivateBtn().setVisible(bool);
        activateNormalBtn.setVisible(bool);
        leader1.setVisibleButtons(bool);
        leader2.setVisibleButtons(bool);
    }

    public void setDisableProductions(Boolean bool) {
        activateNormalBtn.setDisable(bool);
        setDisableProduction(slotDevelopComponent1, bool);
        setDisableProduction(slotDevelopComponent2, bool);
        setDisableProduction(slotDevelopComponent3, bool);
        // todo disable leaderProduction if any
    }


    private void setDisableProduction(SlotDevelopComponent s, Boolean bool) {
        s.getActivateBtn().setDisable(bool);
    }

    private void setUpViewsForOtherPlayersBoard() {
        setVisibleButtonsActions(false);
    }
}
