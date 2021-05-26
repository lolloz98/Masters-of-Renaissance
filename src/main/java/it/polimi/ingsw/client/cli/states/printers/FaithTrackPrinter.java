package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalFigureState;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;

import java.util.ArrayList;

public class FaithTrackPrinter {

    public static ArrayList<String> toStringBlock(LocalGame localGame, LocalPlayer localPlayer) {
        ArrayList<String> out = new ArrayList<>();
        out.add(" vp:     1           2        4        6        9        12       16       20  ");
        out.add("");
        CLIutils.appendSpaces(out, 1, 2);
        for (int i = 0; i < 25; i++) {
            if (localGame instanceof LocalSingle) {
                if (i == localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore() && i == ((LocalSingle) localGame).getLorenzoTrack().getFaithTrackScore()) {
                    CLIutils.append(out, 1, "LY ");
                } else if (i == localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore()) {
                    CLIutils.append(out, 1, " Y ");
                } else if (i == ((LocalSingle) localGame).getLorenzoTrack().getFaithTrackScore()) {
                    CLIutils.append(out, 1, " L ");
                } else {
                    CLIutils.append(out, 1, " ██");
                }
            } else {
                if (i == localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore()) {
                    CLIutils.append(out, 1, " Y ");
                } else {
                    CLIutils.append(out, 1, " ██");
                }
            }
        }
        out.add("vatican figures: ┗━━━━ ");
        if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[0] == LocalFigureState.ACTIVE) {
            CLIutils.append(out, 2, "A");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[0] == LocalFigureState.DISCARDED) {
            CLIutils.append(out, 2, "D");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[0] == LocalFigureState.INACTIVE) {
            CLIutils.append(out, 2, "I");
        }
        CLIutils.append(out, 2, " ━━━━┛        ┗━━━━━ ");
        if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[1] == LocalFigureState.ACTIVE) {
            CLIutils.append(out, 2, "A");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[1] == LocalFigureState.DISCARDED) {
            CLIutils.append(out, 2, "D");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[1] == LocalFigureState.INACTIVE) {
            CLIutils.append(out, 2, "I");
        }
        CLIutils.append(out, 2, " ━━━━━━┛     ┗━━━━━━━ ");
        if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[2] == LocalFigureState.ACTIVE) {
            CLIutils.append(out, 2, "A");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[2] == LocalFigureState.DISCARDED) {
            CLIutils.append(out, 2, "D");
        } else if (localPlayer.getLocalBoard().getLocalTrack().getFiguresState()[2] == LocalFigureState.INACTIVE) {
            CLIutils.append(out, 2, "I");
        }
        CLIutils.append(out, 1, "  ");
        CLIutils.append(out, 2, " ━━━━━━━┛ ");
        return out;
    }
}
