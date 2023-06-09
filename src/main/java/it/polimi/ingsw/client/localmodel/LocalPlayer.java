package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

public class LocalPlayer extends Observable implements Serializable {
    private static final long serialVersionUID = 16L;

    private final LocalBoard localBoard;
    private final int id;
    private final String name;

    public LocalBoard getLocalBoard() {
        return localBoard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalPlayer(int id, String name, LocalBoard localBoard){
        this.id = id;
        this.name = name;
        this.localBoard = localBoard;
    }
}
