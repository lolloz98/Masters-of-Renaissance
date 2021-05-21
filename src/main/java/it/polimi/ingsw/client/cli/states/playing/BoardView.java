package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.BoardPrinter;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardView extends GameView {
    /**
     * the player to which belongs the board i'm showing
     */
    private final LocalPlayer localPlayer;

    public BoardView(CLI cli, LocalGame<?> localGame, LocalPlayer localPlayer) {
        this.localGame = localGame;
        this.ui = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
        localGame.addObserver(this);
        waiting = false;
    }

    @Override
    public synchronized void draw() {
        if (waiting) {
            System.out.println("Please wait");
        } else {
            CLIutils.clearScreen();
            CLIutils.printBlock(BoardPrinter.toStringBlock(localGame, localPlayer));
            System.out.println("");
            super.drawTurn();
        }
        System.out.println("");
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localPlayer.getLocalBoard().removeObserver();
        localGame.getLocalTurn().removeObserver();
        localGame.removeObserver();
    }

    @Override
    public synchronized void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if (localPlayer == localGame.getMainPlayer()) {
                switch (ansList.get(0)) { // todo check command lenght in every method
                    case "AL": // activate leader
                        activateLeader(ansList);
                        break;
                    case "DL": // discard leader
                        discardLeader(ansList);
                        break;
                    case "AD": // activate development
                        activateProduction(ansList);
                        break;
                    case "FP": // flush development
                        flushProduction();
                        break;
                    default:
                        super.handleCommand(ansList);
                }
            } else {
                super.handleCommand(ansList);

            }
        }
    }

    private void discardLeader(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localPlayer == localGame.getMainPlayer()) {
                int leaderNumber = 0;
                try {
                    leaderNumber = Integer.parseInt(ans1);
                } catch (NumberFormatException e) {
                    writeErrText();
                }
                if (leaderNumber > 0 && leaderNumber <= localPlayer.getLocalBoard().getLeaderCards().size()) {
                    try {
                        ui.getGameHandler().dealWithMessage(new DiscardLeaderMessage(localGame.getGameId(), localPlayer.getId(), localPlayer.getLocalBoard().getLeaderCards().get(leaderNumber - 1).getId()));
                        waiting = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    writeErrText();
                }
            } else {
                System.out.println("You can only do this on your board!");
            }
        } else writeErrText();
    }

    private void flushProduction() {
        if (localGame.isMainPlayerTurn()) {
            try {
                ui.getGameHandler().dealWithMessage(new FlushProductionResMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You can only do this on your board!");
        }
    }

    private void activateProduction(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localGame.isMainPlayerTurn()) {
                if (localPlayer == localGame.getMainPlayer()) {
                    int number = -1;
                    try {
                        number = Integer.parseInt(ans1);
                        if (number >= 0 && number < 6) {
                            if (number == 0) {
                                removeObserved();
                                ui.setState(new ActivateProductionView(ui, localGame, 0));
                            } else if (number > 0 && number < 4) {
                                if (localPlayer.getLocalBoard().getDevelopCards().get(number - 1).size() == 0) {
                                    System.out.println("There are no cards in this slot!");// there are no develop cards in this slot
                                } else {
                                    removeObserved();
                                    ui.setState(new ActivateProductionView(ui, localGame, number));
                                }
                            } else { // activating a leader production
                                if (!((LocalLeaderCard) localPlayer.getLocalBoard().getLeaderCards().get(number - 4)).isDiscarded() && ((LocalLeaderCard) localPlayer.getLocalBoard().getLeaderCards().get(number - 4)).isActive()) {
                                    if ((number - 4) < localPlayer.getLocalBoard().getLeaderCards().size() || !(localPlayer.getLocalBoard().getLeaderCards().get(number - 4) instanceof LocalProductionLeader)) {
                                        writeErrText();
                                    } else {
                                        removeObserved();
                                        ui.setState(new ActivateProductionView(ui, localGame, number));
                                    }
                                } else {
                                    System.out.println("This leader card is not active");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        writeErrText();
                    }
                } else {
                    System.out.println("You can only do this on your board!");
                }
            } else {
                System.out.println("It's not your turn!");
            }
        } else writeErrText();
    }

    private void activateLeader(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localPlayer == localGame.getMainPlayer()) {
                int number = 0;
                try {
                    number = Integer.parseInt(ans1);
                } catch (NumberFormatException e) {
                    writeErrText();
                }
                if (number > 0 && number <= localPlayer.getLocalBoard().getLeaderCards().size()) {
                    try {
                        ui.getGameHandler().dealWithMessage(new ActivateLeaderMessage(localGame.getGameId(), localPlayer.getId(), localPlayer.getLocalBoard().getLeaderCards().get(number - 1).getId()));
                        waiting = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("You can only do this on your board!");
            }
        } else writeErrText();
    }

}
