package it.polimi.ingsw.messages.answers.endgameanswer;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.PairId;

import java.util.ArrayList;


public class EndGameAnswer extends Answer {
    /**
     * list of winners: if null it means that Lorenzo is the winner
     */
    private final ArrayList<LocalPlayer> winners;

    /**
     *leaderboard of the players who participated at this game
     */
    private final ArrayList<PairId<LocalPlayer, Integer>> localLeaderboard;

    public EndGameAnswer(int gameId, int playerId, ArrayList<LocalPlayer> winners, ArrayList<PairId<LocalPlayer, Integer>> localLeaderboard) {
        super(gameId, playerId);
        this.winners = winners;
        this.localLeaderboard = localLeaderboard;
    }

    public ArrayList<PairId<LocalPlayer, Integer>> getLocalLeaderboard() {
        return localLeaderboard;
    }

    public ArrayList<LocalPlayer> getWinners() {
        return winners;
    }
}
