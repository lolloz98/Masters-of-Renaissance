package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class FlushMarketResMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(FlushMarketResMessageControllerTest.class);

    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;
    TreeMap<Resource, Integer> chosenRes;
    TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep;

    @Test
    public void doAction() throws ControllerException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        Player player = ca.getGame().getPlayers().get(0);
        chosenRes = new TreeMap<>();
        toKeep = new TreeMap<>();
        // TODO CARE: IT'S WRONG THAT THIS LINE BELOW DOES NOT THROW AND EXCEPTION
        MessageControllerTestHelper.doFlushMarket(gameId, player, chosenRes, toKeep);
    }
}