package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.InputHelper;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.playing.*;
import it.polimi.ingsw.client.cli.states.playing.WinnerView;
import it.polimi.ingsw.client.exceptions.LeaderIndexOutOfBoundException;
import it.polimi.ingsw.client.exceptions.LeadersAlreadyPickedException;
import it.polimi.ingsw.client.exceptions.ResourceNumberOutOfBoundException;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.messages.requests.FinishTurnMessage;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CLI state for the playing states
 */
public abstract class GameView extends View<CLI> {
    protected LocalGame<?> localGame;
    /**
     * message string to be shown at the end of the screen, can be an error message or some other information, like "Please wait"
     */
    protected String message = "";

    /**
     * remove all observers from the game
     */
    public void removeObserved() {
        localGame.removeAllObservers();
    }

    @Override
    public synchronized void notifyUpdate() {
        message = "";
        if (localGame.getState() == LocalGameState.OVER) goToWinnerScreen();
        else if (localGame.getState() == LocalGameState.DESTROYED) goToDestroyed();
        else {
            waiting = false;
            draw();
        }
    }

    @Override
    public void notifyError() {
        message = localGame.getError().getErrorMessage();
        System.out.println(localGame.getError().getErrorMessage());
        waiting = false;
        draw();
    }

    @Override
    public void handleCommand(String s) {
        String ans = s.toUpperCase();
        ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
        handleCommand(ansList);
    }

