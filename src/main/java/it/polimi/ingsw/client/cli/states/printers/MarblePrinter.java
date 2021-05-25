package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;

public class MarblePrinter {

    public static ArrayList<String> toStringBlock(Resource res) {
        ArrayList<String> out = new ArrayList<>();
        out.add(" ");
        out.add("");
        out.add(" ");
        CLIutils.append(out, 0, CLIutils.resourceToAnsiMarble(res));
        CLIutils.append(out, 1, CLIutils.resourceToAnsiMarble(res));
        CLIutils.append(out, 2, CLIutils.resourceToAnsiMarble(res));
        CLIutils.append(out, 0, "▄▄");
        CLIutils.append(out, 1, "████");
        CLIutils.append(out, 2, "▀▀");
        CLIutils.append(out, 0, CLIutils.BLACK_BACKGROUND);
        CLIutils.append(out, 1, CLIutils.BLACK_BACKGROUND);
        CLIutils.append(out, 2, CLIutils.BLACK_BACKGROUND);
        CLIutils.append(out, 0, CLIutils.ANSI_WHITE);
        CLIutils.append(out, 1, CLIutils.ANSI_WHITE);
        CLIutils.append(out, 2, CLIutils.ANSI_WHITE);
        CLIutils.append(out, 0, " ");
        CLIutils.append(out, 2, " ");
        return out;
    }
}
