package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LeaderSlotComponent extends AnchorPane {
    private static final Logger logger = LogManager.getLogger(SlotDevelopComponent.class);

    @FXML
    private ImageView cardImg;
    @FXML
    private Button activateBtn;
    @FXML
    private Button discardBtn;

    private LocalCard leaderCard;

    public LocalCard getLeaderCard() {
        return leaderCard;
    }

    public void setCard(LocalCard card){
        this.leaderCard = card;
        // Todo update activeCard imageView
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
    }

    public void setVisibleButtons(boolean bool) {
        activateBtn.setVisible(bool);
        discardBtn.setVisible(bool);
    }
}
