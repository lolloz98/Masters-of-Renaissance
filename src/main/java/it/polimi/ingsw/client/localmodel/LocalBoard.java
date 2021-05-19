package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class LocalBoard extends Observable implements Serializable {
    private ArrayList<ArrayList<LocalDevelopCard>> developCards;
    private final LocalProduction baseProduction;
    private TreeMap<Resource, Integer> resInStrongBox;
    private TreeMap<Resource, Integer> resInNormalDepot;
    private ArrayList<LocalCard> leaderCards;
    /**
     * notify the board for an update of the local track, it doesn't own an observer
     */
    private LocalTrack localTrack;
    private int initialRes; // fixme: probably should be removed

    /**
     * method that updates the leader depots of this localBoard
     * @param updatedLeaders array list of updated leaders
     */
    public void updateLeaderDepots(ArrayList<LocalDepotLeader> updatedLeaders) {
        for (LocalCard localLeader : leaderCards) {
            for (LocalDepotLeader updated : updatedLeaders) {
                if (localLeader.getId() == updated.getId()) {
                    LocalDepotLeader toUpdate = (LocalDepotLeader) localLeader;
                    toUpdate.setNumberOfRes(updated.getNumberOfRes());
                }
            }
        }
    }

    /**
     * empties the gained resources in all productions
     */
    public void flushFromProductions(){
        baseProduction.setResToFlush(new TreeMap<>());
        for(ArrayList<LocalDevelopCard> ad: developCards){
            for(LocalDevelopCard d: ad) d.getProduction().setResToFlush(new TreeMap<>());
        }
        for(LocalCard l: leaderCards){
            if(l instanceof LocalDevelopCard) ((LocalDevelopCard) l).getProduction().setResToFlush(new TreeMap<>());
        }
    }

    public synchronized ArrayList<ArrayList<LocalDevelopCard>> getDevelopCards() {
        return developCards;
    }

    public void setDevelopCards(ArrayList<ArrayList<LocalDevelopCard>> developCards) {
        this.developCards = developCards;
    }

    public void setLocalTrack(LocalTrack localTrack) {
        this.localTrack = localTrack;
    }

    public synchronized void addDevelopCards(int i, LocalDevelopCard developCard) {
        this.developCards.get(i).add(developCard);
    }

    public synchronized int getResInDepotNumber(){
        int sum = 0;
        for(Resource r : Resource.values()){
            sum = sum + resInNormalDepot.getOrDefault(r,0);
        }
        return sum;
    }

    public synchronized void addResInNormalDepot(Resource res){
        int resNumber = getResInNormalDepot().getOrDefault(res, 0);
        getResInNormalDepot().put(res, resNumber+1);
    }

    public LocalTrack getLocalTrack() {
        return localTrack;
    }

    public synchronized int getInitialRes() {
        return initialRes;
    }

    public synchronized void setInitialRes(int initialRes) {
        this.initialRes = initialRes;
    }

    public synchronized TreeMap<Resource, Integer> getResInStrongBox() {
        return resInStrongBox;
    }

    public synchronized void setResInStrongBox(TreeMap<Resource, Integer> resInStrongBox) {
        this.resInStrongBox = resInStrongBox;
    }

    public synchronized TreeMap<Resource, Integer> getResInNormalDepot() {
        return resInNormalDepot;
    }

    public synchronized void setResInNormalDepot(TreeMap<Resource, Integer> resInNormalDepot) {
        this.resInNormalDepot = resInNormalDepot;
    }

    public synchronized ArrayList<LocalCard> getLeaderCards() {
        return leaderCards;
    }

    public synchronized void setLeaderCards(ArrayList<LocalCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public LocalBoard(ArrayList<ArrayList<LocalDevelopCard>> developCards, ArrayList<LocalCard> leaderCards, LocalTrack localTrack, LocalProduction baseProduction, int initialRes, TreeMap<Resource, Integer> resInNormalDepot){
        this.developCards = developCards;
        this.leaderCards = leaderCards;
        this.localTrack = localTrack;
        this.baseProduction = baseProduction;
        this.initialRes = initialRes;
        this.resInNormalDepot = resInNormalDepot;
    }

    public LocalBoard(){
        this.developCards = new ArrayList<>();
        this.leaderCards = new ArrayList<>();
        this.localTrack = new LocalTrack();
        this.baseProduction = new LocalProduction();
        this.initialRes = 0;
        this.resInNormalDepot = new TreeMap<>();
    }

    public synchronized LocalProduction getBaseProduction() {
        return baseProduction;
    }
}
