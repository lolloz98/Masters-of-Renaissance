package it.polimi.ingsw.messages.answers.leader;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;

public class ActivateMarbleLeaderAnswer extends Answer {
    private final ArrayList<Resource> leaderResources;

    public ActivateMarbleLeaderAnswer(int gameId, int playerId, ArrayList<Resource> leaderResources) {
        super(gameId, playerId);
        this.leaderResources = new ArrayList<>(leaderResources);
    }

    public ArrayList<Resource> getLeaderResources() {
        return leaderResources;
    }
}
