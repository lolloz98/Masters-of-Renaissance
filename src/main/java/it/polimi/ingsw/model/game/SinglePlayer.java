package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.lorenzo.LorenzoDeck;
import it.polimi.ingsw.model.player.Player;

public class SinglePlayer extends Game<TurnSingle>{
    private final Player player;
    private final Lorenzo lorenzo;
    private LorenzoDeck lorenzoDeck; // FIXME: final
    private boolean lastTurn;

    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    public SinglePlayer(Player player) {
        super();
        this.player = player;
        this.lorenzo = new Lorenzo();
        // TODO lorenzo Deck inizialization
    }

    public LorenzoDeck getLorenzoDeck() {
        return lorenzoDeck;
    }


    public Player getPlayer() {
        return player;
    }

    public Lorenzo getLorenzo() {
        return lorenzo;
    }

    /**
     * Checks if end condition is met. If it is, the lastRound is set to true
     * and when the last player completes its turn, the game ends.
     */
    @Override
    public void checkEndConditions(){
        // TODO after player package implementation
        // if (condition) set lastRound to true
    }

    /**
     * Method that changes the turn. If the next turn is null, it means that the game is over.
     */
    @Override
    public void nextTurn(){
        TurnSingle turn = getTurn().nextTurn(this);
        if (turn == null){
            setGameOver(true);
            // TODO: points computation
        }
        else setTurn(turn);
    }
}
