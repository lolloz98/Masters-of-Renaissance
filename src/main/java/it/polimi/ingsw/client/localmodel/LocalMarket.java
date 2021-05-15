package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.server.model.game.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class LocalMarket extends Observable implements Serializable {
    private Resource[][] marbleMatrix;
    private Resource freeMarble;
    private ArrayList<TreeMap<Resource, Integer>> resCombinations;

    public synchronized Resource[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    private synchronized void setMarbleMatrix(Resource[][] marbleMatrix) {
        this.marbleMatrix = marbleMatrix;

    }

    public synchronized void setMarket(Resource[][] marbleMatrix, Resource freeMarble) {
        setMarbleMatrix(marbleMatrix);
        setFreeMarble(freeMarble);
    }

    public synchronized Resource getFreeMarble() {
        return freeMarble;
    }

    private synchronized void setFreeMarble(Resource freeMarble) {
        this.freeMarble = freeMarble;
    }

    public synchronized ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return resCombinations;
    }

    public synchronized void setResCombinations(ArrayList<TreeMap<Resource, Integer>> resCombinations) {
        this.resCombinations = resCombinations;
    }

    public LocalMarket(){
        this.marbleMatrix = new Resource[3][4];
        resCombinations = new ArrayList<>();
    }

    public LocalMarket(Resource[][] marbleMatrix, ArrayList<TreeMap<Resource, Integer>> resCombinations){
        this.marbleMatrix = marbleMatrix;
        this.resCombinations = resCombinations;
    }
}
