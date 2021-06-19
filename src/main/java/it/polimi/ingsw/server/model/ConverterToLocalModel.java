package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.cards.lorenzo.DevelopLorenzoCard;
import it.polimi.ingsw.server.model.cards.lorenzo.FaithLorenzoCard;
import it.polimi.ingsw.server.model.cards.lorenzo.LorenzoCard;
import it.polimi.ingsw.server.model.cards.lorenzo.ReshuffleLorenzoCard;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.*;
import it.polimi.ingsw.server.model.utility.PairId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Contains methods to convert from model to localModel
 */
public final class ConverterToLocalModel {
    private static final Logger logger = LogManager.getLogger(ConverterToLocalModel.class);

    /**
     * converts the lorenzo card in LocalLorenzoCard
     *
     * @param lorenzoCard to convert
     * @return LocalLorenzoCard
     */
    public static LocalLorenzoCard convert(LorenzoCard lorenzoCard) {
        if (lorenzoCard instanceof FaithLorenzoCard)
            return LocalLorenzoCard.FAITH;
        if (lorenzoCard instanceof ReshuffleLorenzoCard)
            return LocalLorenzoCard.RESHUFFLE;
        if (lorenzoCard instanceof DevelopLorenzoCard) {
            DevelopLorenzoCard devCard = (DevelopLorenzoCard) lorenzoCard;
            if (devCard.getColor().equals(Color.BLUE))
                return LocalLorenzoCard.DISCARD_BLUE;
            if (devCard.getColor().equals(Color.GREEN))
                return LocalLorenzoCard.DISCARD_GREEN;
            if (devCard.getColor().equals(Color.PURPLE))
                return LocalLorenzoCard.DISCARD_PURPLE;
            if (devCard.getColor().equals(Color.GOLD))
                return LocalLorenzoCard.DISCARD_GOLD;
        }
        return null;
    }

    /**
     * @param leaderboard
     * @return the localLeaderboard converted
     */
    public static ArrayList<PairId<LocalPlayer, Integer>> convert(ArrayList<PairId<Player, Integer>> leaderboard) throws UnexpectedControllerException {
        ArrayList<PairId<LocalPlayer, Integer>> localLeaderboard=new ArrayList<>();
        for(PairId<Player,Integer> el:leaderboard){
            localLeaderboard.add(new PairId<>(convert(el.getFirst(),0),el.getSecond()));//note: the playerIdRequiring is hardcoded because is not fundamental in this case
        }
        return localLeaderboard;
    }

    public static LocalDevelopCard convert(DevelopCard developCard) {
        return new LocalDevelopCard(developCard.getId(),
                developCard.isDiscounted(),
                developCard.getCost(),
                developCard.getLevel(),
                developCard.getColor(),
                developCard.getVictoryPoints(),
                developCard.getProduction().whatResourceToGive(),
                developCard.getProduction().whatResourceToGain(),
                developCard.getProduction().getGainedResources());
    }

    public static LocalDepotLeader convert(DepotLeaderCard depotLeaderCard) {
        return new LocalDepotLeader(depotLeaderCard.getId(),
                depotLeaderCard.getVictoryPoints(),
                depotLeaderCard.isActive(),
                depotLeaderCard.isDiscarded(),
                depotLeaderCard.getDepot().getTypeOfResource(),
                depotLeaderCard.getDepot().getStored(),
                depotLeaderCard.getRequirement().getRes(),
                depotLeaderCard.getRequirement().getQuantity()
        );
    }

    public static LocalDiscountLeader convert(DiscountLeaderCard discountLeaderCard) {
        return new LocalDiscountLeader(discountLeaderCard.getId(),
                discountLeaderCard.getVictoryPoints(),
                discountLeaderCard.isActive(),
                discountLeaderCard.isDiscarded(),
                discountLeaderCard.getRequirement().getRequiredDevelop(),
                discountLeaderCard.getRes(),
                discountLeaderCard.getQuantity());
    }

    public static LocalProductionLeader convert(ProductionLeaderCard productionLeaderCard) {
        return new LocalProductionLeader(productionLeaderCard.getId(),
                productionLeaderCard.getVictoryPoints(),
                productionLeaderCard.isActive(),
                productionLeaderCard.isDiscarded(),
                productionLeaderCard.getProduction().whatResourceToGive(),
                productionLeaderCard.getProduction().whatResourceToGain(),
                productionLeaderCard.getProduction().getGainedResources(),
                productionLeaderCard.getRequirement().getColor(),
                productionLeaderCard.getRequirement().getLevel(),
                productionLeaderCard.getWhichProd());
    }

    public static LocalMarbleLeader convert(MarbleLeaderCard marbleLeaderCard) {
        return new LocalMarbleLeader(marbleLeaderCard.getId(),
                marbleLeaderCard.getVictoryPoints(),
                marbleLeaderCard.isActive(),
                marbleLeaderCard.isDiscarded(),
                marbleLeaderCard.getTargetRes(),
                marbleLeaderCard.getRequirement().getRequiredDevelop());
    }

