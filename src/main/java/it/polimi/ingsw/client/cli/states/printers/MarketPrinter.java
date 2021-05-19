package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class MarketPrinter {
    public static ArrayList<String> toStringBlock(LocalMarket localMarket){
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> letters = new ArrayList<>(){{
            add("A");
            add("B");
            add("C");
        }};
        out.add("Market:");
        out.add("");
        out.add("Free marble: " + localMarket.getFreeMarble());
        out.add("");
        for (int i = 0; i < 3; i++) {
            String line = "";
            for (int j = 0; j < 4; j++) {
                line = line + localMarket.getMarbleMatrix()[i][j] + " ";
            }
            line = line +"  " +letters.get(i);
            out.add(line);
        }
        out.add("    1      2      3      4");
        if (localMarket.getResCombinations().size() > 0) {
            out.add("Resources combinations:");
            int i = 0;
            for (TreeMap<Resource, Integer> t : localMarket.getResCombinations()) {
                i++;
                out.add(i + ") " + t);
            }
        }
        out.add(" ");
        return out;
    }
}
