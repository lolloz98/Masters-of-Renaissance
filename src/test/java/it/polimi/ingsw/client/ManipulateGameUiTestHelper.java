package it.polimi.ingsw.client;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.leader.Requirement;
import it.polimi.ingsw.server.model.cards.leader.RequirementColorsDevelop;
import it.polimi.ingsw.server.model.cards.leader.RequirementLevelDevelop;
import it.polimi.ingsw.server.model.cards.leader.RequirementResource;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.TreeMap;

@Ignore
public class ManipulateGameUiTestHelper {
    private static final Logger logger = LogManager.getLogger(ManipulateGameUiTestHelper.class);

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
     * this method buys a develop card (not using messageController)
     * (It puts the required resources to buy the card in the StrongBox).
     * @param gameId current gameId
     * @param player player, can be any player
     * @param c color of the develop to buy
     * @param level level of the develop
     * @param whichSlot slot where to put the bought develop
     * @return developCard bought
     */
    public static DevelopCard setBuyDevelopCard(int gameId, Player player, Color c, int level, int whichSlot) throws NoSuchControllerException, EmptyDeckException, ResourceNotDiscountableException, InvalidArgumentException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException {
        Game<?> game = ControllerManager.getInstance().getControllerFromMap(gameId).getGame();
        DevelopCard card = game.getDecksDevelop().get(c).get(level).topCard();
        TreeMap<Resource, Integer> cost = setResourcesInStrongBoxForDevelop(game, player, c, level);
        player.getBoard().buyDevelopCard(game, c, level, whichSlot, new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
        }});
        return card;
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
                    player.getBoard().buyDevelopCard(game, c, i, whichSlot, new TreeMap<>() {{
                        put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
                    }});
                }
            } catch (ModelException e) {
                throw new ControllerException(e.getMessage());
            }
        }
    }

    /**
     * satisfies a requirement adjusting the status of a player (if resources are needed are always put and taken from the strongBox)
     * @param r requirement to be satisfied
     * @param game current game
     * @param p player for which to satisfy the req
     */
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

    private static void setChooseInitRes(int gameId, Player player, Resource res) throws ControllerException, InvalidTypeOfResourceToDepotException, InvalidArgumentException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException {
        ControllerActionsServer<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        while (player.getBoard().getInitialRes() != 0) {
            Board board = player.getBoard();
            board.gainResources(new TreeMap<>() {{
                put(res, 1);
            }}, new TreeMap<>() {{
                put(WarehouseType.NORMAL, new TreeMap<>() {{
                    put( res, 1);
                }});
            }}, ca.getGame());

            // once we add initResource to the depot, we diminish the counter
            board.setInitialRes(player.getBoard().getInitialRes() - 1);
        }
    }

    /**
     * remove first two leaders of the player
     * @param player player on which to perform the action
     */
    private static void setRemoveLeaders(Player player) throws InvalidArgumentException {
        player.getBoard().removeLeaderCards(new ArrayList<>() {{
            add(player.getBoard().getLeaderCards().get(0).getId());
            add(player.getBoard().getLeaderCards().get(1).getId());
        }});
    }

    /**
     * set state of the game to ready, it removes the first two leaders
     */
    public static void setStateOfGame1(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException {
        setRemoveLeaders(game.getPlayer());
    }

    /**
     * set status of the game:
     * init resources chosen, remove first two leaders from all players, and the first player has bought first blue card in slot 0
     */
    public static void setStateOfGame1(int gameId, MultiPlayer game) throws ResourceNotDiscountableException, InvalidArgumentException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException, ControllerException, EndAlreadyReachedException, InvalidTypeOfResourceToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException {
        try {
            setChooseInitRes(gameId, game.getPlayers().get(0), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayers().get(1), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayers().get(2), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayers().get(3), Resource.SERVANT);
        }catch (IndexOutOfBoundsException ignore){}
        for(Player p: game.getPlayers())
            setRemoveLeaders(p);
        setBuyDevelopCard(gameId, game.getPlayers().get(0), Color.BLUE, 1, 0);
    }
}
