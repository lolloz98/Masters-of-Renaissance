package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.GameIsOverException;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Concrete extension of the class Game. It has all the variables and methods needed to represent the state of a MultiPlayer game.
 */

public class MultiPlayer extends Game<TurnMulti> {
    private final ArrayList<Player> players;
    private boolean lastRound;
    private ArrayList<Integer> playerPoints;

    public MultiPlayer(ArrayList<Player> players){
        super();
        this.turn = new TurnMulti(players.get(0));
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
        if (isGameOver()) throw new GameIsOverException();
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
        playerPoints = new ArrayList<>();
        for (Player p : players){
            playerPoints.add(p.getBoard().getVictoryPoints());
        }

    }

    /**
     * Distribute 4 cards to the players
     */
    @Override
    public void distributeLeader(){
        for(Player p : players) distributeLeaderToPlayer(p);
    }

    /**
     * @return the winner if the game is over
     */
    public void getWinner(){
        if(!isGameOver()) throw new GameNotOverException();
        // TODO
    }
}
