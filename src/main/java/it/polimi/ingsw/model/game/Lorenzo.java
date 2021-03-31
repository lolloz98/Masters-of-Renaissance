package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.FaithTrack;

public class Lorenzo {
    private int pathNumber;
    private final FaithTrack faithTrack;

    public Lorenzo(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public int getPathNumber() {
        return pathNumber;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }


}
