package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.TreeMap;

public class FlushProductionResAnswer extends Answer {
    /**
     * total of the resources flushed by the productions activated
     */
    private final TreeMap<Resource,Integer> totGainedResources;

    public FlushProductionResAnswer(int gameId, int playerId, TreeMap<Resource, Integer> totGainedResources) {
        super(gameId, playerId);
        this.totGainedResources = new TreeMap<>(totGainedResources);
    }

    public TreeMap<Resource, Integer> getTotGainedResources() {
        return totGainedResources;
    }
}
