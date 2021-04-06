package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

public class MultiPlayer extends Game<TurnMulti> {
    private final ArrayList<Player> players;
    private boolean lastRound;

    public MultiPlayer(ArrayList<Player> players){
        super();
        this.players = players;
        this.lastRound = false;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Checks if end condition is met. If it is, the lastRound is set to true and when the last player completes its turn, the game ends.
     */
    @Override
    public void checkEndConditions(){
        for(Player p : players) {
            if (p.getBoard().getFaithtrack().isEndReached()) setLastRound(true);
            if (p.getBoard().getDevelopCardSlots().stream().mapToInt(x -> x.getCards().size()).sum() >= 6) setLastRound(true);
        }
    }

    /**
     * Method that changes the turn. If the next turn is null, it means that the game is over.
     */
    @Override
    public void nextTurn(){
        TurnMulti turn = getTurn().nextTurn(this);
        if (turn == null){
            setGameOver(true);
            // TODO: points computation
        }
        else setTurn(turn);
    }

}
