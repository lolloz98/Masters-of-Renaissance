package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.playing.DevelopmentGridView;
import it.polimi.ingsw.client.cli.states.playing.MarketView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;

import java.util.ArrayList;

public abstract class GameView extends View {
    protected LocalGame<?> localGame;
    protected boolean waiting;

    public abstract void draw();

    public abstract void removeObserved();

    public abstract void notifyUpdate();

    public abstract void helpScreen();

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
            default:
                writeErrText();
        }
    }

    private void moveToDevelop(int ansWithoutLetters) {
        if (ansWithoutLetters == 0){
            removeObserved();
            cli.setState(new DevelopmentGridView(cli, localGame, localGame.getLocalDevelopmentGrid()));
        } else writeErrText();
    }

    private void moveToMarket(int ansWithoutLetters) {
        if (ansWithoutLetters == 0){
            removeObserved();
            cli.setState(new MarketView(cli, localGame, localGame.getLocalMarket()));
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
                    cli.setState(new BoardView(cli, localMulti, localMulti.getLocalPlayers().get(ansWithoutLetters - 1)));
                } else {
                    // go to ans-1 board
                    removeObserved();
                    cli.setState(new BoardView(cli, localMulti, localMulti.getMainPlayer()));
                }
            }
        }
        if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            if (ansWithoutLetters == 0) {
                // go to main board
                removeObserved();
                cli.setState(new BoardView(cli, localSingle, localSingle.getMainPlayer()));
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
}