    /**
     * method that gets called from the implementations of this class.
     * from the string of commands calls the correct function.
     *
     * @param ansList the parsed string of commands, converted in a list of strings
     */
    public void handleCommand(ArrayList<String> ansList) {
        int ansNumber;
        try {
            ansNumber = Integer.parseInt(ansList.get(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            ansNumber = 0;
        }
        switch (ansList.get(0)) {
            case "SB": // show board
                moveToBoard(ansNumber);
                break;
            case "SH": // show history
                historyScreen();
                break;
            case "SM": // show market
                moveToMarket(ansNumber);
                break;
            case "SD": // show development decks
                moveToDevelop(ansNumber);
                break;
            case "NT": // next turn
                next();
                break;
            case "PL":
                pickLeaders(ansList);
                break;
            case "PR":
                pickResources(ansList);
                break;
            case "HELP": // show help screen
                helpScreen();
                break;
            case "QUIT":
                quit();
                break;
            default:
                writeErrText();
        }
    }

    /**
     * Handles the choice of initial resources.
     *
     * @param ansList the parsed string of commands, converted in a list of strings
     */
    protected void pickResources(ArrayList<String> ansList) {
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if (ansList.size() == 3 && localMulti.getMainPlayerPosition() == 3) {
                try {
                    ChooseOneResPrepMessage chooseOneResPrepMessage1 = InputHelper.getChooseOneResPrepMessage(localGame, ansList.get(1));
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(chooseOneResPrepMessage1);
                    ChooseOneResPrepMessage chooseOneResPrepMessage2 = InputHelper.getChooseOneResPrepMessage(localGame, ansList.get(2));
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(chooseOneResPrepMessage2);
                } catch (ResourceNumberOutOfBoundException | NumberFormatException e) {
                    writeErrText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (ansList.size() == 2 && (localMulti.getMainPlayerPosition() == 1 || localMulti.getMainPlayerPosition() == 2)) {
                try {
                    ChooseOneResPrepMessage chooseOneResPrepMessage = InputHelper.getChooseOneResPrepMessage(localGame, ansList.get(1));
                    waiting = true;
                    ui.getGameHandler().dealWithMessage(chooseOneResPrepMessage);
                } catch (ResourceNumberOutOfBoundException | NumberFormatException e) {
                    writeErrText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                writeErrText();
        } else writeErrText();
    }

    /**
     * Handles the choice of leader cards.
     *
     * @param ansList the parsed string of commands, converted in a list of strings
     */
    protected void pickLeaders(ArrayList<String> ansList) {
        if (ansList.size() == 3) {
            try {
                RemoveLeaderPrepMessage removeLeaderPrepMessage = InputHelper.getRemoveLeaderPrepMessage(localGame, ansList.get(1), ansList.get(2));
                waiting = true;
                ui.getGameHandler().dealWithMessage(removeLeaderPrepMessage);
            } catch (LeaderIndexOutOfBoundException | LeadersAlreadyPickedException | NumberFormatException e) {
                writeErrText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else writeErrText();
    }

    /**
     * Sends the "next turn" message to the server
     */
    private void next() {
        try {
            ui.getGameHandler().dealWithMessage(new FinishTurnMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches view to development grid
     */
    private void moveToDevelop(int ansNumber) {
        if (ansNumber == 0) {
            removeObserved();
            ui.setState(new DevelopmentGridView(ui, localGame.getLocalDevelopmentGrid()));
        } else writeErrText();
    }

    /**
     * Switches view to market
     */
    private void moveToMarket(int ansNumber) {
        if (ansNumber == 0) {
            removeObserved();
            ui.setState(new MarketView(ui, localGame.getLocalMarket()));
        } else writeErrText();
    }

    /**
     * Switches view to one of the boards
     *
     * @param ansNumber number of the player corresponding to the board to show, in playing order. If the number is 0 it shows the main player's board
     */
    private void moveToBoard(int ansNumber) {
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if (ansNumber < 0 || ansNumber > localMulti.getLocalPlayers().size()) {
                writeErrText();
            } else {
                if (ansNumber == 0) {
                    // go to main board
                    removeObserved();
                    ui.setState(new BoardView(ui, localMulti.getMainPlayer()));
                } else {
                    // go to ans-1 board
                    removeObserved();
                    ui.setState(new BoardView(ui, localMulti.getLocalPlayers().get(ansNumber - 1)));
                }
            }
        }
        if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            if (ansNumber == 0) {
                // go to main board
                removeObserved();
                ui.setState(new BoardView(ui, localSingle.getMainPlayer()));
            } else {
                writeErrText();
            }
        }
    }

    /**
     * Prints at the end of the screen information about the playing order, the current player, the messages about
     * the initial resources and leader card choice, and prints error messages if present
     */
    public void drawTurn() {
        if (localGame.getState() != LocalGameState.DESTROYED && localGame.getState() != LocalGameState.OVER) {
            if (localGame instanceof LocalMulti) {
                LocalMulti localMulti = (LocalMulti) localGame;
                System.out.print("Playing order:");
                ArrayList<LocalPlayer> localPlayers = localMulti.getLocalPlayers();
                for (int i = 0; i < localPlayers.size(); i++) {
                    System.out.print(" " + (i + 1) + "," + localPlayers.get(i).getName());
                }
                System.out.print("\n");
                if (localMulti.getState() == LocalGameState.PREP_LEADERS) {
                    if (localMulti.getMainPlayer().getLocalBoard().getLeaderCards().size() == 2) {
                        System.out.println("Please wait");
                    } else
                        System.out.println("Pick two leader cards: type pl followed by two numbers, corresponding to the leader cards to keep");
                } else if (localMulti.getState() == LocalGameState.PREP_RESOURCES) {
                    if (localMulti.getMainPlayerPosition() == 0)
                        System.out.println("Please wait");
                    else if (localMulti.getMainPlayerPosition() == 1 || localMulti.getMainPlayerPosition() == 2)
                        if (localMulti.getMainPlayer().getLocalBoard().getResInDepotNumber() == 1) {
                            System.out.println("Please wait");
                        } else
                            System.out.println("Pick a free resource: type pr followed by 1 for Shield, 2 for Gold, 3 for Servant, 4 for Rock");
                    else if (localMulti.getMainPlayerPosition() == 3)
                        if (localMulti.getMainPlayer().getLocalBoard().getResInDepotNumber() == 1) {
                            System.out.println("Pick another free resource: type pr followed by 1 for Shield, 2 for Gold, 3 for Servant, 4 for Rock");
                        } else if (localMulti.getMainPlayer().getLocalBoard().getResInDepotNumber() == 2) {
                            System.out.println("Please wait");
                        } else
                            System.out.println("Pick two free resources: type pr followed by two numbers, 1 for Shield, 2 for Gold, 3 for Servant, 4 for Rock");
                } else {
                    System.out.println("Currently playing: " + localMulti.getLocalTurn().getCurrentPlayer().getName());
                }
            } else {
                if (localGame.getState() == LocalGameState.PREP_LEADERS) {
                    System.out.println("Pick two leader cards: type pl followed by two numbers, corresponding to the leader cards to keep");
                }
            }
            System.out.println(message);
        }
    }

    /**
     * Prints the generic "wrong command" text
     */
    protected void writeErrText() {
        message = ("Invalid choice, try again. To see the possible commands, write 'help'");
    }

    /**
     * Redirects to the help view
     */
    public void helpScreen() {
        removeObserved();
        ui.setState(new HelpView(ui));
    }

    /**
     * Redirects to the destroyed game view, gets called if someone left the game
     */
    public void goToDestroyed() {
        removeObserved();
        ui.setState(new DestroyedView(ui));
        // must call the draw function because this state change is not called by a player command, but by an update from the server
        ui.getState().draw();
    }

    /**
     * Redirects to the winner view, gets called if the game is over
     */
    private void goToWinnerScreen() {
        removeObserved();
        ui.setState(new WinnerView(ui));
        // must call the draw function because this state change is not called by a player command, but by an update from the server
        ui.getState().draw();
    }

    /**
     * Redirects to the history view
     */
    private void historyScreen() {
        removeObserved();
        ui.setState(new HistoryView(ui));
    }
}
