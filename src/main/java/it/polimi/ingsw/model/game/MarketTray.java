package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;
import it.polimi.ingsw.model.exception.TooManyLeaderResourcesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class MarketTray {
    private Marble[][] marbleMatrix;
    private Marble freeMarble;
    private ArrayList<Resource> leaderResources;

    public MarketTray() {
        this.marbleMatrix = new Marble[4][3];
        ArrayList<Marble> marbles = new ArrayList<>();
        int i, j;
        for (i = 0; i < 4; i++) marbles.add(new Marble(Resource.NOTHING));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SHIELD));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.ROCK));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.SERVANT));
        for (i = 0; i < 2; i++) marbles.add(new Marble(Resource.GOLD));
        marbles.add(new Marble(Resource.FAITH));
        Collections.shuffle(marbles);
        for (j = 0; j < 3; j++) {
            for (i = 0; i < 4; i++) {
                marbleMatrix[i][j] = marbles.get(i + j * 4);
            }
        }
        freeMarble = marbles.get(12);
        leaderResources = new ArrayList<Resource>();
    }

    public Marble[][] getMarbleMatrix() {
        // FIXME: maybe it shoud return a copy
        return marbleMatrix;
    }

    public Marble getFreeMarble() {
        return freeMarble;
    }

    /**
     * Adds a resource to the list that indicates the resources that can be replaced to the white marble when a player uses the market
     *
     * @param resource is the resource to add to the list
     * @throws TooManyLeaderResourcesException if there are already 2 resourece in the list
     */
    public void addLeaderResource(Resource resource) {
        if (leaderResources.size() == 2) throw new TooManyLeaderResourcesException();
        leaderResources.add(resource);
    }

    /**
     * Clears the list that indicates the resources that can be replaced to the white marble
     */
    public void removeLeaderResources() {
        leaderResources.clear();
    }

    /**
     * Pushes the freeMarble into the marbleMatrix, and returns the resources
     *
     * @param index index of the matrix that indicates where to push the marble
     * @param onRow if true pushes on the row (from right), if false pushes on the column (from the bottom)
     * @return an ArrayList conaining the possible combinations of resources that the player can get, based on the leader cards he has activated
     * @throws MatrixIndexOutOfBoundException the combination of onRow and index is not valid
     */
    public ArrayList<TreeMap<Resource, Integer>> pushMarble(boolean onRow, int index) throws MatrixIndexOutOfBoundException {
        TreeMap<Resource, Integer> resourcesTaken = new TreeMap<>();
        Marble newMarble = freeMarble;
        Resource res;
        int i;
        if (onRow) { // push from right
            if (index > 2 || index < 0) throw new MatrixIndexOutOfBoundException();
            for(i=0; i<4; i++){
                res = marbleMatrix[i][index].getResource();
                addToResMap(resourcesTaken, res);
            }
            this.freeMarble = marbleMatrix[0][index];
            for(i=0; i<3; i++){
                marbleMatrix[i][index] = marbleMatrix[i+1][index];
            }
            marbleMatrix[3][index] = newMarble;
        } else { // push from the bottom
            if (index > 3 || index < 0) throw new MatrixIndexOutOfBoundException();
            for(i=0; i<3; i++){
                res = marbleMatrix[index][i].getResource();
                addToResMap(resourcesTaken, res);
            }
            this.freeMarble = marbleMatrix[index][0];
            for(i=0; i<2; i++){
                marbleMatrix[index][i] = marbleMatrix[index][i+1];
            }
            marbleMatrix[index][2] = newMarble;
        }
        return computeCombinations(resourcesTaken);
    }

    /**
     * Computes the combinations of resources, according to the leader resources
     *
     * @param resourcesTaken is the set of resources to compute the combinations
     * @return an ArrayList containing the possible combinations of resources that the player can get, based on the leader cards he has activated
     */
    private ArrayList<TreeMap<Resource, Integer>> computeCombinations(TreeMap<Resource, Integer> resourcesTaken) {
        ArrayList<TreeMap<Resource, Integer>> resCombinations = new ArrayList<TreeMap<Resource, Integer>>();
        if (leaderResources.size() == 0 || !resourcesTaken.containsKey(Resource.NOTHING))
            resCombinations.add(resourcesTaken);
        else{
            Integer howManyWhites = resourcesTaken.get(Resource.NOTHING);
            resourcesTaken.remove(Resource.NOTHING);
            if(leaderResources.size() == 1){
                addToResMap(resourcesTaken, leaderResources.get(0));
                resCombinations.add(resourcesTaken);
            }
            else{ // case with two leader resources
                TreeMap<Resource, Integer> tmp;
                for(int i = 0; i<=howManyWhites; i++){ // computing all possible combinations of the two resources
                    tmp = (TreeMap<Resource, Integer>) resourcesTaken.clone();
                    for(int j = 0; j<i; j++){
                        addToResMap(tmp, leaderResources.get(0));
                    }
                    for(int j = i; j<howManyWhites; j++){
                        addToResMap(tmp, leaderResources.get(1));
                    }
                    resCombinations.add(tmp);
                }
            }
        }
        return resCombinations;
    }

    /**
     * helper method to add a resource to a map.
     * if the resource is already present it increments the index, otherwise it adds the entry.
     */
    private void addToResMap(TreeMap<Resource, Integer> resMap, Resource resource){
        if (resMap.containsKey(resource)){
            resMap.replace(resource, 1+resMap.get(resource));
        }
        else{
            resMap.put(resource, 1);
        }
    }
}
