package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.MapUtils;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;

import java.io.IOException;
import java.util.TreeMap;

public class BuyDevelopmentCardView extends View {
    private final LocalGame<?> localGame;
    private TreeMap<Resource, Integer> cost;
    private Color color;
    private int level;
    private int slotNumber;
    private TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToPay;

    public BuyDevelopmentCardView(CLI cli, LocalGame<?> localGame, Color color, int level, int slotNumber, TreeMap<Resource, Integer> cost) {
        this.localGame = localGame;
        this.cost = new TreeMap<>(cost);
        this.cli = cli;
        this.color = color;
        this.level = level;
        this.slotNumber = slotNumber;
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void handleCommand(String ans) {
        if (!MapUtils.isMapEmpty(cost)) {
            switch (ans) {
                case "1":
                    MapUtils.addToResMapWarehouse(resToPay, cost.firstKey(), WarehouseType.NORMAL);
                    MapUtils.removeResFromMap(cost, cost.firstKey());
                    break;
                case "2":
                    MapUtils.addToResMapWarehouse(resToPay, cost.firstKey(), WarehouseType.LEADER);
                    MapUtils.removeResFromMap(cost, cost.firstKey());
                    break;
                case "3":
                    MapUtils.addToResMapWarehouse(resToPay, cost.firstKey(), WarehouseType.STRONGBOX);
                    MapUtils.removeResFromMap(cost, cost.firstKey());
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
                        cli.getServerListener().sendMessage(new BuyDevelopCardMessage(
                                localGame.getGameId(),
                                localGame.getMainPlayer().getId(),
                                level,
                                color,
                                slotNumber,
                                resToPay
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
        if (!MapUtils.isMapEmpty(cost)) {
            System.out.println("Pick where to take a " + cost.firstKey() + " from:");
            CLIutils.printWarehouseList();
        } else {
            System.out.println("Res to pay: " + resToPay);
            System.out.println("Insert 1 to confirm, 2 to abort");
        }
    }
}
