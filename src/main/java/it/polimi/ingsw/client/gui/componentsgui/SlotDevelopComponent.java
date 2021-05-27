package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class SlotDevelopComponent extends Pane {
    private static final Logger logger = LogManager.getLogger(SlotDevelopComponent.class);

    public void setDisableActivateBtn(boolean hasBeenActivated) {
        activateBtn.setDisable(hasBeenActivated);
    }


    @FXML
    private CoveredCardComponent coveredCard1;
    @FXML
    private CoveredCardComponent coveredCard2;
    @FXML
    private ImageView activeCard;
    @FXML
    private Button activateBtn;

    private LocalDevelopCard localDevelopCard;

    public void setCards(List<LocalDevelopCard> localDevelopCards){
        int lastPos = localDevelopCards.size() - 1;
        for(int i = lastPos; i >= 0; i--){
            if(i == lastPos) localDevelopCard = localDevelopCards.get(i);
            else if(i == lastPos - 1) coveredCard1.setDevelopCard(localDevelopCards.get(i));
            else if(i == lastPos - 2) coveredCard2.setDevelopCard(localDevelopCards.get(i));
        }
        if(localDevelopCard != null) activeCard.setImage(localDevelopCard.getImage());
        else activeCard.setImage(ImageCache.EMPTY_CARD);
    }

    public Button getActivateBtn() {
        return activateBtn;
    }
    public LocalDevelopCard getLocalDevelopCard(){
        return localDevelopCard;
    }

    public SlotDevelopComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/slot_develop.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
