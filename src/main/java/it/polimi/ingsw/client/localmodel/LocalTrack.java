package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

public class LocalTrack extends Observable implements Serializable {
    private static final long serialVersionUID = 19L;

    private final LocalFigureState[] figuresState;
    private int position;

    public synchronized int getFaithTrackScore() {
        return position;
    }

    public synchronized void setFaithTrackScore(int faithTrackScore) {
        this.position = faithTrackScore;
    }

    public synchronized void setFigureState(int i, LocalFigureState state){
        figuresState[i]=state;
    }

    public synchronized LocalFigureState[] getFiguresState() {
        return figuresState;
    }

    public LocalTrack(){
        figuresState = new LocalFigureState [3];
        for(int i= 0; i<3; i++) figuresState[i] = LocalFigureState.INACTIVE;
    }

    public LocalTrack(LocalFigureState[] figuresStates, int position){
        this.figuresState = figuresStates;
        this.position = position;
    }
}
