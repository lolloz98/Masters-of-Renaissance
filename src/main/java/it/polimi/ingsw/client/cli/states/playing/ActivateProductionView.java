package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.ApplyProductionMessage;

import java.io.IOException;
import java.util.TreeMap;

public class ActivateProductionView extends View<CLI> {
    private final int whichProd;
    private final LocalGame<?> localGame;
    /**
     * one by one, resources are taken from resToMove and put in resToGive once the player has decided where to take them from
     */
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive;
    private final TreeMap<Resource, Integer> resToGain;
    private final TreeMap<Resource, Integer> resToMove;

    public ActivateProductionView(CLI cli, int whichProd, LocalProduction localProduction) {
        this.ui = cli;
        this.localGame = cli.getLocalGame();
        localGame.overrideObserver(this);
        this.whichProd = whichProd;  
        this.resToGive = new TreeMap<>();
        LocalProduction prod = localProduction;
        if (whichProd == 0) prod = localGame.getMainPlayer().getLocalBoard().getBaseProduction();
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
                    MapUtils.addToResMap(resToMove, Resource.SHIELD);
                    MapUtils.removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "2":
                    MapUtils.addToResMap(resToMove, Resource.GOLD);
                    MapUtils.removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "3":
                    MapUtils.addToResMap(resToMove, Resource.SERVANT);
                    MapUtils.removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                case "4":
                    MapUtils.addToResMap(resToMove, Resource.ROCK);
                    MapUtils.removeResFromMap(resToMove, Resource.ANYTHING);
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else if (resToGain.containsKey(Resource.ANYTHING)) {
            switch (ans) {
                case "1":
                    MapUtils.addToResMap(resToGain, Resource.SHIELD);
                    MapUtils.removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "2":
                    MapUtils.addToResMap(resToGain, Resource.GOLD);
                    MapUtils.removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "3":
                    MapUtils.addToResMap(resToGain, Resource.SERVANT);
                    MapUtils.removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                case "4":
                    MapUtils.addToResMap(resToGain, Resource.ROCK);
                    MapUtils.removeResFromMap(resToGain, Resource.ANYTHING);
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else if (!MapUtils.isMapEmpty(resToMove)) {
            switch (ans) {
                case "1":
                    MapUtils.addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.NORMAL);
                    MapUtils.removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                case "2":
                    MapUtils.addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.LEADER);
                    MapUtils.removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                case "3":
                    MapUtils.addToResMapWarehouse(resToGive, resToMove.firstKey(), WarehouseType.STRONGBOX);
                    MapUtils.removeResFromMap(resToMove, resToMove.firstKey());
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else {
            switch (ans) {
                case "1":
                    // switch view, send message
                    localGame.removeAllObservers();
                    ui.setState(new BoardView(ui, localGame.getMainPlayer()));
                    try {
                        ui.getGameHandler().dealWithMessage(new ApplyProductionMessage(
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
                    localGame.removeAllObservers();
                    ui.setState(new BoardView(ui, localGame.getMainPlayer()));
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
            CLIutils.printResList();
        } else if (resToGain.containsKey(Resource.ANYTHING)) {
            System.out.println("Pick a res for the ANYTHING resource to gain");
            CLIutils.printResList();
        }
        // check if resToMove is empty, if it's not i pick one resource and ask where to put it
        else if (!MapUtils.isMapEmpty(resToMove)) {
            System.out.println("Pick where to take a " + resToMove.firstKey() + " from:");
            CLIutils.printWarehouseList();
        } else { // all above are false, i can ask for confirmation
            System.out.println("Res to give: " + resToGive);
            System.out.println("Res to gain: " + resToGain);
            System.out.println("Insert 1 to confirm, 2 to abort");
        }
    }
}
