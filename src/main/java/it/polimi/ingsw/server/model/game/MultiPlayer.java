package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Player;
import java.util.ArrayList;

/**
 * Concrete extension of the class Game. It has all the variables and methods needed to represent the state of a MultiPlayer game.
 */

public class MultiPlayer extends Game<TurnMulti> {
    private static final long serialVersionUID = 1022L;

    private final ArrayList<Player> players;
    private boolean lastRound;
    private final ArrayList<Integer> playerPoints = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MultiPlayer) {
            MultiPlayer t = (MultiPlayer) obj;
            return super.equals(obj) &&
                    players.equals(t.players) &&
                    lastRound == t.lastRound &&
                    playerPoints.equals(t.playerPoints);
        }
        return false;
    }

    public MultiPlayer(ArrayList<Player> players) throws PlayersOutOfBoundException, WrongColorDeckException, WrongLevelDeckException, EmptyDeckException {
        super();
        if (players.size()<2 || players.size()>4) throw new PlayersOutOfBoundException();
        this.turn = new TurnMulti(players.get(0));
        this.players = new ArrayList<>(players);
        this.lastRound = false;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    private void setLastRound() {
        this.lastRound = true;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getPlayer(int playerId) throws InvalidArgumentException {
        for(Player p: players){
            if(p.getPlayerId()==playerId){
                return p;
            }
        }
        throw new InvalidArgumentException("no player found with id " + playerId);
    }

    /**
     * Checks if end condition is met. If it is, the lastRound is set to true and when the last player completes its turn, the game ends.
     */
    @Override
    public void checkEndConditions(){
        for(Player p : players) {
            if (p.getBoard().getFaithtrack().isEndReached() ||
                    p.getBoard().getDevelopCardSlots().stream().mapToInt(x -> x.getCards().size()).sum() >= 6)
                setLastRound();
        }
    }

    /**
     * Method that changes the turn. If the next turn is null, it means that the game is over.
     *
     * @throws GameIsOverException if i call nextTurn on an game that is already terminated
     */
    @Override
    public void nextTurn() throws GameIsOverException, MarketTrayNotEmptyException, ProductionsResourcesNotFlushedException, MainActionNotOccurredException {
        if (isGameOver()) throw new GameIsOverException();
        TurnMulti turn = getTurn().nextTurn(this);
        if (!turn.getIsPlayable()){
            setGameOver(true);
            computeLeaderBoard();
        }
        else setTurn(turn);
    }

    /**
     * Computes the points of all the players
     */
    private void computeLeaderBoard(){
        playerPoints.clear();
        for (Player p : players){
            playerPoints.add(p.getBoard().getVictoryPoints());
        }

    }

    /**
     * Distribute 4 cards to the players
     */
    @Override
    public void distributeLeader() throws EmptyDeckException {
        for(Player p : players) distributeLeaderToPlayer(p);
    }

    /**
     * @return the winner if the game is over
     * @throws GameNotOverException if i call getWinner on an ongoing game
     */
    public ArrayList<Player> getWinners() throws GameNotOverException {
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
            int maxRes = 0;
            ArrayList<Player> winnersRes = new ArrayList<>();
            for (Player winner : winners) {
                if (winner.getBoard().howManyResources() == maxRes) {
                    winnersRes.add(winner);
                } else if (winner.getBoard().howManyResources() > maxRes) {
                    winnersRes.clear();
                    winnersRes.add(winner);
                    maxRes = winner.getBoard().howManyResources();
                }
            }
            return winnersRes;
        }
    }
}
