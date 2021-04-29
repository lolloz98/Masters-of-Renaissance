package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.model.game.Marble;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.utility.CollectionsHelper;

import java.util.ArrayList;
import java.util.TreeMap;

public class LocalMarket extends LocalModelAbstract {
    private Marble[][] marbleMatrix;
    private UI ui;
    private Marble freeMarble;
    private ArrayList<TreeMap<Resource, Integer>> resCombinations;

    public synchronized Marble[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    public void setMarbleMatrix(Marble[][] marbleMatrix) {
        this.marbleMatrix = marbleMatrix;
        ui.notifyAction(this);
    }

    public synchronized Marble getFreeMarble() {
        return freeMarble;
    }

    public synchronized void setFreeMarble(Marble freeMarble) {
        this.freeMarble = freeMarble;
    }

    public synchronized ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return resCombinations;
    }

    public synchronized void setResCombinations(ArrayList<TreeMap<Resource, Integer>> resCombinations) {
        this.resCombinations = resCombinations;
        ui.notifyAction(this);
    }

    public LocalMarket(UI ui){
        // todo substitute with real constructor
        this.marbleMatrix = new Marble[3][4];
        ArrayList<Marble> marbles = new ArrayList<>();
        int i, j;
        for (i = 0; i < 4; i++) marbles.add(new Marble(Resource.NOTHING));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SHIELD));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.ROCK));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SERVANT));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.GOLD));
        marbles.add(new Marble(Resource.FAITH));
        CollectionsHelper.shuffle(marbles);
        for (j = 0; j < 3; j++) {
            for (i = 0; i < 4; i++) {
                marbleMatrix[j][i] = marbles.get(i + j * 4);
            }
        }
        freeMarble = marbles.get(12);
        this.ui = ui;
    }
}
