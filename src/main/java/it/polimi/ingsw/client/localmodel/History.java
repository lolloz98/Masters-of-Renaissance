package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class History extends Observable implements Serializable {
    private ArrayList<String> history;

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }
    public History(){
        history = new ArrayList<>();
    }
}
