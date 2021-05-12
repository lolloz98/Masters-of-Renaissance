package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class LocalBoard extends Observable {
    private LocalDevelopCard[] topDevelopCards;
    private int[] developCardsNumber;
    private TreeMap<Resource, Integer> resInStrongBox;
    private TreeMap<Resource, Integer> resInNormalDeposit;
    private ArrayList<LocalCard> leaderCards;
    private final LocalTrack localTrack;

    public LocalTrack getLocalTrack() {
        return localTrack;
    }

    public synchronized TreeMap<Resource, Integer> getResInStrongBox() {
        return resInStrongBox;
    }

    public synchronized void setResInStrongBox(TreeMap<Resource, Integer> resInStrongBox) {
        this.resInStrongBox = resInStrongBox;
    }

    public synchronized TreeMap<Resource, Integer> getResInNormalDeposit() {
        return resInNormalDeposit;
    }

    public synchronized void setResInNormalDeposit(TreeMap<Resource, Integer> resInNormalDeposit) {
        this.resInNormalDeposit = resInNormalDeposit;
        notifyObserver();
    }

    public synchronized ArrayList<LocalCard> getLeaderCards() {
        return leaderCards;
    }

    public synchronized void setLeaderCards(ArrayList<LocalCard> leaderCards) {
        this.leaderCards = leaderCards;
        notifyObserver();
    }

    public LocalBoard(){
        developCardsNumber = new int[3];
        topDevelopCards = new LocalDevelopCard[3];
        leaderCards = new ArrayList<>();
        localTrack = new LocalTrack();
    }
}
