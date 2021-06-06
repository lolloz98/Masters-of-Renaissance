package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.ManipulateGameUiTestHelper;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.*;
import it.polimi.ingsw.messages.requests.actions.*;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.JoinGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.playing.*;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.controller.messagesctr.preparation.RemoveLeaderPrepMessageController;
import it.polimi.ingsw.server.model.Persist;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * In this class we ignore the ActionControllers, to test just the messageControllers
 */
@Ignore
public final class MessageControllerTestHelper {
    private static final Logger logger = LogManager.getLogger(MessageControllerTestHelper.class);

    public static ControllerActionsServerSingle getSingle(int gameId) throws NoSuchControllerException {
        return (ControllerActionsServerSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
    }

    public static ControllerActionsServerMulti getMulti(int gameId) throws NoSuchControllerException {
        return (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
    }

    /**
     * create a 4 player game
     *
     * @return gameId
     */
    public static int doActionCreateGameMulti() throws ControllerException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(4, "1"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        return answer.getGameId();
    }

    /**
     * @return gameId
     */
    public static int doActionCreateGameSingle() throws ControllerException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(1, "1"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        return answer.getGameId();
    }


    private static void doJoinGameMulti(int gameId, String playerName) throws ControllerException {
        JoinGameMessageController joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId, playerName));
        joinGameMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    /**
     * 3 players join the game
     *
     * @param gameId game to join
     */
    public static void doJoinGameMulti(int gameId) throws ControllerException {
        doJoinGameMulti(gameId, "2");
        doJoinGameMulti(gameId, "3");
        doJoinGameMulti(gameId, "4");
    }

    /**
     * create a game with 4 players
     *
     * @return gameId
     */
    public static int doToPrepStateMulti() throws ControllerException {
        int gameId = doActionCreateGameMulti();
        doJoinGameMulti(gameId);
        return gameId;
    }

    private static void doChooseInitRes(int gameId, Player player) throws ControllerException {
        ControllerActionsServer<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        while (player.getBoard().getInitialRes() != 0) {
            ChooseOneResPrepMessageController chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, player.getPlayerId(), Resource.GOLD));
            chooseOneResPrepMessageController.doAction(ca);
        }
    }

    /**
     * create a game with 4 players with init resources already picked by all players
     *
     * @return gameId
     */
    public static int toDecidedInitResMulti() throws ControllerException {
        int gameId = doToPrepStateMulti();
        for (Player p : ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()) {
            doChooseInitRes(gameId, p);
        }
        return gameId;
    }

    private static void doRemoveLeaders(int gameId, Player player) throws ControllerException {
        ControllerActionsServer<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        RemoveLeaderPrepMessageController removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, player.getPlayerId(), new ArrayList<>() {{
            add(player.getBoard().getLeaderCards().get(0).getId());
            add(player.getBoard().getLeaderCards().get(1).getId());
        }}));
        removeLeaderPrepMessageController.doAction(ca);
    }

    /**
     * create a game with 4 players in ready state (preparation finished)
     *
     * @return gameId
     */
    public static int toReadyMulti() throws ControllerException {
        int gameId = toDecidedInitResMulti();
        for (Player p : ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()) {
            doRemoveLeaders(gameId, p);
        }
        return gameId;
    }

    public static int toReadySingle() throws ControllerException {
        int gameId = doActionCreateGameSingle();
        doRemoveLeaders(gameId, ((SinglePlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayer());
        return gameId;
    }

    public static GameStatusAnswer getGameStatus(int gameId, int playerId) throws ControllerException {
        ControllerActionsServer<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        GameStatusMessageController gameStatusMessageController = new GameStatusMessageController(new GameStatusMessage(gameId, playerId));
        return (GameStatusAnswer) gameStatusMessageController.doAction(ca);
    }

    /**
     * It first checks if requirement for activation is met. If not it changes the status of the board of the player (and the game)
     * to satisfy it. then the card gets activated (if a valid card is passed).
     * Careful: to satisfy the requirement (put resources in the board and buy develop cards) it does it all performing actions
     * directly on the game and NOT passing through messageControllers.
     *
     * @param card   to activate
     * @param player who wants to activate the card (must be the current player otherwise exception)
     * @param gameId current gameId
     * @throws ControllerException something unexpected happens or parameters given are invalid
     */
    public static void doActivateLeader(LeaderCard<?> card, Player player, int gameId) throws ControllerException {
        ManipulateGameUiTestHelper.satisfyReq(card.getRequirement(), ControllerManager.getInstance().getControllerFromMap(gameId).getGame(), player);
        ActivateLeaderMessageController activateLeaderMessageController = new ActivateLeaderMessageController(new ActivateLeaderMessage(gameId, player.getPlayerId(), card.getId()));
        activateLeaderMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doPushMarble(int gameId, Player player, boolean onRow, int index) throws ControllerException {
        UseMarketMessageController useMarketMessageController = new UseMarketMessageController(new UseMarketMessage(gameId, player.getPlayerId(), onRow, index));
        useMarketMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doFlushMarket(int gameId, Player player, TreeMap<Resource, Integer> chosenCombination, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep) throws ControllerException {
        FlushMarketResMessageController flushMarketResMessageController = new FlushMarketResMessageController(new FlushMarketResMessage(gameId, player.getPlayerId(), chosenCombination, toKeep));
        flushMarketResMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doBuyDevelopCard(int gameId, Player player, int level, Color color, int whichSlot, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws ControllerException {
        BuyDevelopCardMessageController buyDevelopCardMessageController = new BuyDevelopCardMessageController(new BuyDevelopCardMessage(gameId, player.getPlayerId(), level, color, whichSlot, toPay));
        buyDevelopCardMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doApplyProduction(int gameId, Player player, int whichProd, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toGive, TreeMap<Resource, Integer> toGain) throws ControllerException {
        ApplyProductionMessageController applyProductionMessageController = new ApplyProductionMessageController(new ApplyProductionMessage(gameId, player.getPlayerId(), whichProd, toGive, toGain));
        applyProductionMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doFlushProductionResources(int gameId, Player player) throws ControllerException {
        FlushProductionResMessageController flushProductionResMessageController = new FlushProductionResMessageController(new FlushProductionResMessage(gameId, player.getPlayerId()));
        flushProductionResMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    public static void doFinishTurn(int gameId, Player player) throws ControllerException {
        FinishTurnMessageController finishTurnMessageController = new FinishTurnMessageController(new FinishTurnMessage(gameId, player.getPlayerId()));
        finishTurnMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    /**
     * this method buys a develop card (not using messageController), and apply its production (using messageController).
     * (It puts the required resources to buy the card and to activate the production in the StrongBox).
     *
     * @param gameId    current gameId
     * @param player    player (must be the current player)
     * @param c         color of the develop to buy
     * @param level     level of the develop
     * @param whichSlot slot where to put the bought develop
     */
    public static void setPlayerAndDoActivateProduction(int gameId, Player player, Color c, int level, int whichSlot) throws ResourceNotDiscountableException, InvalidArgumentException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException, ControllerException {
        DevelopCard card = ManipulateGameUiTestHelper.setBuyDevelopCard(gameId, player, c, level, whichSlot);
        Game<?> game = ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        player.getBoard().flushGainedResources(new TreeMap<>(card.getProduction().whatResourceToGive()), game);
        doApplyProduction(gameId, player, whichSlot + 1, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(card.getProduction().whatResourceToGive()));
        }}, card.getProduction().whatResourceToGain());
    }

    public static void cleanTmp() {
        final File folder = new File("tmp");
        if (!folder.exists()) return;
        List<File> files = List.of(Objects.requireNonNull(folder.listFiles()));
        List<String> fileNames = files.stream().filter(File::isFile).map(File::getName).collect(Collectors.toList());
        for (String fileName : fileNames) {
            if (fileName.endsWith(".tmp")) {
                logger.debug("adding game from file: " + fileName);
                String idS = fileName.substring(4);
                int id = Integer.parseInt(idS.split(".tmp")[0]);
                Persist.getInstance().remove(id);
            }
        }
    }
}
