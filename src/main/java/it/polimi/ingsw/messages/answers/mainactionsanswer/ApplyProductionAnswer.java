package it.polimi.ingsw.messages.answers.mainactionsanswer;

import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class ApplyProductionAnswer extends Answer {
    private static final long serialVersionUID = 33L;

    private final TreeMap<Resource, Integer> resToGain;
    /**
     * all kind of warehouse could be modified after a production application
     */
    private final TreeMap<Resource, Integer> resInNormalDepots;
    private final TreeMap<Resource, Integer> resInStrongBox;
    private final ArrayList<LocalDepotLeader> leaderDepots;
    /**
     * info about which production we want to activate
     */
    private final int whichProdSlot;

    public ApplyProductionAnswer(int gameId, int playerId, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resInNormalDepot, TreeMap<Resource, Integer> resInStrongBox, ArrayList<LocalDepotLeader> leaderDepots, int whichProdSlot) {
        super(gameId, playerId);
        this.resToGain = resToGain;
        this.resInNormalDepots = resInNormalDepot;
        this.resInStrongBox = resInStrongBox;
        this.leaderDepots = leaderDepots;
        this.whichProdSlot = whichProdSlot;
    }

    public TreeMap<Resource, Integer> getResToFlush() {
        return resToGain;
    }

    public TreeMap<Resource, Integer> getResInNormalDepots() {
        return resInNormalDepots;
    }

    public ArrayList<LocalDepotLeader> getLeaderDepots() {
        return leaderDepots;
    }

    public TreeMap<Resource, Integer> getResInStrongBox() {
        return resInStrongBox;
    }

    public int getWhichProdSlot() {
        return whichProdSlot;
    }
}
