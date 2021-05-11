package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class LocalBoard extends Observable {
    private LocalDevelopCard[] topDevelopCards;
    private int[] developCardsNumber;
    private TreeMap<Resource, Integer> resInStrongBox;
    private TreeMap<Resource, Integer> resInNormalDeposit;
    private LeaderCard[] leaderCards;
    private LocalTrack localTrack;

    public synchronized LocalTrack getLocalTrack() {
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

    public synchronized LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    public synchronized void setLeaderCards(LeaderCard[] leaderCards) {
        this.leaderCards = leaderCards;
        notifyObserver();
    }

    public LocalBoard(){
        developCardsNumber = new int[3];
        topDevelopCards = new LocalDevelopCard[3];
        leaderCards = new LeaderCard[2];
        localTrack = new LocalTrack();
        /* todo: modify with real constructor
        playerName = "firstplayer";
        resInStrongBox = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 2);
        }};
        resInNormalDeposit = new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};
        */
    }
}
