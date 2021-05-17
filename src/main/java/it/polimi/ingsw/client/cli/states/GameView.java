package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.playing.DevelopmentGridView;
import it.polimi.ingsw.client.cli.states.playing.MarketView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.requests.FinishTurnMessage;

import java.io.IOException;
import java.util.ArrayList;

public abstract class GameView extends View<CLI> {
    protected LocalGame<?> localGame;
    protected boolean waiting;

    public abstract void draw();

    public abstract void removeObserved();

    public abstract void notifyUpdate();

    public void handleCommand(ArrayList<String> ansList) {
        int ansNumber;
        try {
            ansNumber = Integer.parseInt(ansList.get(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            ansNumber = 0;
        }
        switch (ansList.get(0)) {
            case "BOARD":
                moveToBoard(ansNumber);
                break;
            case "MARKET":
                moveToMarket(ansNumber);
                break;
            case "DEVELOP":
                moveToDevelop(ansNumber);
                break;
            case "HELP":
                helpScreen();
                break;
            case "NEXT":
                next();
                break;
            default:
                writeErrText();
        }
    }

    private void next() {
        if(localGame instanceof LocalSingle){
            try {
                ui.getServerListener().sendMessage(new FinishTurnMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LocalMulti localMulti = (LocalMulti) localGame;
            if(localMulti.getMainPlayerId() == localMulti.getLocalTurn().getCurrentPlayer().getId()){
                try {
                    ui.getServerListener().sendMessage(new FinishTurnMessage(localGame.getGameId(), localGame.getMainPlayer().getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("It's not your turn!");
            }
        }
    }

    private void moveToDevelop(int ansWithoutLetters) {
        if (ansWithoutLetters == 0){
            removeObserved();
            ui.setState(new DevelopmentGridView(ui, localGame, localGame.getLocalDevelopmentGrid()));
        } else writeErrText();
    }

    private void moveToMarket(int ansWithoutLetters) {
        if (ansWithoutLetters == 0){
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
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            System.out.print("Playing order:");
            ArrayList<LocalPlayer> localPlayers = localMulti.getLocalPlayers();
            for (int i = 0; i < localPlayers.size(); i++) {
                System.out.print(" " + (i + 1) + "," + localPlayers.get(i).getName());
            }
            System.out.print("\n");
            System.out.println("Currently playing: " + localMulti.getLocalTurn().getCurrentPlayer().getName());
        }
    }

    protected void writeErrText(){
        System.out.println("Invalid choice, try again. To see the possible commands, write 'help'");
    }


    public void helpScreen(){
        System.out.println("You can type:");
        System.out.println("'market' to look at the market");
        System.out.println("'develop' to look at the development decks");
        System.out.println("'board', followed by a number, to see the corresponding board");
        System.out.println("'next' to end your turn");
    }
}
