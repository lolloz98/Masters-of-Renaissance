package it.polimi.ingsw.client.localmodel;

public class LocalSingle extends LocalGame<LocalTurnSingle>{
    private LocalPlayer mainPlayer;
    private LocalTrack lorenzoTrack;

    public LocalPlayer getMainPlayer() {
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
