package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.cli.states.ConversationalView;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;

import java.io.IOException;
import java.util.TreeMap;

/**
 * CLI state to handle the flush of the resources from the market
 */
public class FlushMarketCombinationView extends ConversationalView {
    /**
     * TreeMap containing the resources gained from the market.
     * When the player chooses where to put one of the resources, it gets removed from this tree
     */
    private final TreeMap<Resource, Integer> resToFlush;

    /**
     * temporary resource map to keep the ones to show in the confirmation question,
     * contains all resToFlush except for FAITH
     */
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToShow;

    /**
     * temporary int to keep the number of FAITH resources, to show in the confirmation question
     */
    private int faithNumber;

    /**
     * Copy of the TreeMap containing the resources gained from the market.
     * Needed to set the message to be sent to the server.
     */
    private final TreeMap<Resource, Integer> chosenCombination;

    /**
     * TreeMap containing the resources the player decided to keep, with the corresponding depots to be put in
     */
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToKeep;

    public FlushMarketCombinationView(CLI cli, TreeMap<Resource, Integer> resToFlush) {
        this.localGame = cli.getLocalGame();
        localGame.overrideObserver(this);
        this.resToFlush = resToFlush;
        this.ui = cli;
        resToKeep = new TreeMap<>();
        resToShow = new TreeMap<>();
        chosenCombination = new TreeMap<>(resToFlush);
        // i move the faith to normal depot in res to flush, so it doesn't show it as a choice
        if (resToFlush.containsKey(Resource.FAITH)) {
            faithNumber = resToFlush.remove(Resource.FAITH);
            for (int i = 0; i < faithNumber; i++){
                MapUtils.addToResMapWarehouse(resToKeep, Resource.FAITH, WarehouseType.NORMAL);
            }
        }
    }

    @Override
    public void notifyError() {
    }

    @Override
    public void handleCommand(String ans) {
        if (!MapUtils.isMapEmpty(resToFlush)) {
            switch (ans) {
                case "1":
                    MapUtils.addToResMapWarehouse(resToKeep, resToFlush.firstKey(), WarehouseType.NORMAL);
                    MapUtils.addToResMapWarehouse(resToShow, resToFlush.firstKey(), WarehouseType.NORMAL);
                    MapUtils.removeResFromMap(resToFlush, resToFlush.firstKey());
                    break;
                case "2":
                    MapUtils.addToResMapWarehouse(resToKeep, resToFlush.firstKey(), WarehouseType.LEADER);
                    MapUtils.addToResMapWarehouse(resToShow, resToFlush.firstKey(), WarehouseType.LEADER);
                    MapUtils.removeResFromMap(resToFlush, resToFlush.firstKey());
                    break;
                case "3":
                    MapUtils.removeResFromMap(resToFlush, resToFlush.firstKey());
                    break;
                default:
                    System.out.println("Invalid choice, try again:");
            }
        } else {
            switch (ans) {
                case "1":
                    // switch view, send message
                    localGame.removeAllObservers();
                    ui.setState(new BoardView(ui, localGame.getMainPlayer(), true));
                    try {
                        ui.getGameHandler().dealWithMessage(new FlushMarketResMessage(
                                localGame.getGameId(),
                                localGame.getMainPlayer().getId(),
                                chosenCombination,
                                resToKeep
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
        if (!MapUtils.isMapEmpty(resToFlush)) {
            System.out.println("Pick where to put a " + resToFlush.firstKey() + " in:");
            System.out.println("1. Normal depot");
            System.out.println("2. Leader depot");
            System.out.println("3. Don't keep it");
        } else {
            System.out.println("Res to flush: " + resToShow + " FAITH = " + faithNumber);
            System.out.println("Insert 1 to confirm, 2 to abort");
        }
    }
}
