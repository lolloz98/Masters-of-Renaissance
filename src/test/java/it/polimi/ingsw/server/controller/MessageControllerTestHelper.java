package it.polimi.ingsw.server.controller;

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

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * In this class we ignore the ActionControllers, to test just the messages
 */
public final class MessageControllerTestHelper {
    private static final Logger logger = LogManager.getLogger(MessageControllerTestHelper.class);


    public static ControllerActionsSingle getSingle(int gameId) throws NoSuchControllerException {
        return (ControllerActionsSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
    }

    public static ControllerActionsMulti getMulti(int gameId) throws NoSuchControllerException {
        return (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
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
    public static int toPrepStateMulti() throws ControllerException {
        int gameId = doActionCreateGameMulti();
        doJoinGameMulti(gameId);
        return gameId;
    }

    private static void chooseInitRes(int gameId, Player player) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
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
        int gameId = toPrepStateMulti();
        for (Player p : ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()) {
            chooseInitRes(gameId, p);
        }
        return gameId;
    }

    private static void removeLeaders(int gameId, Player player) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
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
            removeLeaders(gameId, p);
        }
        return gameId;
    }

    public static int toReadySingle() throws ControllerException {
        int gameId = doActionCreateGameSingle();
        removeLeaders(gameId, ((SinglePlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayer());
        return gameId;
    }

    public static GameStatusAnswer getGameStatus(int gameId, int playerId) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        GameStatusMessageController gameStatusMessageController = new GameStatusMessageController(new GameStatusMessage(gameId, playerId));
        return (GameStatusAnswer) gameStatusMessageController.doAction(ca);
    }

    /**
     * satisfy either the RequirementColorsDevelop or RequirementLevelDevelop with a bit of a stretch.
     * @param req developCard needed (Color is the color and the associated integer is the level)
     * @param game current game
     * @param player current player
     * @throws ControllerException something unexpected happens
     */
    private static void satisfyReq(TreeMap<Color, Integer> req, Game<?> game, Player player) throws ControllerException {
        for (Color c : req.keySet()) {
            try {
                int whichSlot = 0;
                while (!player.getBoard().getDevelopCardSlots().get(whichSlot).isEmpty()) {
                    whichSlot++;
                }
                if(req.get(c) > 3) logger.error("number too big. For RequirementColorsDevelop I considered this number as level, actually is the quantity of given color");
                for (int i = 1; i <= req.get(c); i++) {
                    TreeMap<Resource, Integer> cost = setResourcesInStrongBoxForDevelop(game, player,c, i);
                    player.getBoard().buyDevelopCard(game, c, i, whichSlot, new TreeMap<WarehouseType, TreeMap<Resource, Integer>>() {{
                        put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
                    }});
                }
            } catch (ModelException e) {
                throw new ControllerException(e.getMessage());
            }
        }
    }

    public static void satisfyReq(Requirement r, Game<?> game, Player p) throws ControllerException {
        try {
            if(r.checkRequirement(p)) return;
        } catch (ResourceNotDiscountableException e) {
            throw new ControllerException("something unexpected happened while checking the requirement");
        }

        if(r instanceof RequirementColorsDevelop) satisfyReq(((RequirementColorsDevelop) r).getRequiredDevelop(), game, p);
        else if(r instanceof RequirementLevelDevelop) satisfyReq(new TreeMap<>(){{
            put(((RequirementLevelDevelop) r).getColor(), ((RequirementLevelDevelop) r).getLevel());
        }}, game, p);
        else if(r instanceof RequirementResource) {
            try {
                p.getBoard().flushGainedResources(new TreeMap<>(){{
                    put(((RequirementResource) r).getRes(), ((RequirementResource) r).getQuantity());
                }}, game);
            } catch (ModelException e) {
               throw new ControllerException("Something happened while flushing resources to the board");
            }
        }
    }

    /**
     * It first checks if requirement for activation is met. If not it changes the status of the board of the player (and the game)
     * to satisfy it. then the card gets activated (if a valid card is passed).
     * Careful: to satisfy the requirement (put resources in the board and buy develop cards) it does it all performing actions
     * directly on the game and passing through messageControllers.
     * @param card to activate
     * @param player who wants to activate the card (must be the current player otherwise exception)
     * @param gameId current gameId
     * @throws ControllerException something unexpected happens or parameters given are invalid
     */
    public static void doActivateLeader(LeaderCard<?> card, Player player, int gameId) throws ControllerException {
        satisfyReq(card.getRequirement(), ControllerManager.getInstance().getControllerFromMap(gameId).getGame(), player);
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

    /**
     * It set up the resources in the strongBox of the player in order to have the possibility to buy the specified card.
     * CARE: this method does not use messageControllers, it manages the game directly.
     * @param game current game
     * @param player player buying the card
     * @param c color of the card to be bought
     * @param level level of the card to be bought
     * @return the cost of the card to b bought
     */
    public static TreeMap<Resource, Integer> setResourcesInStrongBoxForDevelop(Game<?> game, Player player, Color c, int level) throws EmptyDeckException, ResourceNotDiscountableException, InvalidArgumentException, InvalidStepsException, EndAlreadyReachedException {
        DevelopCard cardToBuy = game.getDecksDevelop().get(c).get(level).topCard();
        TreeMap<Resource, Integer> cost = cardToBuy.getCost();
        player.getBoard().flushGainedResources(cost, game);
        return cost;
    }

    /**
     * this method buys a develop card (not using messageController), and apply its production (using messageController).
     * (It puts the required resources to buy the card and to activate the production in the StrongBox).
     * @param gameId current gameId
     * @param player player (must be the current player)
     * @param c color of the develop to buy
     * @param level level of the develop
     * @param whichSlot slot where to put the bought develop
     */
    public static void setPlayerAndActivateProduction(int gameId, Player player, Color c, int level, int whichSlot) throws ResourceNotDiscountableException, InvalidArgumentException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException, ControllerException {
        Game<?> game = ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        DevelopCard card = game.getDecksDevelop().get(c).get(level).topCard();
        TreeMap<Resource, Integer> cost = setResourcesInStrongBoxForDevelop(game, player, c, level);
        player.getBoard().buyDevelopCard(game, c, level, whichSlot, new TreeMap<WarehouseType, TreeMap<Resource, Integer>>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
        }});
        player.getBoard().flushGainedResources(new TreeMap<>(card.getProduction().whatResourceToGive()), game);
        doApplyProduction(gameId, player, whichSlot + 1, new TreeMap<WarehouseType, TreeMap<Resource, Integer>>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(card.getProduction().whatResourceToGive()));
        }}, card.getProduction().whatResourceToGain());
    }

}
