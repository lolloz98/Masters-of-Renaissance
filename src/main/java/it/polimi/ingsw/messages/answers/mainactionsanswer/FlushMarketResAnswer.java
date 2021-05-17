package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class FlushMarketResAnswer extends Answer {

    private final ArrayList<LocalTrack> localTracks;
    private final TreeMap<Resource, Integer> resInNormalDeposit;
    private final ArrayList<LocalDepotLeader> localDepotLeaders;
    private final LocalTrack lorenzoTrack;


    /**
     * @param gameId             current game id
     * @param playerId           id of the player who sent the request
     * @param localTracks
     * @param resInNormalDeposit
     * @param localDepotLeaders
     */
    public FlushMarketResAnswer(int gameId, int playerId, ArrayList<LocalTrack> localTracks, TreeMap<Resource, Integer> resInNormalDeposit, ArrayList<LocalDepotLeader> localDepotLeaders, LocalTrack lorenzoTrack) {
        super(gameId, playerId);
        this.localTracks = localTracks;
        this.resInNormalDeposit = resInNormalDeposit;
        this.localDepotLeaders = localDepotLeaders;
        this.lorenzoTrack=lorenzoTrack;
    }

    public LocalTrack getLorenzoTrack() {
        return lorenzoTrack;
    }

    public ArrayList<LocalTrack> getLocalTracks() {
        return localTracks;
    }

    public TreeMap<Resource, Integer> getResInNormalDeposit() {
        return resInNormalDeposit;
    }

    public ArrayList<LocalDepotLeader> getLocalDepotLeaders() {
        return localDepotLeaders;
    }
}
