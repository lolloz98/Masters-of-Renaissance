package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;

public class LocalDevelopmentGrid extends Observable {
    private LocalDevelopCard[][] topDevelopCards;
    private int[][] developCardsNumber;

    public synchronized LocalDevelopCard[][] getTopDevelopCards() {
        return topDevelopCards;
    }

    public synchronized void setTopDevelopCards(LocalDevelopCard[][] topDevelopCards) {
        this.topDevelopCards = topDevelopCards;
    }

    public synchronized int[][] getDevelopCardsNumber() {
        return developCardsNumber;
    }

    public synchronized void setDevelopCardsNumber(int[][] developCardsNumber) {
        this.developCardsNumber = developCardsNumber;
        notifyObserver();
    }

    public LocalDevelopmentGrid(){
        topDevelopCards = new LocalDevelopCard[4][3];
        developCardsNumber = new int[4][3];
    }
}
