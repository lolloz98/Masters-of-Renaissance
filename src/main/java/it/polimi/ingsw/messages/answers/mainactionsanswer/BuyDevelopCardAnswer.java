package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.messages.answers.Answer;

public class BuyDevelopCardAnswer extends Answer {
    private final LocalBoard localBoard;
    private final LocalDevelopmentGrid localGrid;

    /**
     * @param gameId   current game id
     * @param playerId id of the player who sent the request
     * @param localBoard
     * @param localGrid
     */
    public BuyDevelopCardAnswer(int gameId, int playerId, LocalBoard localBoard, LocalDevelopmentGrid localGrid) {
        super(gameId, playerId);
        this.localBoard = localBoard;
        this.localGrid = localGrid;
    }
}
