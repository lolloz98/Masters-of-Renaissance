package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class LocalBoard extends LocalModelAbstract {
    private LocalDevelopCard[] topDevelopCards;
    private int[] developCardsNumber;
    private int faithTrackScore;
    private int playerId;
    private String playerName;
    private TreeMap<Resource, Integer> resInStrongBox;
    private TreeMap<Resource, Integer> resInNormalDeposit;
    private LeaderCard[] leaderCards;
    private UI ui;

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
        ui.notifyAction(this);
    }

    public synchronized LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    public synchronized void setLeaderCards(LeaderCard[] leaderCards) {
        this.leaderCards = leaderCards;
        ui.notifyAction(this);
    }

    public synchronized int getFaithTrackScore() {
        return faithTrackScore;
    }

    public synchronized void setFaithTrackScore(int faithTrackScore) {
        this.faithTrackScore = faithTrackScore;
        ui.notifyAction(this);
    }

    public synchronized int getPlayerId() {
        return playerId;
    }

    public synchronized void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public synchronized String getPlayerName() {
        return playerName;
    }

    public synchronized void setPlayerName(String playerName) {
        this.playerName = playerName;
        ui.notifyAction(this);
    }

    public LocalBoard(UI ui){
        this.ui = ui;
        developCardsNumber = new int[3];
        topDevelopCards = new LocalDevelopCard[3];
        leaderCards = new LeaderCard[2];
        // todo: modify with real constructor
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
    }
}
