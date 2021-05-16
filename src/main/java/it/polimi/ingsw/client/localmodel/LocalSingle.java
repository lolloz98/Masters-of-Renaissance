package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.localmodel.exceptions.NoSuchLocalPlayerException;

import java.io.Serializable;

public class LocalSingle extends LocalGame<LocalTurnSingle> implements Serializable {
    private LocalPlayer mainPlayer;

    public synchronized void setLorenzoTrack(LocalTrack lorenzoTrack) {
        this.lorenzoTrack = lorenzoTrack;
    }

    private LocalTrack lorenzoTrack;

    public synchronized void setMainPlayer(LocalPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
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

    public LocalSingle(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, LocalTurnSingle localTurn, LocalGameState state, LocalTrack lorenzoTrack, LocalPlayer mainPlayer){
        super(gameId,localDevelopmentGrid, localMarket, localTurn, state);
        this.lorenzoTrack = lorenzoTrack;
        this.mainPlayer = mainPlayer;
    }
}
