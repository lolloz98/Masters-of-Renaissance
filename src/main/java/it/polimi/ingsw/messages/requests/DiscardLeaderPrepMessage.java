package it.polimi.ingsw.messages.requests;

import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;

/**
 * choose the leader card to discard
 */
public class DiscardLeaderPrepMessage extends ClientMessage{

    private static final long serialVersionUID = 115L;
    private final ArrayList<LeaderCard> leadersToDiscard;

        public DiscardLeaderPrepMessage(int gameId, int playerId, Resource res, ArrayList<LeaderCard> leaders) {
            super(gameId, playerId);
            leadersToDiscard=new ArrayList<>();
            leadersToDiscard.addAll(leaders);
        }

        public ArrayList<LeaderCard> getLeadersToDiscard() {
            return leadersToDiscard;
        }
}

