package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.exceptions.NoSuchLocalPlayerException;

import java.io.Serializable;
import java.util.ArrayList;

public class LocalSingle extends LocalGame<LocalTurnSingle> implements Serializable {
    private LocalPlayer mainPlayer;

    public synchronized void setLorenzoTrack(LocalTrack lorenzoTrack) {
        this.lorenzoTrack = lorenzoTrack;
    }

    /**
     * to notify the board for an update of lorenzo track, it doesn't own an observer
     */
    private LocalTrack lorenzoTrack;

    public synchronized void setMainPlayer(LocalPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    @Override
    public synchronized ArrayList<LocalPlayer> getLocalPlayers() {
        ArrayList<LocalPlayer> localPlayer=new ArrayList<>();
        localPlayer.add(mainPlayer);
        return localPlayer;
    }

    public synchronized LocalPlayer getMainPlayer() {
        return mainPlayer;
    }

    @Override
    public synchronized LocalPlayer getPlayerById(int playerId) {
        if(mainPlayer.getId()==playerId)
            return mainPlayer;
        else
            throw new NoSuchLocalPlayerException();
    }

    public LocalTrack getLorenzoTrack() {
        return lorenzoTrack;
    }

    public LocalSingle(){
        super();
        this.localTurn = new LocalTurnSingle();
        lorenzoTrack = new LocalTrack();
    }

    @Override
    public boolean isMainPlayerTurn() {
        return true;
    }

    public LocalSingle(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, LocalTurnSingle localTurn, LocalGameState state, LocalTrack lorenzoTrack, LocalPlayer mainPlayer){
        super(gameId,localDevelopmentGrid, localMarket, localTurn, state);
        this.lorenzoTrack = lorenzoTrack;
        this.mainPlayer = mainPlayer;
    }
}
