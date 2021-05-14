package it.polimi.ingsw.messages.requests;

import it.polimi.ingsw.server.model.cards.leader.LeaderCard;

import java.util.ArrayList;

/**
 * choose the leader card to remove
 */
public class RemoveLeaderPrepMessage extends ClientMessage{

    private static final long serialVersionUID = 115L;
    private final ArrayList<Integer> leadersIdToRemove;

        public RemoveLeaderPrepMessage(int gameId, int playerId, ArrayList<Integer> leadersIdToRemove) {
            super(gameId, playerId);
            this.leadersIdToRemove =new ArrayList<>();
            this.leadersIdToRemove.addAll(leadersIdToRemove);
        }

        public ArrayList<Integer> getLeadersToRemove() {
            return leadersIdToRemove;
        }
}

