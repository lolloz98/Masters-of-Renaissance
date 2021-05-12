package it.polimi.ingsw.client.localmodel;

public class LocalSingle extends LocalGame<LocalTurnSingle>{
    private LocalPlayer mainPlayer;
    private final LocalTrack lorenzoTrack;

    public synchronized void setMainPlayer(LocalPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
    }
    public synchronized LocalPlayer getMainPlayer() {
        return mainPlayer;
    }

    public LocalTrack getLorenzoTrack() {
        return lorenzoTrack;
    }

    public LocalSingle(){
        super();
        this.localTurn = new LocalTurnSingle();
        lorenzoTrack = new LocalTrack();
    }
}
