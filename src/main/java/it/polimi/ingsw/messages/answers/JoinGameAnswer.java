package it.polimi.ingsw.messages.answers;

import java.util.ArrayList;

public class JoinGameAnswer extends Answer {
    private static final long serialVersionUID = 52L;
    private final ArrayList<Integer> playerIds;
    private final ArrayList<String> playerNames;

    public JoinGameAnswer(int gameId, int playerId, ArrayList<Integer> playerIds, ArrayList<String> playerNames) {
        super(gameId, playerId);
        this.playerIds = playerIds;
        this.playerNames = playerNames;
    }

    public ArrayList<Integer> getPlayerIds() {
        return playerIds;
    }

    public ArrayList<String> getPlayerNames(){
        return playerNames;
    }
}
