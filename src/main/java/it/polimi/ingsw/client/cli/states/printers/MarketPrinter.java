package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class MarketPrinter {
    public static ArrayList<String> toStringBlock(LocalMarket localMarket) {
        ArrayList<String> out = new ArrayList<>();
        out.add("                ");
        out.add("  free marble : ");
        out.add("                ");
        CLIutils.append(out, 0, MarblePrinter.toStringBlock(localMarket.getFreeMarble()));
        out.add("┏━━━━━━━━━━━━━━━━━━━━━━━━┓                         Legend:");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┗━━━━━━━━━━━━━━━━━━━━━━━━┛");
        out.add("    1     2     3     4");
        out.add(" ");
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                CLIutils.append(out, 4 + 3 * y, " ");
                CLIutils.append(out, 4 + 3 * y + 1, " ");
                CLIutils.append(out, 4 + 3 * y + 2, " ");
                CLIutils.append(out, 4 + 3 * y, MarblePrinter.toStringBlock(localMarket.getMarbleMatrix()[y][x]));
                CLIutils.append(out, 4 + 3 * y, " ");
                CLIutils.append(out, 4 + 3 * y + 1, " ");
                CLIutils.append(out, 4 + 3 * y + 2, " ");
            }
        }
        for (int i = 4; i < 13; i++) {
            CLIutils.append(out, i, "┃");
        }
        CLIutils.append(out, 4, "               ");
        CLIutils.append(out, 5, "  A            ");
        CLIutils.append(out, 6, "               ");
        CLIutils.append(out, 7, "               ");
        CLIutils.append(out, 8, "  B            ");
        CLIutils.append(out, 9, "               ");
        CLIutils.append(out, 10, "               ");
        CLIutils.append(out, 11, "  C            ");
        CLIutils.append(out, 12, "               ");

        CLIutils.append(out, 4, MarblePrinter.toStringBlock(Resource.FAITH));
        CLIutils.append(out, 4, "            ");
        CLIutils.append(out, 5, " : Faith    ");
        CLIutils.append(out, 6, "            ");

        CLIutils.append(out, 4, MarblePrinter.toStringBlock(Resource.GOLD));
        CLIutils.append(out, 4, "            ");
        CLIutils.append(out, 5, " : Gold ");
        CLIutils.append(out, 6, "            ");

        CLIutils.append(out, 7, MarblePrinter.toStringBlock(Resource.NOTHING));
        CLIutils.append(out, 7, "            ");
        CLIutils.append(out, 8, " : Nothing  ");
        CLIutils.append(out, 9, "            ");

        CLIutils.append(out, 7, MarblePrinter.toStringBlock(Resource.ROCK));
        CLIutils.append(out, 7, "            ");
        CLIutils.append(out, 8, " : Rock     ");
        CLIutils.append(out, 9, "            ");

        CLIutils.append(out, 10, MarblePrinter.toStringBlock(Resource.SHIELD));
        CLIutils.append(out, 10, "            ");
        CLIutils.append(out, 11, " : Shield   ");
        CLIutils.append(out, 12, "            ");

        CLIutils.append(out, 10, MarblePrinter.toStringBlock(Resource.SERVANT));
        CLIutils.append(out, 10, "            ");
        CLIutils.append(out, 11, " : Servant   ");
        CLIutils.append(out, 12, "            ");

        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");

        ArrayList<TreeMap<Resource, Integer>> resComb = localMarket.getResCombinations();

        if (resComb.size() == 0) {
            CLIutils.append(out, 16, "Type 'pm' followed by a number or a letter corresponding to a marble to push");
        } else {
            CLIutils.append(out, 16, "Type 'fm' followed by a number to flush the resources to the board");
            CLIutils.append(out, 18, "  ");
            CLIutils.append(out, 19, "1)");
            CLIutils.append(out, 20, "  ");

            TreeMap<Resource, Integer> resTree = new TreeMap<>(resComb.get(0));
            do {
                CLIutils.append(out, 18, " ");
                CLIutils.append(out, 19, " ");
                CLIutils.append(out, 20, " ");
                CLIutils.append(out, 18, MarblePrinter.toStringBlock(resTree.firstKey()));
                MapUtils.removeResFromMap(resTree, resTree.firstKey());
                CLIutils.append(out, 18, " ");
                CLIutils.append(out, 19, " ");
                CLIutils.append(out, 20, " ");
            } while (!resTree.isEmpty());

            if (resComb.size() > 1) {
                resTree = new TreeMap<>(resComb.get(1));
                CLIutils.append(out, 18, "         ");
                CLIutils.append(out, 19, "      2) ");
                CLIutils.append(out, 20, "         ");
                do {
                    CLIutils.append(out, 18, " ");
                    CLIutils.append(out, 19, " ");
                    CLIutils.append(out, 20, " ");
                    CLIutils.append(out, 18, MarblePrinter.toStringBlock(resTree.firstKey()));
                    MapUtils.removeResFromMap(resTree, resTree.firstKey());
                    CLIutils.append(out, 18, " ");
                    CLIutils.append(out, 19, " ");
                    CLIutils.append(out, 20, " ");
                } while (!resTree.isEmpty());
            }
            if (resComb.size() > 2) {
                resTree = new TreeMap<>(resComb.get(2));
                CLIutils.append(out, 18, "         ");
                CLIutils.append(out, 19, "      3) ");
                CLIutils.append(out, 20, "         ");
                do {
                    CLIutils.append(out, 18, " ");
                    CLIutils.append(out, 19, " ");
                    CLIutils.append(out, 20, " ");
                    CLIutils.append(out, 18, MarblePrinter.toStringBlock(resTree.firstKey()));
                    MapUtils.removeResFromMap(resTree, resTree.firstKey());
                    CLIutils.append(out, 18, " ");
                    CLIutils.append(out, 19, " ");
                    CLIutils.append(out, 20, " ");
                } while (!resTree.isEmpty());
            }
            if (resComb.size() > 3) {
                resTree = new TreeMap<>(resComb.get(3));
                CLIutils.append(out, 21, "         ");
                CLIutils.append(out, 22, "      4) ");
                CLIutils.append(out, 23, "         ");
                do {
                    CLIutils.append(out, 21, " ");
                    CLIutils.append(out, 22, " ");
                    CLIutils.append(out, 23, " ");
                    CLIutils.append(out, 21, MarblePrinter.toStringBlock(resTree.firstKey()));
                    MapUtils.removeResFromMap(resTree, resTree.firstKey());
                    CLIutils.append(out, 21, " ");
                    CLIutils.append(out, 22, " ");
                    CLIutils.append(out, 23, " ");
                } while (!resTree.isEmpty());
            }
            if (resComb.size() > 4) {
                resTree = new TreeMap<>(resComb.get(4));
                CLIutils.append(out, 21, "         ");
                CLIutils.append(out, 22, "      5) ");
                CLIutils.append(out, 23, "         ");
                do {
                    CLIutils.append(out, 21, " ");
                    CLIutils.append(out, 22, " ");
                    CLIutils.append(out, 23, " ");
                    CLIutils.append(out, 21, MarblePrinter.toStringBlock(resTree.firstKey()));
                    MapUtils.removeResFromMap(resTree, resTree.firstKey());
                    CLIutils.append(out, 21, " ");
                    CLIutils.append(out, 22, " ");
                    CLIutils.append(out, 23, " ");
                } while (!resTree.isEmpty());
            }
        }
        return out;
    }
}
