package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class MarketView extends GameView {
    private LocalMarket localMarket;

    public MarketView(CLI cli, LocalGame<?> localGame, LocalMarket localMarket) {
        this.cli = cli;
        this.localMarket = localMarket;
        this.localGame = localGame;
        waiting = false;
        localMarket.addObserver(this);
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
    }

    @Override
    public synchronized void draw() {
        if (waiting)
            System.out.println("Please wait");
        else {
            CLI.clearScreen();
            System.out.println("Market:");
            System.out.println("");
            System.out.println("Free marble: " + localMarket.getFreeMarble());
            System.out.println("");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print(localMarket.getMarbleMatrix()[i][j] + " ");
                }
                System.out.print("\r\n");
            }
            if (localMarket.getResCombinations().size() > 0) {
                System.out.println("Resources combinations:");
                int i = 0;
                for (TreeMap<Resource, Integer> t : localMarket.getResCombinations()) {
                    i++;
                    System.out.println(i + ") " + t);
                }
            }
            System.out.println("");
            super.drawTurn();
        }
    }

    @Override
    public synchronized void removeObserved() {
        localGame.getError().removeObserver();
        localMarket.removeObserver();
        localGame.getLocalTurn().removeObserver();
    }

    @Override
    public synchronized void notifyUpdate() {
        draw();
        waiting = false;
    }

    @Override
    public synchronized void helpScreen() {
        // todo
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localGame.getError().getErrorMessage());
        waiting = false;
    }

    @Override
    public synchronized void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if (ansList.size() > 2) {
                writeErrText();
            } else {
                switch (ansList.get(0)) {
                    case "PUSH":
                        push(ansList.get(1));
                        break;
                    case "FLUSH":
                        flush(ansList.get(1));
                        break;
                    default:
                        super.handleCommand(ansList);
                }
            }
        }
    }

    private void push(String s) {
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
                cli.getServerListener().sendMessage(new UseMarketMessage(
                        localGame.getGameId(),
                        localGame.getMainPlayer().getId(),
                        onRow,
                        index
                ));
                waiting = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void flush(String s) {
        int number = -1;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            writeErrText();
        }
        if (number >= 0 && number < localMarket.getResCombinations().size() + 1) {
            removeObserved();
            cli.setState(new FlushMarketCombinationView(cli, localGame, localMarket.getResCombinations().get(number - 1)));
        } else {
            writeErrText();
        }
    }
}
