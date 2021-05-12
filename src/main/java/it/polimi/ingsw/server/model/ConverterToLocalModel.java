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
import it.polimi.ingsw.server.model.game.Marble;
import it.polimi.ingsw.server.model.game.MarketTray;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;

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
        return new LocalBoard(localDevelopCards, localLeader, localTrack, localBaseProduction);

    }

    public static LocalPlayer convert(Player player, int playerIdRequiring) throws UnexpectedControllerException {
        return new LocalPlayer(player.getPlayerId(), player.getName(), convert(player.getBoard(), playerIdRequiring == player.getPlayerId()));
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
                    topDevelopCards[color.ordinal()][i] = convert(deck.topCard());
                } catch (EmptyDeckException ignore) { } // we do nothing

                developCardsNumber[color.ordinal()][i] = deck.howManyCards();
            }
        }
        return new LocalDevelopmentGrid(topDevelopCards, developCardsNumber);
    }

    // todo convert turn and game
}
