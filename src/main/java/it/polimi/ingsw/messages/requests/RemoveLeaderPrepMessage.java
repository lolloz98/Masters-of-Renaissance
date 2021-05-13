package it.polimi.ingsw.messages.requests;

import it.polimi.ingsw.server.model.cards.leader.LeaderCard;

import java.util.ArrayList;

/**
 * choose the leader card to remove
 */
public class RemoveLeaderPrepMessage extends ClientMessage{

    private static final long serialVersionUID = 115L;
    private final ArrayList<LeaderCard<?>> leadersToRemove;

        public RemoveLeaderPrepMessage(int gameId, int playerId, ArrayList<LeaderCard<?>> leaders) {
            super(gameId, playerId);
            leadersToRemove =new ArrayList<>();
            leadersToRemove.addAll(leaders);
        }

        public ArrayList<LeaderCard<?>> getLeadersToRemove() {
            return leadersToRemove;
        }
}

