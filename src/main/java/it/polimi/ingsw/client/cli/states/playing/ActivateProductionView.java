package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.ApplyProductionMessage;

import java.io.IOException;
import java.util.TreeMap;

public class ActivateProductionView extends View {
    private final int whichProd;
    private final LocalGame<?> localGame;
    /**
     * one by one, resources are taken from resToMove and put in resToGive once the player has decided where to take them from
     */
    private TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive;
    private TreeMap<Resource, Integer> resToGain;
    private TreeMap<Resource, Integer> resToMove;

    public ActivateProductionView(CLI cli, LocalGame<?> localGame, int whichProd) {
        this.cli = cli;
        this.localGame = localGame;
        this.whichProd = whichProd;
        this.resToGive = new TreeMap<>();
        LocalProduction prod;
        if (whichProd == 0) prod = localGame.getMainPlayer().getLocalBoard().getBaseProduction();
        else if (whichProd > 0 && whichProd < 4)
            prod = localGame.getMainPlayer().getLocalBoard().getDevelopCards().get(whichProd - 1).get(localGame.getMainPlayer().getLocalBoard().getDevelopCards().get(whichProd - 1).size()).getProduction();
        else
            // already checked type
            prod = ((LocalProductionLeader) localGame.getMainPlayer().getLocalBoard().getLeaderCards().get(whichProd - 4)).getProduction();
        this.resToGain = new TreeMap<>(prod.getResToGain());
        this.resToMove = new TreeMap<>(prod.getResToGive());
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void handleCommand(String ans) {
        if (resToMove.containsKey(Resource.ANYTHING)) {
            switch (ans) {
                case "1":
                    addToResMap(resToMove, Resource.SHIELD);
                    removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "2":
                    addToResMap(resToMove, Resource.GOLD);
                    removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "3":
                    addToResMap(resToMove, Resource.SERVANT);
                    removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "4":
                    addToResMap(resToMove, Resource.ROCK);
                    removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else if (resToGain.containsKey(Resource.ANYTHING)) {
            switch (ans) {
                case "1":
                    addToResMap(resToGain, Resource.SHIELD);
                    removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "2":
                    addToResMap(resToGain, Resource.GOLD);
                    removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "3":
                    addToResMap(resToGain, Resource.SERVANT);
                    removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "4":
                    addToResMap(resToGain, Resource.ROCK);
                    removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else if (!isMapEmpty(resToMove)) {
            switch (ans) {
                case "1":
                    addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.NORMAL);
                    removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                case "2":
                    addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.LEADER);
                    removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                case "3":
                    addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.STRONGBOX);
                    removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else {
            switch (ans) {
                case "1":
                    // switch view, send message
                    cli.setState(new BoardView(cli, localGame, localGame.getMainPlayer()));
                    try {
                        cli.getServerListener().sendMessage(new ApplyProductionMessage(
                                localGame.getGameId(),
                                localGame.getMainPlayer().getId(),
                                whichProd,
                                resToGive,
                                resToGain
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    // only switch view
                    cli.setState(new BoardView(cli, localGame, localGame.getMainPlayer()));
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        }
    }

    @Override
    public void draw() {
        if (resToMove.containsKey(Resource.ANYTHING)) {
            System.out.println("Pick a res for the ANYTHING resource to give");
            printResList();
        } else if (resToGain.containsKey(Resource.ANYTHING)) {
            System.out.println("Pick a res for the ANYTHING resource to gain");
            printResList();
        }
        // check if resToMove is empty, if it's not i pick one resource and ask where to put it
        else if (!isMapEmpty(resToMove)) {
            System.out.println("Pick where to take a " + resToMove.firstKey() + " from:");
            printWarehouseList();
        } else { // all above are false, i can ask for confirmation
            System.out.println("Res to give: " + resToGive);
            System.out.println("Res to gain: " + resToGain);
            System.out.println("Insert 1 to confirm, 2 to abort");
        }
    }

    private void addToResMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        if (resMap.containsKey(resource)) {
            resMap.replace(resource, 1 + resMap.get(resource));
        } else {
            resMap.put(resource, 1);
        }
    }

    private void addToResMapWarehouse(TreeMap<WarehouseType, TreeMap<Resource, Integer>> resMap, Resource resource, WarehouseType warehouseType) {
        if (!resMap.containsKey(warehouseType)) {
            resMap.put(warehouseType, new TreeMap<>());
        }
        addToResMap(resMap.get(warehouseType), resource);
    }

    private void removeResFromMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        resMap.replace(resource, resMap.get(resource) - 1);
        if (resMap.get(resource) == 0) {
            resMap.remove(resource);
        }
    }

    private void printResList() {
        System.out.println("1. Shield");
        System.out.println("2. Gold");
        System.out.println("3. Servant");
        System.out.println("4. Rock");
    }

    private boolean isMapEmpty(TreeMap<Resource, Integer> map) {
        int sum = 0;
        for (Resource r : map.keySet()) {
            sum += map.get(r);
        }
        return sum == 0;
    }

    private void printWarehouseList() {
        System.out.println("1. Normal depot");
        System.out.println("2. Leader depot");
        System.out.println("3. Strongbox");
    }
}
