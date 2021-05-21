package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMarket;

public class HelpView extends GameView {

    public HelpView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        this.localGame = localGame;
        waiting = false;
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
        localGame.addObserver(this);
    }

    @Override
    public void draw() {
        System.out.println("Anywhere, you can type:");
        System.out.println("'sm' to look at the market");
        System.out.println("'sd' to look at the development decks");
        System.out.println("'sb', followed by a number, to see the corresponding board");
        System.out.println("'next' to end your turn");
        System.out.println("");
        System.out.println("In your board, you can type:");
        System.out.println("'al', followed by a number, to activate a leader card");
        System.out.println("'dl', followed by a number, to discard a leader card");
        System.out.println("'ad', followed by a number, to activate a production");
        System.out.println("'fd', to move all the resources currently in a production to the strongbox");
        System.out.println("");
        System.out.println("In the market, you can type:");
        System.out.println("'pm', followed by a number or a letter indicating where to push the free marble, to use the market");
        System.out.println("'fm', followed by the number of the combination to pick, to flush the resources to the board");
        System.out.println("");
        System.out.println("When you are looking at the development card decks, you can type:");
        System.out.println("'bd', followed by a card coordinate, followed by a number that indicates in which slot in the board to put it, to buy a development card (for example: 'buy a1 2')");
        System.out.println("");
        drawTurn();
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localGame.getLocalTurn().removeObserver();
        localGame.removeObserver();
    }
}
