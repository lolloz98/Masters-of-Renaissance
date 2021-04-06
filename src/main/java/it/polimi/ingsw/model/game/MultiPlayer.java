package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Concrete extension of the class Game. It has all the variables and methods needed to represent the state of a MultiPlayer game.
 */

public class MultiPlayer extends Game<TurnMulti> {
    private final ArrayList<Player> players;
    private boolean lastRound;
    private TreeMap<Player, Integer> leaderBoard;

    public MultiPlayer(ArrayList<Player> players){
        super();
        this.players = players;
        this.lastRound = false;
    }

    public TreeMap<Player, Integer> getLeaderBoard() {
        return leaderBoard;
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
            if (p.getBoard().getFaithtrack().isEndReached() ||
                    p.getBoard().getDevelopCardSlots().stream().mapToInt(x -> x.getCards().size()).sum() >= 6)
                setLastRound(true);
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
            computeLeaderBoard();
        }
        else setTurn(turn);
    }

    /**
     * Computes the points of all the players
     */
    private void computeLeaderBoard(){
        leaderBoard = new TreeMap<>(){{
            for (Player p : players){
                put(p, p.getBoard().getVictoryPoints());
            }
        }};
    }


}
