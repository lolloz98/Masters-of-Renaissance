package it.polimi.ingsw.messages.answers.leaderanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;

import java.util.ArrayList;

public class DiscardLeaderAnswer extends LeaderAnswer {
    private final ArrayList<LocalTrack> localTracks;
    private final LocalTrack lorenzoTrack;

    public DiscardLeaderAnswer(int gameId, int playerId, LocalCard leader, ArrayList<LocalTrack> localTracks, LocalTrack lorenzoTrack) {
        super(gameId, playerId,leader);
        this.localTracks = localTracks;
        this.lorenzoTrack = lorenzoTrack;
    }

    public LocalTrack getLorenzoTrack() {
        return lorenzoTrack;
    }

    public ArrayList<LocalTrack> getLocalTracks() {
        return localTracks;
    }
}
