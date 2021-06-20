package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;

/**
 * CLI state to show all the possible commands of the game
 */
public class HelpView extends GameView {

    public HelpView(CLI cli) {
        this.ui = cli;
        this.localGame = cli.getLocalGame();
        waiting = false;
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
    }

    @Override
    public void draw() {
        CLIutils.clearScreen();
        System.out.println("Anywhere, you can type:");
        System.out.println("'sm' to show the market");
        System.out.println("'sd' to show the development decks");
        if (localGame instanceof LocalMulti)
            System.out.println("'sb', followed by the number of a player in the turn order, to show his board");
        else
            System.out.println("'sb', to show your board");
        if (localGame instanceof LocalMulti)
            System.out.println("'sh', to show the history of actions of the players");
        else
            System.out.println("'sh', to show the history of actions of Lorenzo");
        System.out.println("'nt' to go to the next turn");
        System.out.println(" ");
        System.out.println("In your board, you can type:");
        System.out.println("'al', followed by a number, to activate a leader card");
        System.out.println("'dl', followed by a number, to discard a leader card");
        System.out.println("'ad', followed by a number, to activate a development card");
        System.out.println("(0 for the base development, otherwise the number of the slot on the board)");
        System.out.println("'ald', followed by a number, to activate a leader development card");
        System.out.println("'fd', to move all the resources currently in all development cards to the strongbox");
        System.out.println(" ");
        System.out.println("In the market, you can type:");
        System.out.println("'pm', followed by a number or a letter indicating where to push the free marble, to use the market");
        System.out.println("'fm', followed by the number of the combination to pick, to flush the resources to the board");
        System.out.println(" ");
        System.out.println("When you are looking at the development card decks, you can type:");
        System.out.println("'bd', followed by a card coordinate, followed by a number that indicates in which slot in the board to put it, to buy a development card (for example: 'bd a1 2')");
        System.out.println(" ");
        drawTurn();
    }
}
