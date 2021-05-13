package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.*;
import it.polimi.ingsw.server.model.player.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Contains methods to convert from model to localModel
 */
public final class ConverterToLocalModel {
    private static final Logger logger = LogManager.getLogger(ConverterToLocalModel.class);

    public static LocalDevelopCard convert(DevelopCard developCard){
        return new LocalDevelopCard(developCard.getId(),
                developCard.getCost(),
                developCard.getLevel(),
                developCard.getColor(),
                developCard.getVictoryPoints(),
                developCard.getProduction().whatResourceToGive(),
                developCard.getProduction().whatResourceToGain(),
                developCard.getProduction().getGainedResources());
    }

    public static LocalDepotLeader convert(DepotLeaderCard depotLeaderCard){
        return new LocalDepotLeader(depotLeaderCard.getId(),
                depotLeaderCard.getVictoryPoints(),
                depotLeaderCard.isActive(),
                depotLeaderCard.isDiscarded(),
                depotLeaderCard.getDepot().getTypeOfResource(),
                depotLeaderCard.getRequirement().getRes(),
                depotLeaderCard.getRequirement().getQuantity()
                );
    }

    public static LocalDiscountLeader convert(DiscountLeaderCard discountLeaderCard){
        return new LocalDiscountLeader(discountLeaderCard.getId(),
                discountLeaderCard.getVictoryPoints(),
                discountLeaderCard.isActive(),
                discountLeaderCard.isDiscarded(),
                discountLeaderCard.getRequirement().getRequiredDevelop(),
                discountLeaderCard.getRes(),
                discountLeaderCard.getQuantity());
    }

    public static LocalProductionLeader convert(ProductionLeaderCard productionLeaderCard){
        return new LocalProductionLeader(productionLeaderCard.getId(),
                productionLeaderCard.getVictoryPoints(),
                productionLeaderCard.isActive(),
                productionLeaderCard.isDiscarded(),
                productionLeaderCard.getProduction().whatResourceToGive(),
                productionLeaderCard.getProduction().whatResourceToGain(),
                productionLeaderCard.getProduction().getGainedResources(),
                productionLeaderCard.getRequirement().getColor(),
                productionLeaderCard.getRequirement().getLevel());
    }

    public static LocalMarbleLeader convert(MarbleLeaderCard marbleLeaderCard){
        return new LocalMarbleLeader(marbleLeaderCard.getId(),
                marbleLeaderCard.getVictoryPoints(),
                marbleLeaderCard.isActive(),
                marbleLeaderCard.isDiscarded(),
                marbleLeaderCard.getTargetRes(),
                marbleLeaderCard.getRequirement().getRequiredDevelop());
    }

    public static LocalLeaderCard convert(LeaderCard<? extends Requirement> leaderCard) throws UnexpectedControllerException {
        if(leaderCard instanceof ProductionLeaderCard) return convert((ProductionLeaderCard) leaderCard);
        else if(leaderCard instanceof DiscountLeaderCard) return convert((DiscountLeaderCard) leaderCard);
        else if(leaderCard instanceof MarbleLeaderCard) return convert((MarbleLeaderCard) leaderCard);
        else if(leaderCard instanceof DepotLeaderCard) return convert((DepotLeaderCard) leaderCard);
        logger.error("We want to convert a leader which has no known type: " + leaderCard.getClass());
        throw new UnexpectedControllerException("Type of leader to convert not found");
    }

    public static LocalProduction convert(Production production){
        return new LocalProduction(production.whatResourceToGive(),
                production.whatResourceToGain(),
                production.getGainedResources());
    }

