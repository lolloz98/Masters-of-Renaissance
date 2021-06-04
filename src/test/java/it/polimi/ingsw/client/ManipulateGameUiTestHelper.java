package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.Marble;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

@Ignore
public class ManipulateGameUiTestHelper {
    private static final Logger logger = LogManager.getLogger(ManipulateGameUiTestHelper.class);

    /**
     * It set up the resources in the strongBox of the player in order to have the possibility to buy the specified card.
     * CARE: this method does not use messageControllers, it manages the game directly.
     *
     * @param game   current game
     * @param player player buying the card
     * @param c      color of the card to be bought
     * @param level  level of the card to be bought
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
     *
     * @param gameId    current gameId
     * @param player    player, can be any player
     * @param c         color of the develop to buy
     * @param level     level of the develop
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
     *
     * @param req    developCard needed (Color is the color and the associated integer is the level)
     * @param game   current game
     * @param player current player
     * @throws ControllerException something unexpected happens
     */
    private static void satisfyReq(TreeMap<Color, Integer> req, Game<?> game, Player player) throws ControllerException {
        for (Color c : req.keySet()) {
            try {
                int whichSlot = 0;
                int lev = 1;
                while (!player.getBoard().getDevelopCardSlots().get(whichSlot).isEmpty()) {

                    // todo check this if condition
                    if(player.getBoard().getDevelopCardSlots().get(whichSlot).lastCard().getLevel() == 1){
                        lev = 2;
                        break;
                    }

                    whichSlot++;
                }
                if (req.get(c) > 3)
                    logger.error("number too big. For RequirementColorsDevelop I considered this number as level, actually is the quantity of given color");
                int till = req.get(c);
                if(lev == 2){
                    till = 3;
                }
                for (int i = lev; i <= till; i++) {
                    TreeMap<Resource, Integer> cost = setResourcesInStrongBoxForDevelop(game, player, c, i);
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
     *
     * @param r    requirement to be satisfied
     * @param game current game
     * @param p    player for which to satisfy the req
     */
    public static void satisfyReq(Requirement r, Game<?> game, Player p) throws ControllerException {
        try {
            if (r.checkRequirement(p)) return;
        } catch (ResourceNotDiscountableException e) {
            throw new ControllerException("something unexpected happened while checking the requirement");
        }

        if (r instanceof RequirementColorsDevelop)
            satisfyReq(((RequirementColorsDevelop) r).getRequiredDevelop(), game, p);
        else if (r instanceof RequirementLevelDevelop) satisfyReq(new TreeMap<>() {{
            put(((RequirementLevelDevelop) r).getColor(), ((RequirementLevelDevelop) r).getLevel());
        }}, game, p);
        else if (r instanceof RequirementResource) {
            try {
                p.getBoard().flushGainedResources(new TreeMap<>() {{
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
                    put(res, 1);
                }});
            }}, ca.getGame());

            // once we add initResource to the depot, we diminish the counter
            board.setInitialRes(player.getBoard().getInitialRes() - 1);
        }
    }

    /**
     * remove first two leaders of the player
     *
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
    public static void setStateOfGame1(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, NotEnoughResourcesException, EndAlreadyReachedException {
        setRemoveLeaders(game.getPlayer());
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        setBuyDevelopCard(gameId, game.getPlayer(), Color.BLUE, 1, 0);
        setBuyDevelopCard(gameId, game.getPlayer(), Color.GOLD, 2, 0);
        // flush a bit of res in strongBox
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GREEN, 3);
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
        } catch (IndexOutOfBoundsException ignore) {
        }
        for (Player p : game.getPlayers())
            setRemoveLeaders(p);
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        setBuyDevelopCard(gameId, game.getPlayers().get(0), Color.BLUE, 1, 0);
    }

    /**
     * set state of the game to ready, it removes the first two leaders,
     * then calls the satisfyReq fir the first leadercard of the first player
     */
    public static void setStateOfGame2(int gameId, MultiPlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException {

        try {
            setChooseInitRes(gameId, game.getPlayers().get(0), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayers().get(1), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayers().get(2), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayers().get(3), Resource.SERVANT);
        } catch (IndexOutOfBoundsException ignore) {
        }
        for (Player p : game.getPlayers())
            setRemoveLeaders(p);
        satisfyReq(
                game.getPlayers().get(0).getBoard().getLeaderCards().get(0).getRequirement(),
                game,
                game.getPlayers().get(0)
        );
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
    }

    public static void setStateOfGame2(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, NotEnoughResourcesException {
        try {
            setChooseInitRes(gameId, game.getPlayer(), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayer(), Resource.SERVANT);
        } catch (IndexOutOfBoundsException ignore) {
        }
        game.getPlayer().getBoard().removeLeaderCards(new ArrayList<>() {{
            add(game.getPlayer().getBoard().getLeaderCards().get(1).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(0).getId());
        }});
        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(),
                game,
                game.getPlayer()
        );
        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(),
                game,
                game.getPlayer()
        );
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.BLUE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GOLD, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GREEN, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.PURPLE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.BLUE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GOLD, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GREEN, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.PURPLE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.BLUE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GOLD, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GREEN, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.PURPLE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.BLUE, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GOLD, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.GREEN, 3);
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.PURPLE, 3);
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
    }

