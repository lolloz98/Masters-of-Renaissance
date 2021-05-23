package it.polimi.ingsw.client.gui.componentsgui;

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

public class SlotDevelopComponent extends Pane {
    private static final Logger logger = LogManager.getLogger(SlotDevelopComponent.class);

    @FXML
    private CoveredCardComponent coveredCard1;
    @FXML
    private CoveredCardComponent coveredCard2;
    @FXML
    private ImageView activeCard;
    @FXML
    private Button activateBtn;

    private LocalDevelopCard localDevelopCard;

    public void addCard(LocalDevelopCard localDevelopCard){
        if(coveredCard2.getDevelopCard() != null) {
            logger.error("stopped from trying to add fourth card to slot develop");
            return;
        }
        coveredCard2.setDevelopCard(coveredCard1.getDevelopCard());
        coveredCard1.setDevelopCard(localDevelopCard);
        this.localDevelopCard = localDevelopCard;
        // Todo update activeCard imageView
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

        activateBtn.setOnMouseClicked(mouseEvent -> {
            logger.debug("clicked activate");
            if(localDevelopCard != null){
                // TODO: activate production
            }
        });
    }
}
