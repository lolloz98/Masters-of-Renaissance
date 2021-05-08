package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class which represents the market tray, containing 12 marbles in a matrix and a free one
 */

public class MarketTray {
    private final Marble[][] marbleMatrix;
    private Marble freeMarble;
    private final ArrayList<Resource> leaderResources;
    private final ArrayList<TreeMap<Resource, Integer>> resCombinations;
    private boolean isResCombinationsUsed = false;

    public MarketTray(MarbleDispenserInterface md) {
        this.marbleMatrix = new Marble[3][4];
        ArrayList<Marble> marbles = md.getMarbles();
        int i, j;
        for (j = 0; j < 3; j++) {
            for (i = 0; i < 4; i++) {
                marbleMatrix[j][i] = marbles.get(i + j * 4);
            }
        }
        freeMarble = marbles.get(12);
        leaderResources = new ArrayList<>();
        resCombinations = new ArrayList<>();
    }

    public ArrayList<Resource> getLeaderResources(){
        return new ArrayList<>(leaderResources);
    }

    /**
     * @return a copy resCombinations
     */
    public ArrayList<TreeMap<Resource, Integer>> getResCombinations() {
        return new ArrayList<>(resCombinations);
    }

    /**
     * Resets resCombinations
     */
    public void removeResources(){
        resCombinations.clear();
        isResCombinationsUsed = false;
    }

    /**
     * Checks if a combination of resources is contained in resCombinations
     *
     * @param res the combination to check
     * @return a boolean indicating the result of the check
     */
    public boolean checkResources(TreeMap<Resource, Integer> res){
        return resCombinations.contains(res);
    }

    /**
     * @return a copy of the marble matrix
     */
    public Marble[][] getMarbleMatrix() {
        Marble[][] marbleMatrixCopy = new Marble[marbleMatrix.length][];
        for(int i = 0; i < marbleMatrix.length; i++)
            marbleMatrixCopy[i] = marbleMatrix[i].clone();
        return marbleMatrixCopy;
    }

    public Marble getFreeMarble() {
        return freeMarble;
    }

    /**
     * Adds a resource to the list that indicates the resources that can be replaced to the white marble when a player uses the market
     *
     * @param resource is the resource to add to the list
     * @throws TooManyLeaderResourcesException if there are already 2 resource in the list
     * @throws AlreadyPresentLeaderResException if resource is already in leaderResources
     */
    public void addLeaderResource(Resource resource) {
        if (leaderResources.size() == 2) throw new TooManyLeaderResourcesException();
        if(leaderResources.contains(resource)) throw new AlreadyPresentLeaderResException();
        leaderResources.add(resource);
    }

    /**
     * Clears the list that indicates the resources that can be replaced to the white marble
     */
    public void removeLeaderResources() {
        leaderResources.clear();
    }

    /**
     * Removes the effect of a MarbleLeader from the market
     *
     * @param res the resource to remove
     * @throws NoSuchResourceException if there aren't active MarbleLeader of res type
     */
    public void removeLeaderResource(Resource res) {
        boolean removed = leaderResources.remove(res);
        if (!removed) throw new NoSuchResourceException();
    }

    /**
     * @return if there are active MarbleLeaders
     */
    public boolean isLeaderApplied() {
        return !(leaderResources.size()==0);
    }

    /**
     * Pushes the freeMarble into the marbleMatrix, and stores an ArrayList containing the possible combinations of resources that the player can get, based on the leader cards he has activated.
     *
     * @param index index of the matrix that indicates where to push the marble
     * @param onRow if true pushes on the row (from right), if false pushes on the column (from the bottom)
     * @throws MatrixIndexOutOfBoundException the combination of onRow and index is not valid
     * @throws MarketTrayNotEmptyException if resCombinations is used
     */
    public void pushMarble(boolean onRow, int index) {
        if (isResCombinationsUsed) throw new MarketTrayNotEmptyException();
        TreeMap<Resource, Integer> resourcesTaken = new TreeMap<>();
        Marble newMarble = freeMarble;
        Resource res;
        int i;
        if (onRow) { // push from right
            if (index > 2 || index < 0) throw new MatrixIndexOutOfBoundException();
            for(i=0; i<4; i++){
                res = marbleMatrix[index][i].getResource();
                addToResMap(resourcesTaken, res);
            }
            this.freeMarble = marbleMatrix[index][0];
            for(i=0; i<3; i++){
                marbleMatrix[index][i] = marbleMatrix[index][i+1];
            }
            marbleMatrix[index][3] = newMarble;
        } else { // push from the bottom
            if (index > 3 || index < 0) throw new MatrixIndexOutOfBoundException();
            for(i=0; i<3; i++){
                res = marbleMatrix[i][index].getResource();
                addToResMap(resourcesTaken, res);
            }
            this.freeMarble = marbleMatrix[0][index];
            for(i=0; i<2; i++){
                marbleMatrix[i][index] = marbleMatrix[i+1][index];
            }
            marbleMatrix[2][index] = newMarble;
        }
        resCombinations.addAll(computeCombinations(resourcesTaken));
        isResCombinationsUsed = true;
    }

    /**
     * Computes the combinations of resources, according to the leader resources
     *
     * @param resourcesTaken is the set of resources to compute the combinations
     * @return an ArrayList containing the possible combinations of resources that the player can get, based on the leader cards he has activated
     */
    private ArrayList<TreeMap<Resource, Integer>> computeCombinations(TreeMap<Resource, Integer> resourcesTaken) {
        ArrayList<TreeMap<Resource, Integer>> localResCombinations = new ArrayList<>();
        if (leaderResources.size() == 0 || !resourcesTaken.containsKey(Resource.NOTHING)) {
            resourcesTaken.remove(Resource.NOTHING);
            localResCombinations.add(resourcesTaken);
        }
        else {
            Integer howManyWhites = resourcesTaken.get(Resource.NOTHING);
            resourcesTaken.remove(Resource.NOTHING);
            if(leaderResources.size() == 1){
                for (int i = 0; i < howManyWhites; i++) {
                    addToResMap(resourcesTaken, leaderResources.get(0));
                }
                localResCombinations.add(resourcesTaken);
            }
            else{ // case with two leader resources
                TreeMap<Resource, Integer> tmp;
                for(int i = 0; i<=howManyWhites; i++){ // computing all possible combinations of the two resources
                    tmp = new TreeMap<>(resourcesTaken);
                    for(int j = 0; j<i; j++){
                        addToResMap(tmp, leaderResources.get(0));
                    }
                    for(int j = i; j<howManyWhites; j++){
                        addToResMap(tmp, leaderResources.get(1));
                    }
                    localResCombinations.add(tmp);
                }
            }
        }
        return localResCombinations;
    }

    /**
     * Helper method to add a resource to a map.
     * If the resource is already present it increments the index, otherwise it adds the entry.
     *
     * @param resource the resource to add to the TreeMap
     * @param resMap the map to add it to
     */
    private void addToResMap(TreeMap<Resource, Integer> resMap, Resource resource){
        if (resMap.containsKey(resource)) {
            resMap.replace(resource, 1 + resMap.get(resource));
        } else {
            resMap.put(resource, 1);
        }
    }
}
