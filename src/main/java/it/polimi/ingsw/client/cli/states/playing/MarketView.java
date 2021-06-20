package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.MarketPrinter;
import it.polimi.ingsw.client.exceptions.InvalidMarketIndexException;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * CLI state to show and interact with the market
 */
public class MarketView extends GameView {
    private final LocalMarket localMarket;

    public MarketView(CLI cli, LocalMarket localMarket) {
        this.ui = cli;
        this.localMarket = localMarket;
        this.localGame = cli.getLocalGame();
        waiting = false;
        localMarket.overrideObserver(this);
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
    }

    @Override
    public synchronized void draw() {
        CLIutils.clearScreen();
        if (waiting)
            message = ("Please wait");
        CLIutils.printBlock(MarketPrinter.toStringBlock(localGame, localMarket));
        super.drawTurn();
    }

    @Override
    public synchronized void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            switch (ansList.get(0)) {
                case "PM": // PUSH MARBLE
                    push(ansList);
                    break;
                case "FM": // FLUSH MARKET
                    flush(ansList);
                    break;
                default:
                    super.handleCommand(ansList);
            }
        }
    }

    /**
     * handling of "push marble" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates which one of the marbles must be pushed,
     *                can be a character from A to C or a number from 1 to 4
     */
    private void push(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            try {
                UseMarketMessage useMarketMessage = InputHelper.getUseMarketMessage(localGame, ansList.get(1));
                waiting = true;
                ui.getGameHandler().dealWithMessage(useMarketMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidMarketIndexException e) {
                writeErrText();
            }
        }
    }

    /**
     * handling of "flush resource combination" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates which one of the combinations must be flushed
     */
    private void flush(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localGame.isMainPlayerTurn()) {
                int number = -1;
                try {
                    number = Integer.parseInt(ans1);
                } catch (NumberFormatException e) {
                    writeErrText();
                }
                if (number > 0 && number < localMarket.getResCombinations().size() + 1) {
                    removeObserved();
                    ui.setState(new FlushMarketCombinationView(ui, new TreeMap<>(localMarket.getResCombinations().get(number - 1))));
                } else {
                    writeErrText();
                }
            } else {
                message = ("It's not your turn!");
            }
        } else writeErrText();
    }
}
