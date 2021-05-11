package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.WarehouseType;

import java.util.TreeMap;

public class ApplyProductionAnswer extends Answer {
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive;

    public ApplyProductionAnswer(int gameId, int playerId,  TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive) {
        super(gameId, playerId);
        this.resToGive = new TreeMap<>(resToGive);
    }

    public TreeMap<WarehouseType, TreeMap<Resource, Integer>> getResToGive() {
        return resToGive;
    }
}
