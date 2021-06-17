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

/**
 * CLI state to show and interact with the development grid
 */
public class DevelopmentGridView extends GameView {
    private final LocalDevelopmentGrid localDevelopmentGrid;

    public DevelopmentGridView(CLI cli, LocalDevelopmentGrid localDevelopmentGrid) {
        this.ui = cli;
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localGame = cli.getLocalGame();
        waiting = false;
        localGame.overrideObserver(this);
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localDevelopmentGrid.overrideObserver(this);
    }

    @Override
    public void draw() {
        CLIutils.clearScreen();
        if (waiting)
            message = ("Please wait");
        CLIutils.printBlock(DevelopmentGridPrinter.toStringBlock(localDevelopmentGrid));
        super.drawTurn();
    }

    @Override
    public void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if ("BD".equals(ansList.get(0))) { // buy development card
                buy(ansList);
            } else {
                super.handleCommand(ansList);
            }
        }
    }

    /**
     * handling of "buy development card" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates the coordinate of the card to buy, for example "A1".
     *                The third parameter indicates the slot in the board in which the card will be placed.
     */
    private void buy(ArrayList<String> ansList) {
        if (ansList.size() == 3) {
            if (localGame.isMainPlayerTurn()) {
                try {
                    int slotNumber = Integer.parseInt(ansList.get(2)) - 1;
                    Color color = null;
                    int colorInt = -1;
                    int level;
                    if (ansList.get(1).length() == 2 && slotNumber >= 0 && slotNumber <= 2) {
                        char colorChar = ansList.get(1).charAt(0);
                        switch (colorChar) {
                            case 'A':
                                color = Color.GREEN;
                                colorInt = 0;
                                break;
                            case 'B':
                                color = Color.BLUE;
                                colorInt = 1;
                                break;
                            case 'C':
                                color = Color.GOLD;
                                colorInt = 2;
                                break;
                            case 'D':
                                color = Color.PURPLE;
                                colorInt = 3;
                                break;
                        }
                        level = Character.getNumericValue(ansList.get(1).charAt(1));
                        if (color != null && level > 0 && level < 4) {
                            if (localDevelopmentGrid.getDevelopCardsNumber()[colorInt][level - 1] > 0) {
                                TreeMap<Resource, Integer> cost = localDevelopmentGrid.getTopDevelopCards()[colorInt][level - 1].getCost();
                                ui.setState(new BuyDevelopmentCardView(ui, localGame, color, level, slotNumber, cost));
                            } else {
                                message = ("There are no cards in this deck!");
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
                message = ("It's not your turn!");
            }
        } else writeErrText();
    }
}
