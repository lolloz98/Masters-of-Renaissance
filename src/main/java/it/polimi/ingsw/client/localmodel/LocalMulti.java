package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.exceptions.NoSuchLocalPlayerException;
import it.polimi.ingsw.server.model.utility.PairId;

import java.io.Serializable;
import java.util.ArrayList;

public class LocalMulti extends LocalGame<LocalTurnMulti> implements Serializable {
    /**
     * id of the player playing on the specific device
     */
    private int mainPlayerId;
    private ArrayList<LocalPlayer> localPlayers;
    /**
     * who won the game
     */
    private ArrayList<LocalPlayer> winners = null;
    /**
     * leaderboard of the players who participated at this game
     */
    private ArrayList<PairId<LocalPlayer, Integer>> localLeaderBoard;

    public void setLocalLeaderBoard(ArrayList<PairId<LocalPlayer, Integer>> localLeaderBoard) {
        this.localLeaderBoard = localLeaderBoard;
    }

    public ArrayList<PairId<LocalPlayer, Integer>> getLocalLeaderBoard() {
        return localLeaderBoard;
    }

    public synchronized ArrayList<LocalPlayer> getWinners() {
        return winners;
    }

    public synchronized void setWinners(ArrayList<LocalPlayer> winners) {
        this.winners = winners;
    }

    public synchronized void setLocalPlayers(ArrayList<LocalPlayer> localPlayers) {
        this.localPlayers = localPlayers;
    }

    public synchronized void addLocalPlayer(LocalPlayer localPlayer) {
        this.localPlayers.add(localPlayer);
    }

    public synchronized int getMainPlayerId() {
        return mainPlayerId;
    }

    public synchronized void setMainPlayerId(int mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }

    @Override
    public synchronized ArrayList<LocalPlayer> getLocalPlayers() {
        return localPlayers;
    }

    public synchronized LocalPlayer getMainPlayer() {
        for (LocalPlayer p : localPlayers) {
            if (p.getId() == mainPlayerId) return p;
        }
        return null;
    }

    public synchronized int getMainPlayerPosition() {
        LocalPlayer mainPlayer = getMainPlayer();
        return localPlayers.indexOf(mainPlayer);
    }

    public LocalMulti() {
        super();
        this.localTurn = new LocalTurnMulti();
        localPlayers = new ArrayList<>();
        state = LocalGameState.NEW;
    }

    @Override
    public synchronized boolean isMainPlayerTurn() {
        return getLocalTurn().getCurrentPlayer().getId() == getMainPlayerId();
    }

    @Override
    public synchronized void removeAllObservers() {
        super.removeAllObservers();
        for (LocalPlayer p : localPlayers) {
            p.removeObservers();
            p.getLocalBoard().removeObservers();
            p.getLocalBoard().getLocalTrack().removeObservers();
        }
    }

    @Override
    public synchronized LocalPlayer getPlayerById(int id) {
        for (LocalPlayer l : localPlayers) {
            if (l.getId() == id) return l;
        }
        throw new NoSuchLocalPlayerException();
    }

    public LocalMulti(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, LocalTurnMulti localTurn, LocalGameState state, ArrayList<LocalPlayer> localPlayers, int mainPlayerId) {
        super(gameId, localDevelopmentGrid, localMarket, localTurn, state);
        this.localPlayers = localPlayers;
        this.mainPlayerId = mainPlayerId;
    }
}
