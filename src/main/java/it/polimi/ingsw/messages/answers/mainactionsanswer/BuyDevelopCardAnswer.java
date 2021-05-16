package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.messages.answers.Answer;

public class BuyDevelopCardAnswer extends Answer {
    private final LocalBoard localBoard;
    private final LocalDevelopmentGrid localGrid;
    private final int whichSlot;

    /**
     * @param gameId     current game id
     * @param playerId   id of the player who sent the request
     * @param localBoard
     * @param localGrid
     * @param whichSlot
     */
    public BuyDevelopCardAnswer(int gameId, int playerId, LocalBoard localBoard, LocalDevelopmentGrid localGrid, int whichSlot) {
        super(gameId, playerId);
        this.localBoard = localBoard;
        this.localGrid = localGrid;
        this.whichSlot = whichSlot;
    }

    public LocalBoard getLocalBoard() {
        return localBoard;
    }

    public LocalDevelopmentGrid getLocalGrid() {
        return localGrid;
    }

    public int getWhichSlot() {
        return whichSlot;
    }
}
