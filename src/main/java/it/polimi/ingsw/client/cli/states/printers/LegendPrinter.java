package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;

public class LegendPrinter {

    public static ArrayList<String> toStringBlock() {
        ArrayList<String> out = new ArrayList<>();
        out.add("legend:");
        out.add("resources colors:");
        out.add(CLIutils.resourceToAnsi(Resource.GOLD)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": gold " +
                CLIutils.resourceToAnsi(Resource.SERVANT)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": servant  " +
                CLIutils.resourceToAnsi(Resource.SHIELD)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": shield");
        out.add(CLIutils.resourceToAnsi(Resource.ROCK)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": rock " +
                CLIutils.resourceToAnsi(Resource.ANYTHING)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": anything " +
                CLIutils.resourceToAnsi(Resource.FAITH)+" "+ CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + ": faith");
        out.add("production colors:");
        out.add(CLIutils.colorToAnsi(Color.BLUE)+"B"+ CLIutils.ANSI_WHITE + ": blue " +
                CLIutils.colorToAnsi(Color.PURPLE)+"P"+ CLIutils.ANSI_WHITE + ": purple ");
        out.add(CLIutils.colorToAnsi(Color.GOLD)+"G"+ CLIutils.ANSI_WHITE + ": gold " +
                CLIutils.colorToAnsi(Color.GREEN)+"G"+ CLIutils.ANSI_WHITE + ": green ");
        return out;
    }
}
