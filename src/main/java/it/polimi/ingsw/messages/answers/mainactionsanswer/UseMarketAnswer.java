package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class UseMarketAnswer extends Answer {
    /**
     * combination of resources that a player could choose to flush.
     */
    private final ArrayList<TreeMap<Resource, Integer>> resCombinations;
    private final LocalMarket localMarket;

    /**
     * @param gameId          current game id
     * @param playerId        id of the player who sent the request
     * @param resCombinations combinations of resources that the player can choose
     * @param localMarket     market update after being used
     */
    public UseMarketAnswer(int gameId, int playerId, ArrayList<TreeMap<Resource, Integer>> resCombinations, LocalMarket localMarket) {
        super(gameId, playerId);
        this.resCombinations = resCombinations;
        this.localMarket = localMarket;
    }

    public ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return resCombinations;
    }

    public LocalMarket getLocalMarket() {
        return localMarket;
    }
}