    public static LocalFigureState convert(FigureState fs) throws UnexpectedControllerException {
        switch (fs){
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
        for(int i = 0; i < 3; i++){
            fs[i] = convert(faithTrack.getFigures()[i].getState());
        }
        return new LocalTrack(fs, faithTrack.getPosition());
    }

    public static LocalBoard convert(Board board, boolean isBoardOfPlayerRequiring) throws UnexpectedControllerException {
        ArrayList<ArrayList<LocalDevelopCard>> localDevelopCards = new ArrayList<>();
        for(DevelopCardSlot dcs: board.getDevelopCardSlots()){
            ArrayList<LocalDevelopCard> localSlot = new ArrayList<>();
            for(DevelopCard d: dcs.getCards()){
                localSlot.add(convert(d));
            }
            localDevelopCards.add(localSlot);
        }
        ArrayList<LocalCard> localLeader = new ArrayList<>();
        for(LeaderCard<?> l: board.getLeaderCards()){
            if(l.isActive() || isBoardOfPlayerRequiring){
                localLeader.add(convert(l));
            }else localLeader.add(new LocalConcealedCard());
        }
        LocalTrack localTrack = convert(board.getFaithtrack());
        LocalProduction localBaseProduction = convert(board.getNormalProduction());
        return new LocalBoard(localDevelopCards, localLeader, localTrack, localBaseProduction, board.getInitialRes());

    }

    public static LocalPlayer convert(Player player, int playerIdRequiring) throws UnexpectedControllerException {
        return new LocalPlayer(player.getPlayerId(), player.getName(), convert(player.getBoard(), playerIdRequiring == player.getPlayerId()));
    }

    public static ArrayList<LocalPlayer> convert(ArrayList<Player> players, int playerIdRequiring) throws UnexpectedControllerException {
        ArrayList<LocalPlayer> lps = new ArrayList<>();
        for(Player p: players){
            lps.add(convert(p, playerIdRequiring));
        }
        return lps;
    }

    public static Resource[][] convert(Marble[][] marbleMatrix){
        Resource[][] mt = new Resource[marbleMatrix.length][marbleMatrix[0].length];
        for(int i = 0; i < marbleMatrix.length; i++){
            for(int j = 0; j < marbleMatrix[i].length; j++){
                mt[i][j] = marbleMatrix[i][j].getResource();
            }
        }
        return mt;
    }

    public static LocalMarket convert(MarketTray marketTray){
        return new LocalMarket(convert(marketTray.getMarbleMatrix()), marketTray.getResCombinations());
    }

    public static LocalDevelopmentGrid convert(TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop){
        LocalDevelopCard[][] topDevelopCards = new LocalDevelopCard[4][3];
        int[][] developCardsNumber = new int[4][3];
        for(Color color: decksDevelop.keySet()){
            for(Integer i: decksDevelop.get(color).keySet()){
                DeckDevelop deck = decksDevelop.get(color).get(i);
                try {
                    topDevelopCards[color.ordinal()][i - 1] = convert(deck.topCard());
                } catch (EmptyDeckException ignore) { } // we do nothing

                developCardsNumber[color.ordinal()][i - 1] = deck.howManyCards();
            }
        }
        return new LocalDevelopmentGrid(topDevelopCards, developCardsNumber);
    }

    private static LocalTurnSingle convert(TurnSingle turn){
        return new LocalTurnSingle(turn.isMainActionOccurred(), turn.isProductionsActivated(), turn.isMarketActivated());
    }

    private static LocalTurnMulti convert(TurnMulti turn, int playerIdRequiring) throws UnexpectedControllerException {
        return new LocalTurnMulti(turn.isMainActionOccurred(), turn.isProductionsActivated(), turn.isMarketActivated(), convert(turn.getCurrentPlayer(), playerIdRequiring));
    }

    public static LocalTurn convert(Turn turn, int playerIdRequiring) throws UnexpectedControllerException {
        if(turn instanceof TurnMulti) return convert((TurnMulti) turn, playerIdRequiring);
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
     * @param game current game
     * @param playerIdRequiring id of the player who we are sending the local game to
     * @param gameId current game id
     * @return localGame as it should be seen by the player with playerIdRequiring
     * @throws UnexpectedControllerException if something unexpected happens during conversion
     */
    public static LocalGame<?> convert(Game<?> game, int playerIdRequiring, int gameId) throws UnexpectedControllerException {
        if(game instanceof MultiPlayer) return convert((MultiPlayer) game, playerIdRequiring, gameId);
        else return convert((SinglePlayer) game, playerIdRequiring, gameId);
    }

    public static LocalGameState getGameState(MultiPlayer game){
        for(Player i: game.getPlayers()){
            if(i.getBoard().getInitialRes() != 0) return LocalGameState.PREP_RESOURCES;
        }
        for(Player i: game.getPlayers()){
            if(i.getBoard().getLeaderCards().size() != 2) return LocalGameState.PREP_LEADERS;
        }
        // todo: check that the condition for gameOver is right
        if(game.isGameOver()) return LocalGameState.OVER;
        return LocalGameState.READY;
    }

    public static LocalGameState getGameState(SinglePlayer game){
        if(game.getPlayer().getBoard().getInitialRes() != 0) return LocalGameState.PREP_RESOURCES; // It should never happen
        if(game.getPlayer().getBoard().getLeaderCards().size() != 2) return LocalGameState.PREP_LEADERS;
        if(game.isGameOver()) return LocalGameState.OVER;
        return LocalGameState.READY;
    }

    public static LocalGameState getGameState(Game<?> game){
        if(game instanceof MultiPlayer) return  getGameState((MultiPlayer) game);
        else return getGameState((SinglePlayer) game);
    }
}
