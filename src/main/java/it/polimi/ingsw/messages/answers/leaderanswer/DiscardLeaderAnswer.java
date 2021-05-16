package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;

import java.util.ArrayList;

public class DiscardLeaderAnswer extends LeaderAnswer {
    private final ArrayList<LocalTrack> localTracks;

    public DiscardLeaderAnswer(int gameId, int playerId, LocalLeaderCard leader, ArrayList<LocalTrack> localTracks) {
        super(gameId, playerId,leader);
        this.localTracks = localTracks;
    }

    public ArrayList<LocalTrack> getLocalTracks() {
        return localTracks;
    }
}
