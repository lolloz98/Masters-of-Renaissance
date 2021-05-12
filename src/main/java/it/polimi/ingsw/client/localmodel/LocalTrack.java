package it.polimi.ingsw.client.localmodel;

public class LocalTrack extends Observable{
    private final LocalFigureState[] figuresState;
    private int position;

    public synchronized int getFaithTrackScore() {
        return position;
    }

    public synchronized void setFaithTrackScore(int faithTrackScore) {
        this.position = faithTrackScore;
        notifyObserver();
    }

    public synchronized void setFigureState(int i, LocalFigureState state){
        figuresState[i]=state;
        notifyObserver();
    }

    public synchronized LocalFigureState[] getFiguresState() {
        return figuresState;
    }

    public LocalTrack(){
        figuresState = new LocalFigureState [3];
        for(int i= 0; i<3; i++) figuresState[i] = LocalFigureState.INACTIVE;
    }
}
