package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;

public class LocalDevelopmentGrid extends LocalModelAbstract {
    private UI ui;
    private LocalDevelopCard[][] topDevelopCards;
    private int[][] developCardsNumber;

    public LocalDevelopCard[][] getTopDevelopCards() {
        return topDevelopCards;
    }

    public void setTopDevelopCards(LocalDevelopCard[][] topDevelopCards) {
        this.topDevelopCards = topDevelopCards;
    }

    public int[][] getDevelopCardsNumber() {
        return developCardsNumber;
    }

    public void setDevelopCardsNumber(int[][] developCardsNumber) {
        this.developCardsNumber = developCardsNumber;
    }

    public LocalDevelopmentGrid(UI ui){
        topDevelopCards = new LocalDevelopCard[4][3];
        developCardsNumber = new int[4][3];
        this.ui=ui;
    }
}
