package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.server.model.cards.leader.MarbleLeaderCard;
import it.polimi.ingsw.server.model.cards.leader.ProductionLeaderCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaderSlotComponent extends VBox {
    private static final Logger logger = LogManager.getLogger(SlotDevelopComponent.class);

    private GUI ui;

    @FXML
    private ImageView cardImg;
    @FXML
    private Button activateBtn;
    @FXML
    private Button discardBtn;

    @FXML
    private Label statusLbl;

    // useful if depot leader
    @FXML
    private ImageView depot0;
    @FXML
    private ImageView depot1;
    private final List<ImageView> depots = new ArrayList<>();

    private LocalCard leaderCard;

    public LocalCard getLeaderCard() {
        return leaderCard;
    }

    private void setUi(GUI ui) {
        if(this.ui == null) {
            this.ui = ui;
            activateBtn.setOnMouseClicked(mouseEvent -> {
                synchronized (ui.getLocalGame()) {
                    ActivateLeaderMessage activateLeaderMessage = new ActivateLeaderMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), leaderCard.getId());
                    try {
                        ui.getGameHandler().dealWithMessage(activateLeaderMessage);
                    } catch (IOException e) {
                        logger.debug("Something went wrong while handling the message");
                    }
                }
            });
            discardBtn.setOnMouseClicked(mouseEvent -> {
                synchronized (ui.getLocalGame()) {
                    DiscardLeaderMessage discardLeaderMessage = new DiscardLeaderMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId(), leaderCard.getId());
                    try {
                        ui.getGameHandler().dealWithMessage(discardLeaderMessage);
                    } catch (IOException e) {
                        logger.debug("Something went wrong while handling the message");
                    }
                }
            });
        }
    }

    public void setCard(LocalCard card, GUI ui){
        this.leaderCard = card;
        setUi(ui);
        cardImg.setImage(card.getImage());
        if(leaderCard instanceof LocalLeaderCard && ((LocalLeaderCard) leaderCard).isActive()){
            discardBtn.setVisible(false);
            statusLbl.setText("Active");
            statusLbl.setTextFill(Color.GREEN);
            if(leaderCard instanceof LocalDepotLeader){
                activateBtn.setVisible(false);
                LocalDepotLeader l = (LocalDepotLeader) leaderCard;
                for(int i = 0; i < l.getNumberOfRes(); i++){
                    ImageCache.setImageInStore(l.getResType(), depots.get(i));
                    depots.get(i).setVisible(true);
                }
            }else if(leaderCard instanceof LocalMarbleLeader){
                activateBtn.setVisible(false);
            }
            else if(leaderCard instanceof LocalProductionLeader){
                activateBtn.setText("Activate Production");
                activateBtn.setOnMouseClicked(mouseEvent -> {
                    logger.debug("clicked activate of localProduction leader");
                    // todo: add production behavior
                });
            }
        } else if((leaderCard instanceof LocalLeaderCard && ((LocalLeaderCard) leaderCard).isDiscarded()) ||
                (leaderCard instanceof LocalConcealedCard && ((LocalConcealedCard) leaderCard).isDiscarded())
        ){
            statusLbl.setText("Discarded");
            statusLbl.setTextFill(Color.RED);
            discardBtn.setVisible(false);
            activateBtn.setVisible(false);
        }
    }

    public LeaderSlotComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/leader_slot.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        activateBtn.setOnMouseClicked(mouseEvent -> {

        });

        depots.add(depot0);
        depots.add(depot1);
    }

    public void setVisibleButtons(boolean bool) {
        activateBtn.setVisible(bool);
        discardBtn.setVisible(bool);
    }

    public void setDisableProduction(boolean bool) {
        if(leaderCard instanceof LocalProductionLeader){
            activateBtn.setDisable(bool);
        }
    }
}
