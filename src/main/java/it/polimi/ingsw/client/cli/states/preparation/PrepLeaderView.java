package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.printers.BoardPrinter;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PrepLeaderView extends PreparationView {
    /**
     * Indicates the id of the leader cards to be removed
     */
    private ArrayList<Integer> leaderCardIds;
    /**
     * Position of the first card to be removed, just to show it
     */
    private int firstLeaderCardPosition;
    /**
     * boolean to store if it's the first time in this view, in this case show the board
     */
    private boolean showBoard;

    public PrepLeaderView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        this.localGame = localGame;
        this.localGame.addObserver(this);
        this.localGame.getError().addObserver(this);
        leaderCardIds = new ArrayList<>();
        waiting = false;
        showBoard = true;
    }

    @Override
    public synchronized void notifyUpdate() {
        if (localGame instanceof LocalSingle) {
            if (localGame.getState() == LocalGameState.READY) {
                localGame.removeObserver();
                localGame.getError().removeObserver();
                ui.setState(new BoardView(ui, localGame, localGame.getMainPlayer()));
                ui.getState().draw();
            } else draw();
        } else {
            if (localGame.getState() == LocalGameState.PREP_RESOURCES) {
                LocalMulti localMulti = (LocalMulti) localGame;
                switch (localMulti.getMainPlayerPosition()) {
                    case 0:
                        ui.setState(new PrepResFirstView(ui, localMulti));
                        break;
                    case 1:
                    case 2:
                        ui.setState(new PrepResSecondView(ui, localMulti, localMulti.getMainPlayer().getLocalBoard()));
                        break;
                    case 3:
                        ui.setState(new PrepResFourthView(ui, localMulti, localMulti.getMainPlayer().getLocalBoard()));
                        break;
                }
                ui.getState().draw();
            } else draw();
        }
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localGame.getError().getErrorMessage());
        leaderCardIds = new ArrayList<>();
        System.out.println("Try again:");
    }


    @Override
    public synchronized void handleCommand(String s) {
        String ans = s.toUpperCase();
        ArrayList<String> ansList = new ArrayList<>(Arrays.asList(ans.split("\\s+")));
        if (ansList.get(0) == "DL") {
            discardLeader(ansList);
        } else {
            if (ansList.size() > 1) {
                writeErrText();
            } else {
                super.handleCommand(ansList);
            }
        }

    }


    @Override
    public synchronized void draw() {
        CLIutils.clearScreen();
        CLIutils.printBlock(BoardPrinter.toStringBlock(localGame, localGame.getMainPlayer()));
        if (leaderCardIds.size() == 0) {
            System.out.println("Pick two leader cards to discard: type dl and the number of the leader wou want to discard");
            System.out.println("Type sm to show the market, sd for the development decks, sb to come back to the board");
        } else if (leaderCardIds.size() == 1) {
            System.out.println("You picked " + firstLeaderCardPosition + " to be discarded, pick another one");
        } else { // already picked two cards
            System.out.println("Please wait");
        }
        /*
        if (leaderCardIds.size() == 0) {
            System.out.println("Pick two leader cards to discard:");
            LocalCard c;
            for (int i = 1; i < 5; i++) {
                c = localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i - 1);
                if (c instanceof LocalDiscountLeader) {
                    LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                    System.out.print(i + ") " + localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i - 1).getClass().getSimpleName());
                    System.out.print(", prod requirement: " + localDiscountLeader.getProdRequirement());
                    System.out.print(", discounted res: " + localDiscountLeader.getQuantityToDiscount() + " " + localDiscountLeader.getDiscountedRes());
                } else if (c instanceof LocalMarbleLeader) {
                    LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                    System.out.print(i + ") " + localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i - 1).getClass().getSimpleName());
                    System.out.print(", prod requirement: " + localMarbleLeader.getProdRequirement());
                    System.out.print(", marble: " + localMarbleLeader.getMarbleResource());
                } else if (c instanceof LocalDepotLeader) {
                    LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                    System.out.print(i + ") " + localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i - 1).getClass().getSimpleName());
                    System.out.print(", requirement: " + localDepotLeader.getReqQuantity() + " " + localDepotLeader.getResRequirement());
                    System.out.print(", depot: " + localDepotLeader.getNumberOfRes() + " " + localDepotLeader.getResType());
                } else if (c instanceof LocalProductionLeader) {
                    LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                    System.out.print(i + ") " + localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(i - 1).getClass().getSimpleName());
                    System.out.print(", prod requirement: " + localProductionLeader.getColorRequirement() + " at level " + localProductionLeader.getLevelReq());
                    System.out.print(", production: to give " + localProductionLeader.getProduction().getResToGive());
                    System.out.print(", to gain " + localProductionLeader.getProduction().getResToGain());
                }
                System.out.print("\n");
            }
        } else if (leaderCardIds.size() == 1) {
            System.out.println("Pick another one:");
        } else { // already picked two cards
            System.out.println("Please wait");
        }

         */
    }

    private void discardLeader(ArrayList<String> ansList) {
        if (ansList.size() != 2) {
            if (leaderCardIds.size() < 2) { // there are still leaders to be picked
                try {
                    int ans = Integer.parseInt(ansList.get(1));
                    if (ans < 5 && ans > 0) {
                        firstLeaderCardPosition = ans;
                        leaderCardIds.add(localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(ans - 1).getId());
                    } else {
                        writeErrText();
                    }
                } catch (NumberFormatException e) {
                    writeErrText();
                }
                if (leaderCardIds.size() == 2) {
                    try {
                        ui.getGameHandler().dealWithMessage(new RemoveLeaderPrepMessage(
                                localGame.getGameId(),
                                localGame.getMainPlayer().getId(),
                                new ArrayList<>() {{
                                    add(leaderCardIds.get(0));
                                    add(leaderCardIds.get(1));
                                }}
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else writeErrText();
    }


}
