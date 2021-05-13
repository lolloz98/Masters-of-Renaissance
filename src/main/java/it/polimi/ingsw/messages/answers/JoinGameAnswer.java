package it.polimi.ingsw.messages.answers;

import java.util.ArrayList;

public class JoinGameAnswer extends Answer {
    private static final long serialVersionUID = 52L;
    private final ArrayList<Integer> playerIds;
    public JoinGameAnswer(int gameId, int playerId, ArrayList<Integer> playerIds) {
        super(gameId, playerId);
        this.playerIds = playerIds;
    }

    public ArrayList<Integer> getPlayerIds() {
        return playerIds;
    }
}
