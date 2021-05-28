package it.polimi.ingsw.client.gui.controllergui;

import com.sun.javafx.collections.ObservableListWrapper;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.componentsgui.*;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.messages.requests.FinishTurnMessage;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardControllerGUI extends ControllerGUI implements Observer {
    private static final Logger logger = LogManager.getLogger(BoardControllerGUI.class);
    public ChoiceBox<PairId<Integer, String>> chooseBoard;

    private final ObservableList<PairId<Integer, String>> myList = new ObservableListWrapper<>(new ArrayList<>());

    public FaithTrackComponent faithTrackComponent;
    public SlotDevelopComponent slotDevelopComponent1;
    public SlotDevelopComponent slotDevelopComponent2;
    public SlotDevelopComponent slotDevelopComponent3;

    private final List<SlotDevelopComponent> developSlots = new ArrayList<>();

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
    public StrongBoxComponent strongBoxCmp;
    public Label historyLbl;

    private LocalGameState prevGameState;

    /**
     * set the various elements of the gui
     */
    public void setBoard() {
        synchronized (ui.getLocalGame()) {
            if (ui.getWhoIAmSeeingId() == ui.getLocalGame().getMainPlayer().getId()) {
                setMainPlayerBoard();
            } else {
                setNotMainPlayerBoard();
            }
            prevGameState = ui.getLocalGame().getState();
        }
    }

    public void setMainPlayerBoard() {
        setBaseBoard();
        setUpOnState();
    }

    public void setNotMainPlayerBoard() {
        setBaseBoard();
        setVisibleButtonsActions(false);
    }

    /**
     * set things in common to both not main player and main player
     */
    public void setBaseBoard() {
        LocalPlayer seen = ui.getLocalGame().getPlayerById(ui.getWhoIAmSeeingId());

        List<LocalCard> leaders = seen.getLocalBoard().getLeaderCards();
        if (leaders.size() == 2) {
            leader1.setCard(leaders.get(0), ui, stage);
            leader2.setCard(leaders.get(1), ui, stage);
        }

        List<ArrayList<LocalDevelopCard>> develops = seen.getLocalBoard().getDevelopCards();
        int i = 0;
        for (ArrayList<LocalDevelopCard> ald : develops) {
            developSlots.get(i).setCards(ald);
            if(ald.isEmpty()) developSlots.get(i).setDisableActivateBtn(true);
            else developSlots.get(i).setDisableActivateBtn(!ald.get(ald.size() - 1).getProduction().getResToFlush().isEmpty());
            i++;
        }

        faithTrackComponent.setCurrentFaith(seen.getLocalBoard().getLocalTrack().getFaithTrackScore());
        faithTrackComponent.setFigureStates(seen.getLocalBoard().getLocalTrack().getFiguresState());
        if(ImageCache.isSinglePlayer()){
            LocalSingle game = (LocalSingle) ui.getLocalGame();
            faithTrackComponent.setCurrentLorenzo(game.getLorenzoTrack().getFaithTrackScore());
        }

        depotCmp.setImages(seen.getLocalBoard().getResInNormalDepot());
        strongBoxCmp.updateRes(seen.getLocalBoard().getResInStrongBox());

        historyLbl.setText(ui.getLocalGame().getLocalTurn().getHistoryObservable().getLast());
    }


    public void defaultForReady() {
        optional1Btn.setVisible(false);

        optional2Btn.setVisible(false);

        flushBtn.setDisable(true);

        messageLbl.setText("");

        setVisibleButtonsActions(true);

        setDisableProductions(false);
    }

    /**
     * set up the view (only if board is of main player) depending on the localGame status
     */
    private void setUpOnState() {
        LocalGame<?> game = ui.getLocalGame();
        switch (game.getState()) {
            case PREP_RESOURCES:
                setVisibleButtonsActions(false);
                if (game.getMainPlayer().getLocalBoard().getInitialRes() != 0) {
                    emphasisOnButton(optional1Btn);
                    optional1Btn.setDisable(false);
                    optional1Btn.setVisible(true);
                    optional1Btn.setText("Choose Init Resources");
                    optional1Btn.setOnMouseClicked(mouseEvent -> {
                        logger.debug("optional1Btn clicked");
                        removeThisAsObserver();
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
                        removeThisAsObserver();
                        BuildGUI.getInstance().toRemoveLeaders(stage, ui);
                    });
                } else {
                    messageLbl.setText("Wait for other players to remove their leaders");
                }
                break;
            case READY:
                defaultForReady();
                setUpReady();
                break;
            case OVER:
                // todo
                break;
            default:
                logger.error("Invalid state: " + ui.getLocalGame().getState());
                break;
        }
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
                    FinishTurnMessage finishTurnMessage = new FinishTurnMessage(game.getGameId(), game.getMainPlayer().getId());
                    try {
                        ui.getGameHandler().dealWithMessage(finishTurnMessage);
                    } catch (IOException e) {
                        logger.error("Something happened while sending finishTurnMessage: " + e);
                    }
                });
            }else {
                setVisibleButtonsActions(true);
            }
        } else {
            setVisibleButtonsActions(false);
        }
    }

    /**
     * called from buildGUI, to setup this view
     */
    @Override
    public void setUp(Stage stage, Parent root, GUI ui) {
        setLocalVariables(stage, root, ui);
        marketBtn.setOnMouseClicked(mouseEvent -> {
            removeThisAsObserver();
            BuildGUI.getInstance().toMarket(stage, ui);
        });
        developBtn.setOnMouseClicked(mouseEvent -> {
            removeThisAsObserver();
            BuildGUI.getInstance().toDevelopGrid(stage, ui);
        });

        developSlots.add(slotDevelopComponent1);
        developSlots.add(slotDevelopComponent2);
        developSlots.add(slotDevelopComponent3);

        for(int i = 0; i < developSlots.size(); i++){
            SlotDevelopComponent s = developSlots.get(i);
            int finalI = i;
            s.getActivateBtn().setOnMouseClicked(mouseEvent -> {
                synchronized (ui.getLocalGame()) {
                    if (s.getLocalDevelopCard() != null && s.getLocalDevelopCard().getProduction().getResToFlush().isEmpty()) {
                        int whichProd = finalI + 1;
                        BuildGUI.getInstance().toActivateProduction(stage, ui, s.getLocalDevelopCard(), whichProd);
                    }else{
                        logger.warn("Production already activated or developCard is null");
                    }
                }
            });
        }

        activateNormalBtn.setOnMouseClicked(mouseEvent -> {
            synchronized (ui.getLocalGame()) {
                if(ui.getLocalGame().getPlayerById(ui.getWhoIAmSeeingId()).getLocalBoard().getBaseProduction().getResToFlush().isEmpty()) {
                    BuildGUI.getInstance().toActivateProduction(stage, ui, NormalProductionCard.getINSTANCE(), 0);
                } else{
                    logger.warn("Normal production already activated");
                }
            }
        });

        LocalGame<?> game = ui.getLocalGame();
        if (game instanceof LocalSingle)
            chooseBoard.setVisible(false);
        else {
            for (LocalPlayer p : game.getLocalPlayers()) {
                PairId<Integer, String> tmp = new PairId<>(p.getId(), p.getName());
                myList.add(tmp);
                if(ui.getWhoIAmSeeingId() == tmp.getFirst()) chooseBoard.setValue(tmp);
            }
            chooseBoard.setItems(myList);
            chooseBoard.setOnAction((event) -> {
                // int selectedIndex = chooseBoard.getSelectionModel().getSelectedIndex();
                PairId<Integer, String> selectedItem = chooseBoard.getSelectionModel().getSelectedItem();
                ui.setWhoIAmSeeingId(selectedItem.getFirst());
                removeThisAsObserver();
                BuildGUI.getInstance().toBoard(stage, ui);
            });
        }

        game.getPlayerById(ui.getWhoIAmSeeingId()).overrideObserver(this);
        game.getPlayerById(ui.getWhoIAmSeeingId()).getLocalBoard().overrideObserver(this);
        game.getError().addObserver(this);
        game.getLocalTurn().overrideObserver(this);
        game.overrideObserver(new Observer() {
            @Override
            public void notifyUpdate() {
                Platform.runLater(() -> {
                    synchronized (ui.getLocalGame()) {
                        logger.debug("check game status");
                        if (prevGameState != ui.getLocalGame().getState()) {
                            logger.debug("resetting board");
                            setBoard();
                        }
                    }
                });
            }

            @Override
            public void notifyError() {
                // Nothing to do here
            }
        });
        game.getLocalTurn().getHistoryObservable().overrideObserver(new Observer() {
            @Override
            public void notifyUpdate() {
                Platform.runLater(() -> {
                    synchronized (ui.getLocalGame()) {
                        logger.debug("In notifyUpdate from history");
                        historyLbl.setText(ui.getLocalGame().getLocalTurn().getHistoryObservable().getLast());
                    }
                });
            }

            @Override
            public void notifyError() {
                // Nothing to do here
            }
        });

        setBoard();
    }


    public void removeThisAsObserver(){
        synchronized (ui.getLocalGame()) {
            ui.getLocalGame().removeObservers();
            ui.getLocalGame().getPlayerById(ui.getWhoIAmSeeingId()).removeObservers();
            ui.getLocalGame().getError().removeObserver();
            ui.getLocalGame().getLocalTurn().removeObservers();
            ui.getLocalGame().getLocalTurn().getHistoryObservable().removeObservers();
            ui.getLocalGame().getPlayerById(ui.getWhoIAmSeeingId()).getLocalBoard().removeObservers();
        }
    }

    @Override
    public void notifyUpdate() {
        Platform.runLater(() -> {
            synchronized (ui.getLocalGame()) {
                setBoard();
            }
        });
    }

    @Override
    public void notifyError() {
        Platform.runLater(() -> messageLbl.setText(ui.getLocalGame().getError().getErrorMessage()));
    }

    private void emphasisOnButton(Button btn) {
        btn.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
    }

    private void setVisibleButtonsActions(boolean bool) {
        optional1Btn.setVisible(false);
        optional2Btn.setVisible(false);
        slotDevelopComponent1.getActivateBtn().setVisible(bool);
        slotDevelopComponent2.getActivateBtn().setVisible(bool);
        slotDevelopComponent3.getActivateBtn().setVisible(bool);
        activateNormalBtn.setVisible(bool);
        if(!bool) {
            // if true, better handling in leaderSlot
            logger.debug("setting invisible buttons action");
            leader1.setVisibleButtons(false);
            leader2.setVisibleButtons(false);
        }
    }

    public void setDisableProductions(Boolean bool) {
        activateNormalBtn.setDisable(bool);
        setDisableProduction(slotDevelopComponent1, bool);
        setDisableProduction(slotDevelopComponent2, bool);
        setDisableProduction(slotDevelopComponent3, bool);
        leader1.setDisableProduction(bool);
        leader2.setDisableProduction(bool);
    }


    private void setDisableProduction(SlotDevelopComponent s, Boolean bool) {
        s.getActivateBtn().setDisable(bool);
    }
}
