package it.polimi.ingsw.client.gui.controllergui;

import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.TreeMap;

public class NormalProductionCard extends LocalDevelopCard {
    static NormalProductionCard INSTANCE;

    public static NormalProductionCard getINSTANCE() {
        if (INSTANCE != null) return INSTANCE;
        return INSTANCE = new NormalProductionCard(100,false, null, 0, Color.GOLD, 0, new TreeMap<>() {{
            put(Resource.ANYTHING, 2);
        }}, new TreeMap<>() {{
            put(Resource.ANYTHING, 1);
        }}, null);
    }

    private NormalProductionCard(int id,boolean isDiscounted, TreeMap<Resource, Integer> cost, int level, Color color, int victoryPoints, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, TreeMap<Resource, Integer> resToFlush) {
        super(id,isDiscounted, cost, level, color, victoryPoints, resToGive, resToGain, resToFlush);
    }
}
