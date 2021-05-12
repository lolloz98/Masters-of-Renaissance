package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;

public class LocalDevelopmentGrid extends Observable {
    private LocalDevelopCard[][] topDevelopCards;
    private int[][] developCardsNumber;

    public synchronized LocalDevelopCard[][] getTopDevelopCards() {
        return topDevelopCards;
    }

    public synchronized void setTopDevelopCards(LocalDevelopCard[][] topDevelopCards) {
        this.topDevelopCards = topDevelopCards;
        notifyObserver();
    }

    public synchronized int[][] getDevelopCardsNumber() {
        return developCardsNumber;
    }

    public synchronized void setDevelopCardsNumber(int[][] developCardsNumber) {
        this.developCardsNumber = developCardsNumber;
    }

    public synchronized void setTopDevelopCard(int x, int y, LocalDevelopCard topDevelopCard) {
        this.topDevelopCards[x][y] = topDevelopCard;
        notifyObserver();
    }

    public synchronized void setDevelopCardNumber(int x, int y, int number) {
        this.developCardsNumber[x][y] = number;
    }

    public LocalDevelopmentGrid(){
        topDevelopCards = new LocalDevelopCard[4][3];
        developCardsNumber = new int[4][3];
    }
}
