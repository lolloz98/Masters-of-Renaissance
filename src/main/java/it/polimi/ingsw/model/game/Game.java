package it.polimi.ingsw.model.game;

import java.util.ArrayList;
import java.util.TreeMap;

public class Game {
    private boolean isEnding;
    private int id;
    private Turn turn;

    public Game(){

    }

    public void checkEndConditions(){

    }

    public void endGame(){

    }

    public ArrayList<TreeMap<Resource, Integer>> useMarketTray(){
        return new ArrayList<TreeMap<Resource, Integer>>();
    }
}
