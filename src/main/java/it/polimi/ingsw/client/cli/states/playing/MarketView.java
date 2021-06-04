package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.MarketPrinter;
import it.polimi.ingsw.client.exceptions.InvalidMarketIndexException;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class MarketView extends GameView {
    private final LocalMarket localMarket;

    public MarketView(CLI cli, LocalGame<?> localGame, LocalMarket localMarket) {
        this.ui = cli;
        this.localMarket = localMarket;
        this.localGame = localGame;
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
    public synchronized void removeObserved() {
        localGame.getError().removeObserver();
        localMarket.removeObservers();
        localGame.getLocalTurn().removeObservers();
        localGame.removeObservers();
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
                    ui.setState(new FlushMarketCombinationView(ui, localGame, new TreeMap<>(localMarket.getResCombinations().get(number - 1))));
                } else {
                    writeErrText();
                }
            } else {
                message = ("It's not your turn!");
            }
        } else writeErrText();
    }
}
