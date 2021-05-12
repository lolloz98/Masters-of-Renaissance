package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class LocalBoard extends Observable {
    private final ArrayList<ArrayList<LocalDevelopCard>> developCards;
    private TreeMap<Resource, Integer> resInStrongBox;
    private TreeMap<Resource, Integer> resInNormalDeposit;
    private ArrayList<LocalCard> leaderCards;
    private final LocalTrack localTrack;

    public synchronized ArrayList<ArrayList<LocalDevelopCard>> getDevelopCards() {
        return developCards;
    }

    public synchronized void addDevelopCards(int i, LocalDevelopCard developCard) {
        this.developCards.get(i).add(developCard);
        notifyObserver();
    }

    public LocalTrack getLocalTrack() {
        return localTrack;
    }

    public synchronized TreeMap<Resource, Integer> getResInStrongBox() {
        return resInStrongBox;
    }

    public synchronized void setResInStrongBox(TreeMap<Resource, Integer> resInStrongBox) {
        this.resInStrongBox = resInStrongBox;
        notifyObserver();
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
        developCards = new ArrayList<>();
        for(int i=0; i<3; i++) developCards.add(new ArrayList<>());
        leaderCards = new ArrayList<>();
        localTrack = new LocalTrack();
    }
}
