package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Printer class for a development (just a string, for example "2 } 1")
 */
public class DevelopmentPrinter {

    public static String toStringBlock(LocalProduction localProduction) {
        ArrayList<String> out = new ArrayList<>();
        out.add("");
        TreeMap<Resource, Integer> toGain = new TreeMap<>(localProduction.getResToGain());
        TreeMap<Resource, Integer> toGive = new TreeMap<>(localProduction.getResToGive());
        int size = toGive.size();
        CLIutils.appendSpaces(out, 0, -2 * size + 6);
        CLIutils.append(out, 0, CLIutils.ANSI_BLACK);
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = toGive.pollFirstEntry();
            CLIutils.append(out, 0, CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.append(out, 0, CLIutils.ANSI_WHITE);
        CLIutils.append(out, 0, "} ");
        size = toGain.size();
        CLIutils.append(out, 0, CLIutils.ANSI_BLACK);
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = toGain.pollFirstEntry();
            CLIutils.append(out, 0, CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.appendSpaces(out, 0, -2 * size + 6);
        CLIutils.append(out, 0, CLIutils.ANSI_WHITE);
        return out.get(0);
    }

    public static String toStringBlock(TreeMap<Resource, Integer> toFlushCopy) {
        ArrayList<String> out = new ArrayList<>();
        TreeMap<Resource, Integer> toFlush = new TreeMap<>(toFlushCopy);
        out.add("");
        int size = toFlush.size();
        CLIutils.append(out, 0, CLIutils.ANSI_BLACK);
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = toFlush.pollFirstEntry();
            CLIutils.append(out, 0, CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.append(out, 0, CLIutils.ANSI_WHITE);
        CLIutils.appendSpaces(out, 0, -2 * size + 6);
        CLIutils.append(out, 0, CLIutils.ANSI_WHITE);
        return out.get(0);
    }


}
