package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.printers.BoardPrinter;
import it.polimi.ingsw.client.exceptions.InvalidLeaderPositionException;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CLI state to show and interact with the board
 */
public class BoardView extends GameView {
    /**
     * the player to which belongs the board i'm showing
     */
    private final LocalPlayer localPlayer;

    public BoardView(CLI cli, LocalPlayer localPlayer) {
        this.localGame = cli.getLocalGame();
        this.ui = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().overrideObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
        waiting = false;
    }

    /**
     * alternative constructor to be able to set the waiting boolean
     */
    public BoardView(CLI cli, LocalPlayer localPlayer, boolean waiting) {
        this.localGame = cli.getLocalGame();
        this.ui = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().overrideObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
        this.waiting = waiting;
    }

    @Override
    public synchronized void draw() {
        CLIutils.clearScreen();
        if (waiting)
            message = ("Please wait");
        CLIutils.printBlock(BoardPrinter.toStringBlock(localGame, localPlayer));
        System.out.println(" ");
        super.drawTurn();
    }

    @Override
    public synchronized void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
            if (localPlayer == localGame.getMainPlayer()) {
                switch (ansList.get(0)) {
                    case "AL": // activate leader
                        activateLeader(ansList);
                        break;
                    case "DL": // discard leader
                        discardLeader(ansList);
                        break;
                    case "AD": // activate development
                        activateProduction(ansList);
                        break;
                    case "ALD": // activate leader development
                        activateLeaderProduction(ansList);
                        break;
                    case "FD": // flush development
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

    /**
     * handling of "discard leader" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates
     *                which one of the leaders must be discarded
     */
    private void discardLeader(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localPlayer == localGame.getMainPlayer()) {
                try {
                    DiscardLeaderMessage discardLeaderMessage = InputHelper.getDiscardLeaderMessage(localGame, ans1);
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(discardLeaderMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidLeaderPositionException e) {
                    writeErrText();
                }
            }
        } else {
            message = ("You can only do this on your board!");
        }
    }

    /**
     * handling of "flush all productions" command
     */
    private void flushProduction() {
        if (localGame.isMainPlayerTurn()) {
            try {
                ui.getGameHandler().dealWithMessage(new FlushProductionResMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message = ("You can only do this on your board!");
        }
    }

    /**
     * handling of "activate leader production" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates which one of the leaders productions must be activated
     */
    private void activateLeaderProduction(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            if (localGame.isMainPlayerTurn()) {
                if (localPlayer == localGame.getMainPlayer()) {
                    String ans1 = ansList.get(1);
                    try {
                        int number = Integer.parseInt(ans1);
                        if (number > 0 && number < 3) {
                            LocalLeaderCard leaderCard = (LocalLeaderCard) localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(number - 1);
                            if (leaderCard instanceof LocalProductionLeader &&
                                    leaderCard.isActive() &&
                                    !leaderCard.isDiscarded()) {
                                LocalProductionLeader localProductionLeader = ((LocalProductionLeader) localPlayer.getLocalBoard().getLeaderCards().get(number - 1));
                                if (localProductionLeader.getProduction().getResToFlush().size() != 0) {
                                    message = ("You already used this development leader card in this turn!");
                                } else {
                                    removeObserved();
                                    ui.setState(new ActivateProductionView(
                                            ui,
                                            localProductionLeader.getWhichProd(),
                                            localProductionLeader.getProduction()
                                    ));
                                }
                            } else {
                                message = ("You can only do this with and active development leader card!");
                            }
                        } else writeErrText();
                    } catch (NumberFormatException e) {
                        writeErrText();
                    }
                } else {
                    message = ("You can only do this on your board!");
                }
            } else {
                message = ("It's not your turn!");
            }
        } else writeErrText();
    }

    /**
     * handling of "activate production" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates which one of the productions must be activated.
     */
    private void activateProduction(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localGame.isMainPlayerTurn()) {
                if (localPlayer == localGame.getMainPlayer()) {
                    int number;
                    try {
                        number = Integer.parseInt(ans1);
                        if (number >= 0 && number < 4) {
                            if (number == 0) {
                                if (localPlayer.getLocalBoard().getBaseProduction().getResToFlush().size() != 0) {
                                    message = ("You already used the base development in this turn!");
                                } else {
                                    removeObserved();
                                    ui.setState(new ActivateProductionView(ui, 0, localPlayer.getLocalBoard().getBaseProduction()));
                                }
                            } else {
                                if (localPlayer.getLocalBoard().getDevelopCards().get(number - 1).size() == 0) {
                                    message = ("There are no cards in this slot!");// there are no develop cards in this slot
                                } else {
                                    if (localPlayer.getLocalBoard().getDevelopCards().get(number - 1).get(localPlayer.getLocalBoard().getDevelopCards().get(number - 1).size() - 1).getProduction().getResToFlush().size() != 0) {
                                        message = ("You already used this development card in this turn!");
                                    } else {
                                        removeObserved();
                                        ui.setState(new ActivateProductionView(
                                                ui,
                                                number,
                                                localPlayer.getLocalBoard().getDevelopCards().get(number - 1).get(localPlayer.getLocalBoard().getDevelopCards().get(number - 1).size() - 1).getProduction()
                                        ));
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        writeErrText();
                    }
                } else {
                    message = ("You can only do this on your board!");
                }
            } else {
                message = ("It's not your turn!");
            }
        } else writeErrText();
    }

    /**
     * handling of "activate leader production" command
     *
     * @param ansList string of commands, parsed to a list.
     *                The second parameter in this list indicates which one of the leaders must be activated
     */
    private void activateLeader(ArrayList<String> ansList) {
        if (ansList.size() == 2) {
            String ans1 = ansList.get(1);
            if (localPlayer == localGame.getMainPlayer()) {
                try {
                    ActivateLeaderMessage activateLeaderMessage = InputHelper.getActivateLeaderMessage(localGame, ans1);
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(activateLeaderMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidLeaderPositionException e) {
                    writeErrText();
                }
            }
        } else {
            message = ("You can only do this on your board!");
        }
    }
}
