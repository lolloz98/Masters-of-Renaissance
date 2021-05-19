package it.polimi.ingsw.messages.answers.endgameanswer;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;

import java.util.ArrayList;


public class EndGameAnswer extends Answer {
    /**
     * list of winners: if null it means that Lorenzo is the winner
     */
    private final ArrayList<LocalPlayer> winners;

    public EndGameAnswer(int gameId, int playerId, ArrayList<LocalPlayer> winners) {
        super(gameId, playerId);
        this.winners = winners;
    }

    public ArrayList<LocalPlayer> getWinners() {
        return winners;
    }
}