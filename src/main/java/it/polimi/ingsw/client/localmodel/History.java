package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class History extends Observable implements Serializable {
    private static final long serialVersionUID = 10L;

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

    public String getLast(){
        if(history.isEmpty()) return "";
        else return history.get(history.size() - 1);
    }
}