    public static LocalLeaderCard convert(LeaderCard<? extends Requirement> leaderCard) throws UnexpectedControllerException {
        if (leaderCard instanceof ProductionLeaderCard) return convert((ProductionLeaderCard) leaderCard);
        else if (leaderCard instanceof DiscountLeaderCard) return convert((DiscountLeaderCard) leaderCard);
        else if (leaderCard instanceof MarbleLeaderCard) return convert((MarbleLeaderCard) leaderCard);
        else if (leaderCard instanceof DepotLeaderCard) return convert((DepotLeaderCard) leaderCard);
        logger.error("We want to convert a leader which has no known type: " + leaderCard.getClass());
        throw new UnexpectedControllerException("Type of leader to convert not found");
    }

    public static LocalProduction convert(Production production) {
        return new LocalProduction(production.whatResourceToGive(),
                production.whatResourceToGain(),
                production.getGainedResources());
    }

    public static LocalFigureState convert(FigureState fs) throws UnexpectedControllerException {
        switch (fs) {
            case ACTIVE:
                return LocalFigureState.ACTIVE;
            case INACTIVE:
                return LocalFigureState.INACTIVE;
            case DISCARDED:
                return LocalFigureState.DISCARDED;
            default:
                logger.error("We want to convert a FigureState which has no known type: " + fs.name());
                throw new UnexpectedControllerException("Type of FigureState to convert not found");
        }
    }

    public static LocalTrack convert(FaithTrack faithTrack) throws UnexpectedControllerException {
        LocalFigureState[] fs = new LocalFigureState[3];
        for (int i = 0; i < 3; i++) {
            fs[i] = convert(faithTrack.getFigures()[i].getState());
        }
        return new LocalTrack(fs, faithTrack.getPosition());
    }

    public static LocalBoard convert(Board board, boolean isBoardOfPlayerRequiring) throws UnexpectedControllerException {
        ArrayList<ArrayList<LocalDevelopCard>> localDevelopCards = new ArrayList<>();
        for (DevelopCardSlot dcs : board.getDevelopCardSlots()) {
            ArrayList<LocalDevelopCard> localSlot = new ArrayList<>();
            for (DevelopCard d : dcs.getCards()) {
                localSlot.add(convert(d));
            }
            localDevelopCards.add(localSlot);
        }

        ArrayList<LocalCard> localLeader = new ArrayList<>();
        for (LeaderCard<?> l : board.getLeaderCards()) {
            if (l.isActive() || isBoardOfPlayerRequiring) {
                localLeader.add(convert(l));
            } else localLeader.add(new LocalConcealedCard());
        }

        LocalTrack localTrack = convert(board.getFaithtrack());
        LocalProduction localBaseProduction = convert(board.getNormalProduction());

        TreeMap<Resource, Integer> depots = new TreeMap<>();
        for (int i = 0; i < 3; i++) {
            try {
                TreeMap<Resource, Integer> tmp = board.getResInDepot(i);
                for (Resource r : tmp.keySet()) {
                    if (depots.containsKey(r)) logger.error("depots already contained " + r);
                    depots.put(r, tmp.get(r));
                }
            } catch (InvalidArgumentException e) {
                logger.error("Something unexpected happened while getting the depots");
            }
        }

        TreeMap<Resource, Integer> strongBox = board.getResourcesInStrongBox();

        return new LocalBoard(localDevelopCards, localLeader, localTrack, localBaseProduction, board.getInitialRes(), depots, strongBox);
    }

    public static LocalPlayer convert(Player player, int playerIdRequiring) throws UnexpectedControllerException {
        return new LocalPlayer(player.getPlayerId(), player.getName(), convert(player.getBoard(), playerIdRequiring == player.getPlayerId()));
    }

    public static ArrayList<LocalPlayer> convert(ArrayList<Player> players, int playerIdRequiring) throws UnexpectedControllerException {
        ArrayList<LocalPlayer> lps = new ArrayList<>();
        for (Player p : players) {
            lps.add(convert(p, playerIdRequiring));
        }
        return lps;
    }

    public static Resource[][] convert(Marble[][] marbleMatrix) {
        Resource[][] mt = new Resource[marbleMatrix.length][marbleMatrix[0].length];
        for (int i = 0; i < marbleMatrix.length; i++) {
            for (int j = 0; j < marbleMatrix[i].length; j++) {
                mt[i][j] = marbleMatrix[i][j].getResource();
            }
        }
        return mt;
    }

    public static LocalMarket convert(MarketTray marketTray) {
        return new LocalMarket(convert(marketTray.getMarbleMatrix()), marketTray.getResCombinations(), marketTray.getFreeMarble().getResource());
    }

