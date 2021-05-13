package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;

import java.io.Serializable;

public class LocalDevelopmentGrid extends Observable implements Serializable {
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

    public synchronized void setDrawnDevelop(int x, int y, LocalDevelopCard topDevelopCard){
        setDevelopCardNumber(x, y, getDevelopCardsNumber()[x][y]-1);
        setTopDevelopCard(x, y, topDevelopCard);
    }

    public LocalDevelopmentGrid(LocalDevelopCard[][] topDevelopCards,  int[][] developCardsNumber){
        this.topDevelopCards = topDevelopCards;
        this.developCardsNumber = developCardsNumber;
    }

    public LocalDevelopmentGrid(){
        topDevelopCards = new LocalDevelopCard[4][3];
        developCardsNumber = new int[4][3];
    }
}
