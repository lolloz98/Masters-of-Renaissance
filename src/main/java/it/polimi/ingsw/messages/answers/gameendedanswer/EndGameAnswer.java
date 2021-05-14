package it.polimi.ingsw.messages.answers.gameendedanswer;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;

import java.util.ArrayList;

//todo
public class EndGameAnswer extends Answer {
    private final ArrayList<LocalPlayer> winner;

    public EndGameAnswer(int gameId, int playerId, ArrayList<LocalPlayer> winner) {
        super(gameId, playerId);
        this.winner = winner;
    }
}
