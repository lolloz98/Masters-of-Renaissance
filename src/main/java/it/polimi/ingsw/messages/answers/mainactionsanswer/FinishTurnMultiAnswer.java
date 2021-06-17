package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.messages.answers.Answer;

public class FinishTurnMultiAnswer extends Answer {
    /**
     * the leaders of the next player could change the state of the development grid
     */
    private final LocalDevelopmentGrid LocalGrid;
    private final int newCurrentPlayerId;


    public FinishTurnMultiAnswer(int gameId, int playerId, LocalDevelopmentGrid localGrid, int newCurrentPlayerId) {
        super(gameId, playerId);
        LocalGrid = localGrid;
        this.newCurrentPlayerId = newCurrentPlayerId;
    }

    public LocalDevelopmentGrid getLocalGrid() {
        return LocalGrid;
    }

    public int getNewCurrentPlayerId() {
        return newCurrentPlayerId;
    }
}
