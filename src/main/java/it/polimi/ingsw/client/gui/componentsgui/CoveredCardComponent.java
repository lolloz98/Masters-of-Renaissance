package it.polimi.ingsw.client.gui.componentsgui;

import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Color;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CoveredCardComponent extends StackPane {
    @FXML
    private StackPane cardPane;
    @FXML
    private Label levelLbl;

    private LocalDevelopCard developCard;

    public void setDevelopCard(LocalDevelopCard developCard) {
        if(developCard == null) return;
        this.developCard = developCard;
        setColor(developCard.getColor());
        setLevelLbl(developCard.getLevel());
    }

    public LocalDevelopCard getDevelopCard() {
        return developCard;
    }

    private void setLevelLbl(int level){
        levelLbl.setText("Lv. " + level);
    }

    private void setColor(Color color){
        switch (color){
            case PURPLE:
                cardPane.setStyle("-fx-background-color: #652480");
                break;
            case GREEN:
                cardPane.setStyle("-fx-background-color: #19801C");
                break;
            case GOLD:
                cardPane.setStyle("-fx-background-color: #EEEE20");
                break;
            case BLUE:
                cardPane.setStyle("-fx-background-color: #0D2280");
                break;
        }
    }

    public CoveredCardComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/board/covered_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
