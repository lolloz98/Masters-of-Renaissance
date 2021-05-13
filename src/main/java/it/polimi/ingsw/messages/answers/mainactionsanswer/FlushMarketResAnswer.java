package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;

import java.util.ArrayList;

public class FlushMarketResAnswer extends Answer {

    private final ArrayList<LocalTrack> localTracks;
//todo: put treemap of res in depots

    /**
     * @param gameId   current game id
     * @param playerId id of the player who sent the request
     * @param localTracks
     */
    public FlushMarketResAnswer(int gameId, int playerId, ArrayList<LocalTrack> localTracks) {
        super(gameId, playerId);
        this.localTracks = localTracks;
    }
}