    public static LocalDevelopmentGrid convert(TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop) {
        LocalDevelopCard[][] topDevelopCards = new LocalDevelopCard[4][3];
        int[][] developCardsNumber = new int[4][3];
        for (Color color : decksDevelop.keySet()) {
            for (Integer i : decksDevelop.get(color).keySet()) {
                DeckDevelop deck = decksDevelop.get(color).get(i);
                try {
                    topDevelopCards[color.ordinal()][i - 1] = convert(deck.topCard());
                } catch (EmptyDeckException ignore) {
                } // we do nothing

                developCardsNumber[color.ordinal()][i - 1] = deck.howManyCards();
            }
        }
        return new LocalDevelopmentGrid(topDevelopCards, developCardsNumber);
    }

    private static LocalTurnSingle convert(TurnSingle turn) {
        return new LocalTurnSingle(turn.isMainActionOccurred(), turn.isProductionsActivated(), turn.isMarketActivated());
    }

    private static LocalTurnMulti convert(TurnMulti turn, int playerIdRequiring) throws UnexpectedControllerException {
        return new LocalTurnMulti(turn.isMainActionOccurred(), turn.isProductionsActivated(), turn.isMarketActivated(), convert(turn.getCurrentPlayer(), playerIdRequiring));
    }

    public static LocalTurn convert(Turn turn, int playerIdRequiring) throws UnexpectedControllerException {
        if (turn instanceof TurnMulti) return convert((TurnMulti) turn, playerIdRequiring);
        else return convert((TurnSingle) turn);
    }

    public static LocalSingle convert(SinglePlayer game, int playerIdRequiring, int gameId) throws UnexpectedControllerException {
        return new LocalSingle(
                gameId,
                convert(game.getDecksDevelop()),
                convert(game.getMarketTray()),
                convert(game.getTurn()),
                getGameState(game),
                convert(game.getLorenzo().getFaithTrack()),
                convert(game.getPlayer(), playerIdRequiring)
        );
    }

    public static LocalMulti convert(MultiPlayer game, int playerIdRequiring, int gameId) throws UnexpectedControllerException {
        return new LocalMulti(
                gameId,
                convert(game.getDecksDevelop()),
                convert(game.getMarketTray()),
                convert(game.getTurn(), playerIdRequiring),
                getGameState(game),
                convert(game.getPlayers(), playerIdRequiring),
                playerIdRequiring
        );
    }

    /**
     * @param game              current game
     * @param playerIdRequiring id of the player who we are sending the local game to
     * @param gameId            current game id
     * @return localGame as it should be seen by the player with playerIdRequiring
     * @throws UnexpectedControllerException if something unexpected happens during conversion
     */
    public static LocalGame<?> convert(Game<?> game, int playerIdRequiring, int gameId) throws UnexpectedControllerException {
        if (game instanceof MultiPlayer) return convert((MultiPlayer) game, playerIdRequiring, gameId);
        else return convert((SinglePlayer) game, playerIdRequiring, gameId);
    }

    public static LocalGameState getGameState(MultiPlayer game) {
        for (Player i : game.getPlayers()) {
            if (i.getBoard().getLeaderCards().size() != 2) return LocalGameState.PREP_LEADERS;
        }
        for (Player i : game.getPlayers()) {
            if (i.getBoard().getInitialRes() != 0) return LocalGameState.PREP_RESOURCES;
        }

        if (game.isGameOver()) return LocalGameState.OVER;
        return LocalGameState.READY;
    }

    public static LocalGameState getGameState(SinglePlayer game) {
        if (game.getPlayer().getBoard().getLeaderCards().size() != 2) return LocalGameState.PREP_LEADERS;
        if (game.getPlayer().getBoard().getInitialRes() != 0) return LocalGameState.PREP_RESOURCES;
        if (game.isGameOver()) return LocalGameState.OVER;
        return LocalGameState.READY;
    }

    public static LocalGameState getGameState(Game<?> game) {
        if (game instanceof MultiPlayer) return getGameState((MultiPlayer) game);
        else return getGameState((SinglePlayer) game);
    }

    public static ArrayList<LocalTrack> getLocalFaithTracks(Game<?> game) throws UnexpectedControllerException {
        if (game instanceof SinglePlayer) return getLocalFaithTracks((SinglePlayer) game);
        else return getLocalFaithTracks((MultiPlayer) game);
    }

    public static ArrayList<LocalTrack> getLocalFaithTracks(SinglePlayer game) throws UnexpectedControllerException {
        ArrayList<LocalTrack> localTracks = new ArrayList<>();
        LocalTrack localTrack = ConverterToLocalModel.convert(game.getPlayer().getBoard().getFaithtrack());
        localTracks.add(localTrack);
        return localTracks;
    }

    public static ArrayList<LocalTrack> getLocalFaithTracks(MultiPlayer game) throws UnexpectedControllerException {
        ArrayList<LocalTrack> localTracks = new ArrayList<>();

        for (Player p : game.getPlayers()) {
            LocalTrack localTrack = ConverterToLocalModel.convert(p.getBoard().getFaithtrack());
            localTracks.add(localTrack);
        }
        return localTracks;
    }
}
