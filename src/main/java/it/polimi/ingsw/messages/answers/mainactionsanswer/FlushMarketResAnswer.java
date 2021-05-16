package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class FlushMarketResAnswer extends Answer {

    private final ArrayList<LocalTrack> localTracks;
    private final TreeMap<Resource, Integer> resInNormalDeposit;
    private final ArrayList<LocalDepotLeader> localDepotLeaders;


    /**
     * @param gameId             current game id
     * @param playerId           id of the player who sent the request
     * @param localTracks
     * @param resInNormalDeposit
     * @param localDepotLeaders
     */
    public FlushMarketResAnswer(int gameId, int playerId, ArrayList<LocalTrack> localTracks, TreeMap<Resource, Integer> resInNormalDeposit, ArrayList<LocalDepotLeader> localDepotLeaders) {
        super(gameId, playerId);
        this.localTracks = localTracks;
        this.resInNormalDeposit = resInNormalDeposit;
        this.localDepotLeaders = localDepotLeaders;
    }
}
