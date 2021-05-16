package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class ApplyProductionAnswer extends Answer {
    private final TreeMap<Resource, Integer> resToFlush;
    private final TreeMap<Resource, Integer> resInNormalDepots;
    private final ArrayList<LocalDepotLeader> leaderDepots;
    private final int whichProdSlot;

    public ApplyProductionAnswer(int gameId, int playerId, TreeMap<Resource, Integer> resToFlush, TreeMap<Resource, Integer> resInNormalDepot, ArrayList<LocalDepotLeader> leaderDepots, int whichProdSlot) {
        super(gameId, playerId);
        this.resToFlush = resToFlush;
        this.resInNormalDepots = resInNormalDepot;
        this.leaderDepots = leaderDepots;
        this.whichProdSlot = whichProdSlot;
    }

    public TreeMap<Resource, Integer> getResToFlush() {
        return resToFlush;
    }

    public TreeMap<Resource, Integer> getResInNormalDepots() {
        return resInNormalDepots;
    }

    public ArrayList<LocalDepotLeader> getLeaderDepots() {
        return leaderDepots;
    }

    public int getWhichProdSlot() {
        return whichProdSlot;
    }
}
