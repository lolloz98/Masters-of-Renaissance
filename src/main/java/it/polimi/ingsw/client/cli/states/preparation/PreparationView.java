package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.printers.BoardPrinter;
import it.polimi.ingsw.client.cli.states.printers.DevelopmentGridPrinter;
import it.polimi.ingsw.client.cli.states.printers.MarketPrinter;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.util.ArrayList;

public abstract class PreparationView extends View<CLI> {
    protected boolean waiting;
    protected LocalGame<?> localGame;

    public void handleCommand(ArrayList<String> ansList) {
        if (ansList.size() > 1) {
            writeErrText();
        } else {
            switch (ansList.get(0)) {
                case "SB":
                    CLIutils.clearScreen();
                    CLIutils.printBlock(BoardPrinter.toStringBlock(localGame, localGame.getMainPlayer()));
                    break;
                case "SM":
                    CLIutils.clearScreen();
                    CLIutils.printBlock(MarketPrinter.toStringBlock(localGame.getLocalMarket()));
                    break;
                case "SD":
                    CLIutils.clearScreen();
                    CLIutils.printBlock(DevelopmentGridPrinter.toStringBlock(localGame.getLocalDevelopmentGrid()));
                    break;
                default:
                    writeErrText();
            }
            System.out.println("Type sb to show board, sm for market, sd for development decks, or type dl followed by a number to discard a leader card.");
        }
    }

    protected void writeErrText() {
        System.out.println("Invalid choice. Type sb to show board, sm for market, sd for development decks, or type dl followed by a number to discard a leader card.");
    }
}
