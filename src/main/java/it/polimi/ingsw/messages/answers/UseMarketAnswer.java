package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class UseMarketAnswer extends Answer{
    private static final long serialVersionUID = 53L;

    /**
     * All possible combinations of resources that the player can gain after using the market
     */
    private final ArrayList<TreeMap<Resource, Integer>> combinations;

    public ArrayList<TreeMap<Resource, Integer>> getCombinations() {
        return combinations;
    }

    public UseMarketAnswer(int gameId, int playerId, ArrayList<TreeMap<Resource, Integer>> combinations) {
        super(gameId, playerId);
        this.combinations = combinations;
    }
}
