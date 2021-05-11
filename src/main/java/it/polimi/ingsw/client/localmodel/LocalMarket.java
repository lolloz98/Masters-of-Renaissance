package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;

import java.util.ArrayList;
import java.util.TreeMap;

public class LocalMarket extends Observable {
    private Resource[][] marbleMatrix;
    private Resource freeMarble;
    private ArrayList<TreeMap<Resource, Integer>> resCombinations;

    public synchronized Resource[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    public synchronized void setMarbleMatrix(Resource[][] marbleMatrix) {
        this.marbleMatrix = marbleMatrix;
        notifyObserver();
    }

    public synchronized Resource getFreeMarble() {
        return freeMarble;
    }

    public synchronized void setFreeMarble(Resource freeMarble) {
        this.freeMarble = freeMarble;
    }

    public synchronized ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return resCombinations;
    }

    public synchronized void setResCombinations(ArrayList<TreeMap<Resource, Integer>> resCombinations) {
        this.resCombinations = resCombinations;
        notifyObserver();
    }

    public LocalMarket(){
        // todo substitute with real constructor
        this.marbleMatrix = new Resource[3][4];
        ArrayList<Resource> resources = new ArrayList<>();
        int i, j;
        for (i = 0; i < 4; i++) resources.add(Resource.NOTHING);
        for (i = 0; i < 2; i++) resources.add(Resource.SHIELD);
        for (i = 0; i < 2; i++) resources.add(Resource.ROCK);
        for (i = 0; i < 2; i++) resources.add(Resource.SERVANT);
        for (i = 0; i < 2; i++) resources.add(Resource.GOLD);
        resources.add(Resource.FAITH);
        CollectionsHelper.shuffle(resources);
        for (j = 0; j < 3; j++) {
            for (i = 0; i < 4; i++) {
                marbleMatrix[j][i] = resources.get(i + j * 4);
            }
        }
        freeMarble = resources.get(12);
    }
}
