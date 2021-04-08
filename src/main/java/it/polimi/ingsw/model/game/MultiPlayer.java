package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.GameIsOverException;
import it.polimi.ingsw.model.exception.GameNotOverException;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

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
     *
     * @throws GameIsOverException if i call nextTurn on an game that is already terminated
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
     *
     * @throws GameNotOverException if i call getWinner on an ongoing game
     */
    public ArrayList<Player> getWinner(){
        if(!isGameOver()) throw new GameNotOverException();
        int max = 0;
        ArrayList<Player> winners = new ArrayList<>();
        for(int i=0; i<players.size(); i++) {
            if (playerPoints.get(i) == max) {
                winners.add(players.get(i));
            }
            else if(playerPoints.get(i) > max) {
                winners.clear();
                winners.add(players.get(i));
                max = playerPoints.get(i);
            }
        }
        if (winners.size() == 1) return winners;
        else {
            // TODO check which one has the most resources
            // player.getBoard().howManyResources()
            return winners;
        }
    }
}
