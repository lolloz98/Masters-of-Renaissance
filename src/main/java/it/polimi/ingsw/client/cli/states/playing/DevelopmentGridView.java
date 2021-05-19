package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.DevelopmentGridPrinter;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class DevelopmentGridView extends GameView {
    private final LocalDevelopmentGrid localDevelopmentGrid;

    public DevelopmentGridView(CLI cli, LocalGame<?> localGame, LocalDevelopmentGrid localDevelopmentGrid) {
        this.ui = cli;
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localGame = localGame;
        waiting = false;
        localGame.addObserver(this);
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
    }

    @Override
    public void draw() {
        if (waiting)
            System.out.println("Please wait");
        else {
            CLIutils.clearScreen();
           CLIutils.printBlock(DevelopmentGridPrinter.toStringBlock(localDevelopmentGrid));
            super.drawTurn();
        }
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localDevelopmentGrid.removeObserver();
        localGame.getLocalTurn().removeObserver();
    }

    @Override
    public void helpScreen() {
        super.helpScreen();
        System.out.println("'buy', followed by a card coordinate, followed by a number that indicates in which slot in the board to put it, to buy a development card (for example: 'buy a1 2')");
    }

    @Override
    public void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if (ansList.size() > 3) {
                writeErrText();
            } else {
                if ("BUY".equals(ansList.get(0))) {
                    buy(ansList.get(1), ansList.get(2));
                } else {
                    super.handleCommand(ansList);
                }
            }
        }
    }

    private void buy(String whatToBuy, String whereToPut) {
        if(localGame.isMainPlayerTurn()) {
            try {
                int slotNumber = Integer.parseInt(whereToPut) - 1;
                Color color = null;
                int colorInt = -1;
                int level;
                if (whatToBuy.length() == 2 && slotNumber >= 0 && slotNumber <= 2) {
                    char colorChar = whatToBuy.charAt(0);
                    switch (colorChar) {
                        case 'A':
                            color = Color.PURPLE;
                            colorInt = 0;
                            break;
                        case 'B':
                            color = Color.GREEN;
                            colorInt = 1;
                            break;
                        case 'C':
                            color = Color.GOLD;
                            colorInt = 2;
                            break;
                        case 'D':
                            color = Color.BLUE;
                            colorInt = 3;
                            break;
                    }
                    level = Character.getNumericValue(whatToBuy.charAt(1));
                    if (color != null && level > 0 && level < 4) {
                        if (localDevelopmentGrid.getDevelopCardsNumber()[colorInt][level - 1] > 0) {
                            TreeMap<Resource, Integer> cost = localDevelopmentGrid.getTopDevelopCards()[colorInt][level - 1].getCost();
                            ui.setState(new BuyDevelopmentCardView(ui, localGame, color, level, slotNumber, cost));
                        } else {
                            System.out.println("There are no cards in this deck!");
                        }
                    } else {
                        writeErrText();
                    }
                } else {
                    writeErrText();
                }
            } catch (NumberFormatException e) {
                writeErrText();
            }
        } else {
            System.out.println("It's not your turn!");
        }
    }

}
