package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;

public class StrongBoxPrinter {

    public static ArrayList<String> toStringBlock(LocalPlayer localPlayer) {
        ArrayList<String> out = new ArrayList<>();
        out.add("     strongbox:        ");
        out.add(" ");
        out.add(" ");
        out.add(" ");
        out.add(" ");
        out.add(" ");
        out.add(" ");
        CLIutils.append(out, 1, MarblePrinter.toStringBlock(Resource.SHIELD));
        CLIutils.append(out, 2, " : ");
        if (localPlayer.getLocalBoard().getResInStrongBox().containsKey(Resource.SHIELD)) {
            if (localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SHIELD) < 10) {
                CLIutils.append(out, 2, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SHIELD) + "   ");
            } else {
                CLIutils.append(out, 2, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SHIELD) + "  ");
            }
        } else {
            CLIutils.append(out, 2, "0   ");
        }
        CLIutils.append(out, 1, "       ");
        CLIutils.append(out, 3, "       ");

        CLIutils.append(out, 1, MarblePrinter.toStringBlock(Resource.GOLD));
        CLIutils.append(out, 2, " : ");
        if (localPlayer.getLocalBoard().getResInStrongBox().containsKey(Resource.GOLD)) {
            if (localPlayer.getLocalBoard().getResInStrongBox().get(Resource.GOLD) < 10) {
                CLIutils.append(out, 2, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.GOLD) + "   ");
            } else {
                CLIutils.append(out, 2, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.GOLD) + "  ");
            }
        } else {
            CLIutils.append(out, 2, "0   ");
        }
        CLIutils.append(out, 1, "       ");
        CLIutils.append(out, 3, "       ");

        CLIutils.append(out, 4, MarblePrinter.toStringBlock(Resource.SERVANT));
        CLIutils.append(out, 5, " : ");
        if (localPlayer.getLocalBoard().getResInStrongBox().containsKey(Resource.SERVANT)) {
            if (localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SERVANT) < 10) {
                CLIutils.append(out, 5, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SERVANT) + "   ");
            } else {
                CLIutils.append(out, 5, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.SERVANT) + "  ");
            }
        } else {
            CLIutils.append(out, 5, "0   ");
        }
        CLIutils.append(out, 4, "       ");
        CLIutils.append(out, 6, "       ");

        CLIutils.append(out, 4, MarblePrinter.toStringBlock(Resource.ROCK));
        CLIutils.append(out, 5, " : ");
        if (localPlayer.getLocalBoard().getResInStrongBox().containsKey(Resource.ROCK)) {
            if (localPlayer.getLocalBoard().getResInStrongBox().get(Resource.ROCK) < 10) {
                CLIutils.append(out, 5, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.ROCK) + "   ");
            } else {
                CLIutils.append(out, 5, localPlayer.getLocalBoard().getResInStrongBox().get(Resource.ROCK) + "  ");
            }
        } else {
            CLIutils.append(out, 5, "0   ");
        }
        CLIutils.append(out, 4, "       ");
        CLIutils.append(out, 6, "       ");
        for (int i = 0; i < 7; i++) {
            CLIutils.append(out, i, " ");
        }
        return out;
    }
}
