package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class FlushProductionResAnswer extends Answer {
    private static final long serialVersionUID = 38L;

    /**
     * total of the resources flushed by the productions activated, to add to the strongbox
     */
    private final TreeMap<Resource, Integer> resInStrongbox;
    private final ArrayList<LocalTrack> localTracks;

    public FlushProductionResAnswer(int gameId, int playerId, TreeMap<Resource, Integer> resInStrongbox, ArrayList<LocalTrack> localTracks) {
        super(gameId, playerId);
        this.resInStrongbox = new TreeMap<>(resInStrongbox);
        this.localTracks = localTracks;
    }

    public ArrayList<LocalTrack> getLocalTracks() {
        return localTracks;
    }

    public TreeMap<Resource, Integer> getResInStrongbox() {
        return resInStrongbox;
    }
}
