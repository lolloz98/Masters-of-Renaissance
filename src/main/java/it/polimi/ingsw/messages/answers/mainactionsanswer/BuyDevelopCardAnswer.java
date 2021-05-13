package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.messages.answers.Answer;

public class BuyDevelopCardAnswer extends Answer {
    //attributi da aggiungere: grid aggiornata, card comprata, slot to store
    /**
     * @param gameId   current game id
     * @param playerId id of the player who sent the request
     */
    public BuyDevelopCardAnswer(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
