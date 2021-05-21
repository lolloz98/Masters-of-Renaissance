package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.MarketPrinter;
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
        localMarket.addObserver(this);
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
        localGame.addObserver(this);
    }

    @Override
    public synchronized void draw() {
        if (waiting)
            System.out.println("Please wait");
        else {
            CLIutils.clearScreen();
            CLIutils.printBlock(MarketPrinter.toStringBlock(localMarket));
            super.drawTurn();
        }
    }

    @Override
    public synchronized void removeObserved() {
        localGame.getError().removeObserver();
        localMarket.removeObserver();
        localGame.getLocalTurn().removeObserver();
        localGame.removeObserver();
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
            String s = ansList.get(1);
            boolean onRow = false;
            int index = -1;
            switch (s) {
                case "A":
                    index = 0;
                    onRow = true;
                    break;
                case "B":
                    index = 1;
                    onRow = true;
                    break;
                case "C":
                    index = 2;
                    onRow = true;
                    break;
                case "1":
                    index = 0;
                    onRow = false;
                    break;
                case "2":
                    index = 1;
                    onRow = false;
                    break;
                case "3":
                    index = 2;
                    onRow = false;
                    break;
                case "4":
                    index = 3;
                    onRow = false;
                    break;
                default:
                    writeErrText();
            }
            if (index != -1) {
                try {
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(new UseMarketMessage(
                            localGame.getGameId(),
                            localGame.getMainPlayer().getId(),
                            onRow,
                            index
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else writeErrText();
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
                if (number >= 0 && number < localMarket.getResCombinations().size() + 1) {
                    removeObserved();
                    ui.setState(new FlushMarketCombinationView(ui, localGame, new TreeMap<>(localMarket.getResCombinations().get(number - 1))));
                } else {
                    writeErrText();
                }
            } else {
                System.out.println("It's not your turn!");
            }
        } else writeErrText();
    }
}
