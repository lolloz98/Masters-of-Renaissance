package it.polimi.ingsw.model.game;

public class Turn {
    private boolean hasMainActionOccurred;
    private boolean hasLeaderActivated;

    public boolean isHasLeaderActivated() {
        return hasLeaderActivated;
    }

    public void setHasLeaderActivated(boolean hasLeaderActivated) {
        this.hasLeaderActivated = hasLeaderActivated;
    }

    public boolean isHasMainActionOccurred() {
        return hasMainActionOccurred;
    }

    public void setHasMainActionOccurred(boolean hasMainActionOccurred) {
        this.hasMainActionOccurred = hasMainActionOccurred;
    }

    public Turn(){

    }

    public Turn nextTurn(Game game){
        return new Turn();
    }

}
