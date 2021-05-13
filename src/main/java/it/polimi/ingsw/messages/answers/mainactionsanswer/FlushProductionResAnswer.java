package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class FlushProductionResAnswer extends Answer {
    /**
     * total of the resources flushed by the productions activated
     */
    private final TreeMap<Resource,Integer> totGainedResources;
    private final ArrayList<LocalTrack> localTracks;

    public FlushProductionResAnswer(int gameId, int playerId, TreeMap<Resource, Integer> totGainedResources, ArrayList<LocalTrack> localTracks) {
        super(gameId, playerId);
        this.totGainedResources = new TreeMap<>(totGainedResources);
        this.localTracks = localTracks;
    }

    public TreeMap<Resource, Integer> getTotGainedResources() {
        return totGainedResources;
    }
}
