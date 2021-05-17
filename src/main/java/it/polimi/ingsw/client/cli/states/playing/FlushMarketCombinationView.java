package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;

import java.io.IOException;
import java.util.TreeMap;

public class FlushMarketCombinationView extends View<CLI> {
    private final LocalGame<?> localGame;
    private final TreeMap<Resource, Integer> resToFlush;
    private final TreeMap<Resource, Integer> chosenCombination;
    private final TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToKeep;

    public FlushMarketCombinationView(CLI cli, LocalGame<?> localGame, TreeMap<Resource, Integer> resToFlush) {
        this.localGame = localGame;
        this.resToFlush = resToFlush;
        this.ui = cli;
        resToKeep = new TreeMap<>();
        chosenCombination = new TreeMap<>(resToFlush);
    }


    @Override
    public void notifyUpdate() {

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
                    MapUtils.removeResFromMap(resToFlush, resToFlush.firstKey());
                    break;
                case "2":
                    MapUtils.addToResMapWarehouse(resToKeep, resToFlush.firstKey(), WarehouseType.LEADER);
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
                    ui.setState(new BoardView(ui, localGame, localGame.getMainPlayer()));
                    try {
                        ui.getServerListener().sendMessage(new FlushMarketResMessage(
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
                    ui.setState(new BoardView(ui, localGame, localGame.getMainPlayer()));
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
            System.out.println("Res to flush: " + resToKeep);
            System.out.println("Insert 1 to confirm, 2 to abort");
        }
    }
}
