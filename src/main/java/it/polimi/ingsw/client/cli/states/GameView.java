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

public abstract class GameView extends View<CLI> {
    protected LocalGame<?> localGame;
    protected String message = "";

    public abstract void draw();

    public void removeObserved(){
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

    private void next() {
        try {
            ui.getGameHandler().dealWithMessage(new FinishTurnMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveToDevelop(int ansWithoutLetters) {
        if (ansWithoutLetters == 0) {
            removeObserved();
            ui.setState(new DevelopmentGridView(ui, localGame, localGame.getLocalDevelopmentGrid()));
        } else writeErrText();
    }

    private void moveToMarket(int ansWithoutLetters) {
        if (ansWithoutLetters == 0) {
            removeObserved();
            ui.setState(new MarketView(ui, localGame, localGame.getLocalMarket()));
        } else writeErrText();
    }

    private void moveToBoard(int ansWithoutLetters) {
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            if (ansWithoutLetters < 0 || ansWithoutLetters > localMulti.getLocalPlayers().size()) {
                writeErrText();
            } else {
                if (ansWithoutLetters == 0) {
                    // go to main board
                    removeObserved();
                    ui.setState(new BoardView(ui, localMulti, localMulti.getMainPlayer()));
                } else {
                    // go to ans-1 board
                    removeObserved();
                    ui.setState(new BoardView(ui, localMulti, localMulti.getLocalPlayers().get(ansWithoutLetters - 1)));
                }
            }
        }
        if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            if (ansWithoutLetters == 0) {
                // go to main board
                removeObserved();
                ui.setState(new BoardView(ui, localSingle, localSingle.getMainPlayer()));
            } else {
                writeErrText();
            }
        }
    }

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

    protected void writeErrText() {
        message = ("Invalid choice, try again. To see the possible commands, write 'help'");
    }

    public void helpScreen() {
        removeObserved();
        ui.setState(new HelpView(ui, localGame));
    }

    public void goToDestroyed() {
        removeObserved();
        ui.setState(new DestroyedView(ui, localGame));
        ui.getState().draw();
    }

    private void goToWinnerScreen() {
        removeObserved();
        ui.setState(new WinnerView(ui, localGame));
        ui.getState().draw();
    }

    private void historyScreen() {
        removeObserved();
        ui.setState(new HistoryView(ui, localGame));
    }
}