    /**
     * set requirement of 2 leaders
     */
    public static void setStateOfGame3(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, NotEnoughResourcesException, EndAlreadyReachedException {
        setRemoveLeaders(game.getPlayer());
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        satisfyReq(game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(), game, game.getPlayer());
        satisfyReq(game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(), game, game.getPlayer());
    }

    /**
     * set requirement of 2 leaders. Lorenzo close to victory  (almost finished his track)
     */
    public static void setStateOfGame4(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, NotEnoughResourcesException, EndAlreadyReachedException {
        setRemoveLeaders(game.getPlayer());
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        satisfyReq(game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(), game, game.getPlayer());
        satisfyReq(game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(), game, game.getPlayer());
        game.getLorenzo().getFaithTrack().move(23, game);
    }

    /**
     * set requirement of 2 leaders.
     */
    public static void setStateOfGame5(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, NotEnoughResourcesException, EndAlreadyReachedException {
        setRemoveLeaders(game.getPlayer());
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        setResourcesInStrongBoxForDevelop(game, game.getPlayer(), Color.BLUE, 1);
    }

    /**
     * try strange situation for depots. CAREFUL: IT MODIFIES THE MARBLES IN MARKET
     */
    public static void setStateOfGame6(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, NotEnoughResourcesException, EndAlreadyReachedException {
        setRemoveLeaders(game.getPlayer());
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
        try {
            game.getPlayer().getBoard().storeInNormalDepot(new TreeMap<>() {{
                put(Resource.SHIELD, 1);
                put(Resource.ROCK, 1);
            }});
        } catch (TooManyResourcesToAddException e) {
            e.printStackTrace();
        }
        logger.warn("modifying the marbles in market for testing");
        Marble[][] m = game.getMarketTray().getMarbleMatrixMutable();
        m[0][0] = new Marble(Resource.SHIELD);
        m[0][1] = new Marble(Resource.ROCK);
        m[0][2] = new Marble(Resource.ROCK);
        m[0][3] = new Marble(Resource.GOLD);
    }

    /**
     * set two marble leaders
     */
    public static void setStateOfGame7(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, NotEnoughResourcesException {
        try {
            setChooseInitRes(gameId, game.getPlayer(), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayer(), Resource.SERVANT);
        } catch (IndexOutOfBoundsException ignore) {
        }
        game.getPlayer().getBoard().removeLeaderCards(new ArrayList<>() {{
            add(game.getPlayer().getBoard().getLeaderCards().get(1).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(0).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(2).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(3).getId());
        }});

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        logger.warn("hard setting leaders in player");
        try {
            game.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/058.json")), MarbleLeaderCard.class));
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/059.json")), MarbleLeaderCard.class));
            }});
        } catch (FileNotFoundException ignore) {

        }

        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(),
                game,
                game.getPlayer()
        );
        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(),
                game,
                game.getPlayer()
        );
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
    }

    /**
     * set two depot leaders
     */
    public static void setStateOfGame8(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, NotEnoughResourcesException {
        try {
            setChooseInitRes(gameId, game.getPlayer(), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayer(), Resource.SERVANT);
        } catch (IndexOutOfBoundsException ignore) {
        }
        game.getPlayer().getBoard().removeLeaderCards(new ArrayList<>() {{
            add(game.getPlayer().getBoard().getLeaderCards().get(1).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(0).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(2).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(3).getId());
        }});

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        logger.warn("hard setting leaders in player");
        try {
            game.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/054.json")), DepotLeaderCard.class));
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/055.json")), DepotLeaderCard.class));
            }});
        } catch (FileNotFoundException ignore) {

        }

        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(),
                game,
                game.getPlayer()
        );
        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(),
                game,
                game.getPlayer()
        );
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
    }

    /**
     * set two discount leaders
     */
    public static void setStateOfGame9(int gameId, SinglePlayer game) throws InvalidTypeOfResourceToDepotException, InvalidArgumentException, ControllerException, InvalidResourceQuantityToDepotException, InvalidResourcesToKeepByPlayerException, DifferentResourceForDepotException, ResourceNotDiscountableException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, NotEnoughResourcesException {
        try {
            setChooseInitRes(gameId, game.getPlayer(), Resource.SHIELD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.GOLD);
            setChooseInitRes(gameId, game.getPlayer(), Resource.ROCK);
            setChooseInitRes(gameId, game.getPlayer(), Resource.SERVANT);
        } catch (IndexOutOfBoundsException ignore) {
        }
        game.getPlayer().getBoard().removeLeaderCards(new ArrayList<>() {{
            add(game.getPlayer().getBoard().getLeaderCards().get(1).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(0).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(2).getId());
            add(game.getPlayer().getBoard().getLeaderCards().get(3).getId());
        }});

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        logger.warn("hard setting leaders in player");
        try {
            game.getPlayer().getBoard().addLeaderCards(new ArrayList<>() {{
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/049.json")), DiscountLeaderCard.class));
                add(gson.fromJson(new JsonReader(new FileReader("src/main/resources/json_file/cards/leader/050.json")), DiscountLeaderCard.class));
            }});
        } catch (FileNotFoundException ignore) {

        }

        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(0).getRequirement(),
                game,
                game.getPlayer()
        );
        satisfyReq(
                game.getPlayer().getBoard().getLeaderCards().get(1).getRequirement(),
                game,
                game.getPlayer()
        );
        ControllerManager.getInstance().getControllerFromMap(gameId).toGamePlayState();
    }
}
