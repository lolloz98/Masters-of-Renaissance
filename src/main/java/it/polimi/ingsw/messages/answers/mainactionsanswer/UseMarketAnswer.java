package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class UseMarketAnswer extends Answer {
    private final ArrayList<TreeMap<Resource, Integer>> resCombinations;


    /**
     * @param gameId          current game id
     * @param playerId        id of the player who sent the request
     * @param resCombinations combinations of resources that the player can choose
     */
    public UseMarketAnswer(int gameId, int playerId, ArrayList<TreeMap<Resource, Integer>> resCombinations) {
        super(gameId, playerId);
        this.resCombinations = resCombinations;
    }

    public ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return resCombinations;
    }
}
