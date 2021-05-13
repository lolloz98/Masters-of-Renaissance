package it.polimi.ingsw.server.model.player;


import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * class that models the board of each player
 */
public class Board implements VictoryPointCalculator {
    private static final Logger logger = LogManager.getLogger(Board.class);

    private final FaithTrack faithtrack;
    private final StrongBox strongbox;
    private final ArrayList<LeaderCard<? extends Requirement>> leaderCards;
    private final ArrayList<DevelopCardSlot> developCardSlots;
    private final ArrayList<ProductionLeaderCard> productionLeaderSlots;
    private final Production normalProduction;
    private final ArrayList<DepotLeaderCard> depotLeaders;
    private final ArrayList<Depot> depots;

    /**
     * number of resources Any given at the beginning of the game. It becomes 0 when the player decides how to store them in the board
     */
    private int initialRes = 0;

    public void setInitialRes(int initialRes) {
        this.initialRes = initialRes;
    }

    public int getInitialRes() {
        return initialRes;
    }

    public Board() throws InvalidArgumentException {
        this.faithtrack = new FaithTrack();
        this.strongbox = new StrongBox();
        leaderCards = new ArrayList<>();
        developCardSlots = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            developCardSlots.add(new DevelopCardSlot());
        productionLeaderSlots = new ArrayList<>();
        TreeMap<Resource, Integer> resToGive = new TreeMap<>();
        TreeMap<Resource, Integer> resToGain = new TreeMap<>();
        resToGive.put(Resource.ANYTHING, 2);
        resToGain.put(Resource.ANYTHING, 1);
        normalProduction = new Production(resToGive, resToGain);
        depotLeaders = new ArrayList<DepotLeaderCard>();
        depots = new ArrayList<Depot>();
        for (int i = 0; i < 3; i++)
            depots.add(new Depot(i + 1, true));
    }

    public Board(int initialRes) throws InvalidArgumentException {
        this();
        this.initialRes = initialRes;
    }

    public Production getNormalProduction() {
        return normalProduction;
    }

    public ArrayList<DevelopCardSlot> getDevelopCardSlots() {
        return new ArrayList<>(developCardSlots);
    }

    public FaithTrack getFaithtrack() {
        return faithtrack;
    }

    /**
     * method that add a list of leadercard to the board
     *
     * @param l list of leadercards
     */
    public void addLeaderCards(ArrayList<LeaderCard<? extends Requirement>> l) {
        leaderCards.addAll(l);
    }

    /**
     * method that handle the application of the production chosen
     *
     * @param resToGive and resToGain are given by the player
     * @param whichProd if zero it refers to the normalProduction, if 1,2 or 3 it refers to which productionSlot,
     *                  if 4 or 5 it refers to the leaderCardProductionSlot.
     * @throws ProductionAlreadyActivatedException if the production has already been activated in this turn
     * @throws InvalidResourcesByPlayerException   if toPay or resourcesToGain contain invalid type of Resources
     * @throws NotEnoughResourcesException         if there are not enough resources topay on the board
     */
    public void activateProduction(int whichProd, TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive, TreeMap<Resource, Integer> resToGain, Game<?> game) throws ModelException {
        if (whichProd < 0)
            throw new InvalidProductionSlotChosenException();
        if(whichProd > 0 && whichProd <= 3 && whichProd > developCardSlots.size())
            throw new InvalidProductionSlotChosenException();
        if(whichProd > 3 && whichProd - 3 > productionLeaderSlots.size())
            throw new InvalidProductionSlotChosenException();

        if (whichProd == 0) {
            normalProduction.applyProduction(resToGive, resToGain, this);
        }
        if (whichProd > 0 && whichProd <= 3){
            developCardSlots.get(whichProd-1).applyProduction(resToGive, resToGain, this);
        }
        if (whichProd >= 4) { //branch taken if the production chosen is a LeaderProduction
            if (!theLeaderProductionIsActivated(whichProd - 4)) throw new InvalidProductionSlotChosenException();
            productionLeaderSlots.get(whichProd-4).getProduction().applyProduction(resToGive, resToGain, this);
        }
    }

    /**
     * flush resources from productions to the board
     * @param game current game
     */
    public void flushResFromProductions(Game<?> game) throws ResourceNotDiscountableException, InvalidStepsException, EndAlreadyReachedException, FigureAlreadyDiscardedException, InvalidArgumentException, FigureAlreadyActivatedException {
        normalProduction.flushGainedToBoard(this,game);
        for(DevelopCardSlot ds: developCardSlots){
            if(!ds.isEmpty())
                ds.lastCard().getProduction().flushGainedToBoard(this, game);
        }
        for(ProductionLeaderCard pl : productionLeaderSlots){
            pl.getProduction().flushGainedToBoard(this, game);
        }
    }

    /**
     * checks if i have enough resources compared to resToGive, this method is useful for the "smart" methods of payment
     *
     * @param resToGive to be checked if in board there are at least this amount of resources
     * @return true if there are enough resources on the board, false otherwise
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    public boolean enoughResToActivate(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException {
        TreeMap<Resource, Integer> diffMap = new TreeMap<>(resToGive);
        TreeMap<Resource, Integer> tmpDiffMap = new TreeMap<>();
        enoughResInNormalDepots(diffMap, tmpDiffMap);//can throw exception

        diffMap.clear();
        diffMap.putAll(tmpDiffMap);
        enoughResInLeaderDepots(diffMap, tmpDiffMap);//can throw exception

        diffMap.clear();
        diffMap.putAll(tmpDiffMap);
        enoughResInStrongBox(diffMap, tmpDiffMap);//can throws exception

        diffMap.clear();
        diffMap.putAll(tmpDiffMap);
        for (Resource r : diffMap.keySet()) {
            if (diffMap.get(r) > 0)
                return false;
        }
        return true;
    }

    /**
     * @param resToGive resources we would like to remove from normal depots.
     * @return true, if it is possible to remove resToGive from the normal depots
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    public boolean enoughResInNormalDepots(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException {
        return enoughResInNormalDepots(resToGive, new TreeMap<>());
    }

    /**
     * @param resToGive resources we would like to remove from leader depots.
     * @return true, if it is possible to remove resToGive from the leader depots
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    public boolean enoughResInLeaderDepots(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException {
        return enoughResInLeaderDepots(resToGive, new TreeMap<>());
    }

    /**
     * @param resToGive resources we would like to remove from the strong box.
     * @return true, if it is possible to remove resToGive from the strong box
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    public boolean enoughResInStrongBox(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException {
        return enoughResInStrongBox(resToGive, new TreeMap<>());
    }

    /**
     * @param resToGive resources we would like to remove from normal depots.
     * @param diff      first it is cleared then there are put all the resources that we were not able to put in the normal depots
     * @return true, if it is possible to remove resToGive from the normal depots
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    private boolean enoughResInNormalDepots(TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> diff) throws ResourceNotDiscountableException {
        diff.clear();
        diff.putAll(resToGive);
        for (Resource r : diff.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException(r);
            for (Depot d : depots) {
                if (d.contains(r))
                    diff.replace(r, diff.get(r) - d.getStored());
            }
        }
        for (Resource r : diff.keySet()) {
            if (diff.get(r) > 0)
                return false;
        }
        return true;
    }

    /**
     * @param resToGive resources we would like to remove from leader depots.
     * @param diff      first it is cleared then there are put all the resources that we were not able to put in the leader depots
     * @return true, if it is possible to remove resToGive from the leader depots
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    private boolean enoughResInLeaderDepots(TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> diff) throws ResourceNotDiscountableException {
        diff.clear();
        diff.putAll(resToGive);
        for (Resource r : diff.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (DepotLeaderCard dl : depotLeaders) {
                if (dl.getDepot().contains(r))
                    diff.replace(r, diff.get(r) - dl.getDepot().getStored());
            }
        }
        for (Resource r : diff.keySet()) {
            if (diff.get(r) > 0)
                return false;
        }
        return true;
    }

    /**
     * @param resToGive resources we would like to remove from the strong box.
     * @param diff      first it is cleared then there are put all the resources that we were not able to put in the strong box
     * @return true, if it is possible to remove resToGive from the strong box
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    private boolean enoughResInStrongBox(TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> diff) throws ResourceNotDiscountableException {
        diff.clear();
        diff.putAll(resToGive);
        for (Resource r : diff.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (Resource inStrongBox : strongbox.getResources().keySet()) {
                if (inStrongBox.equals(r))
                    diff.replace(r, diff.get(r) - strongbox.getResources().get(inStrongBox));
            }
        }
        for (Resource r : diff.keySet()) {
            if (diff.get(r) > 0)
                return false;
        }
        return true;
    }

    private boolean theLeaderProductionIsActivated(int whichLeader) throws InvalidArgumentException {
        if (whichLeader >= productionLeaderSlots.size() || whichLeader < 0) throw new InvalidArgumentException("Invalid production slot leader selected");
        else
            return productionLeaderSlots.get(whichLeader).isActive();
    }

    /**
     * remove resToGive from the board.
     * It removes the resources in this order: from the normal Depots, from LeaderDepots (if any) and from the strongbox
     *
     * @param resToGive resources to be removed from the board
     * @throws NotEnoughResourcesException      if there are not enough resources on the board
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    public void removeResourcesSmart(TreeMap<Resource, Integer> resToGive) throws NotEnoughResourcesException, ResourceNotDiscountableException, InvalidResourceQuantityToDepotException, InvalidArgumentException {
        if (!enoughResToActivate(resToGive)) throw new NotEnoughResourcesException();
        TreeMap<Resource, Integer> resToSpend = removeResFromNormalDepotNoCheck(resToGive);
        resToSpend = removeResFromLeaderDepotNoCheck(resToSpend);
        removeResFromStrongBoxNoCheck(resToSpend);
    }

    /**
     * @param resToGive resources to be removed from the normal depots
     * @return treeMap with the resources still to be spent
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    private TreeMap<Resource, Integer> removeResFromNormalDepotNoCheck(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException, InvalidResourceQuantityToDepotException {
        TreeMap<Resource, Integer> resToSpend = new TreeMap<>(resToGive);
        for (Resource r : resToGive.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (Depot d : depots) {
                if (d.contains(r)) {
                    if (!d.enoughResources(resToSpend.get(r))) {
                        int tmp = d.clear();
                        resToSpend.replace(r, resToSpend.get(r) - tmp);
                    } else {
                        d.spendResources(resToSpend.get(r));
                        resToSpend.remove(r);
                    }
                }
            }
        }
        return resToSpend;
    }

    /**
     * @param resToGive resources to be removed from the leader depots
     * @return treeMap with the resources still to be spent
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    private TreeMap<Resource, Integer> removeResFromLeaderDepotNoCheck(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException, InvalidResourceQuantityToDepotException {
        TreeMap<Resource, Integer> resToSpend = new TreeMap<>(resToGive);
        for (Resource r : resToGive.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (DepotLeaderCard dl : depotLeaders) {
                if (dl.getDepot().contains(r)) {
                    if (!dl.getDepot().enoughResources(resToSpend.get(r))) {
                        int tmp = dl.getDepot().clear();
                        resToSpend.replace(r, resToSpend.get(r) - tmp);
                    } else {
                        dl.getDepot().spendResources(resToSpend.get(r));
                        resToSpend.remove(r);
                    }
                }
            }
        }
        return resToSpend;
    }

    /**
     * @param resToGive resources to be removed from the strongbox
     * @throws NotEnoughResourcesException if there are not enough resources on the strongbox
     */
    private void removeResFromStrongBoxNoCheck(TreeMap<Resource, Integer> resToGive) throws ResourceNotDiscountableException, NotEnoughResourcesException, InvalidArgumentException {
        TreeMap<Resource, Integer> resToSpend = new TreeMap<>(resToGive);
        for (Resource r : resToGive.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
        }
        strongbox.spendResources(resToSpend);
    }

    /**
     * @param resToGive resources to be removed from the normal depots
     * @throws NotEnoughResourcesException      if there are not enough resources on the normal depots
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    public void removeResFromNormalDepot(TreeMap<Resource, Integer> resToGive) throws NotEnoughResourcesException, ResourceNotDiscountableException, InvalidResourceQuantityToDepotException {
        if (!enoughResInNormalDepots(resToGive)) throw new NotEnoughResourcesException();
        removeResFromNormalDepotNoCheck(resToGive);
    }

    /**
     * @param resToGive resources to be removed from the leader depots
     * @throws NotEnoughResourcesException      if there are not enough resources on the leader depots
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    public void removeResFromLeaderDepot(TreeMap<Resource, Integer> resToGive) throws NotEnoughResourcesException, ResourceNotDiscountableException, InvalidResourceQuantityToDepotException {
        if (!enoughResInLeaderDepots(resToGive)) throw new NotEnoughResourcesException();
        removeResFromLeaderDepotNoCheck(resToGive);
    }

    /**
     * @param resToGive resources to be removed from the strongbox
     * @throws NotEnoughResourcesException      if there are not enough resources on the strongbox
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    public void removeResFromStrongBox(TreeMap<Resource, Integer> resToGive) throws NotEnoughResourcesException, ResourceNotDiscountableException, InvalidArgumentException {
        if (!enoughResInStrongBox(resToGive)) throw new NotEnoughResourcesException();
        removeResFromStrongBoxNoCheck(resToGive);
    }

    /**
     * advance on the faithpath of the current player
     *
     * @param steps
     * @param game
     */
    public void moveOnFaithPath(int steps, Game<?> game) throws InvalidStepsException, EndAlreadyReachedException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        this.faithtrack.move(steps, game);
    }

    @Override
    public int getVictoryPoints() {
        int points = 0;
        points += faithtrack.getVictoryPoints();
        int res = howManyResources();
        points += Math.floorDiv(res, 5);
        for (LeaderCard<? extends Requirement> lc : leaderCards) {
            if (lc.isActive())
                points += lc.getVictoryPoints();
        }
        for (DevelopCardSlot dcs : developCardSlots) {
            for (DevelopCard dc : dcs.getCards()) {
                points += dc.getVictoryPoints();
            }
        }
        return points;
    }

    /**
     * put the activated depotLeaderCard in depotLeaders array
     *
     * @param depotLeaderCard is owned by the player and it's just been activated
     */
    public void discoverDepotLeader(DepotLeaderCard depotLeaderCard) {
        depotLeaders.add(depotLeaderCard);
    }

    /**
     * put the activated productionLeaderCard in productionLeaderSlots array
     *
     * @param productionLeaderCard is owned by the player and it's just been activated
     */
    public void discoverProductionLeader(ProductionLeaderCard productionLeaderCard) {
        productionLeaderSlots.add(productionLeaderCard);
    }

    /**
     * flush resources to the strongbox. If there is FAITH in gainedResources, move on the faithTrack
     *
     * @param gainedResources are put in the strongbox
     * @param game            current game
     */
    public void flushGainedResources(TreeMap<Resource, Integer> gainedResources, Game<?> game) throws ResourceNotDiscountableException, InvalidStepsException, EndAlreadyReachedException, InvalidArgumentException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        if (gainedResources.getOrDefault(Resource.FAITH, 0) > 0)
            this.faithtrack.move(gainedResources.get(Resource.FAITH), game);
        TreeMap<Resource, Integer> c = new TreeMap<>(gainedResources);
        c.remove(Resource.FAITH);
        strongbox.addResources(c);
    }

    public TreeMap<Resource, Integer> getResourcesInStrongBox() {
        return strongbox.getResources();
    }

    /**
     * @param whichDepot number of depot to get
     * @return the resources in the specified depot
     * @throws InvalidArgumentException if whichDepot is lower than 0 or greater than 2
     */
    public TreeMap<Resource, Integer> getResInDepot(int whichDepot) throws InvalidArgumentException {
        if (whichDepot < 0 || whichDepot > 2) throw new InvalidArgumentException("The number of depot selected is invalid");
        return depots.get(whichDepot).getStoredResources();
    }

    public TreeMap<Resource, Integer> getResInNormalDepots() {
        TreeMap<Resource, Integer> inDepots = new TreeMap<>();
        for (Depot d : depots) {
            if (!d.isEmpty()) {
                inDepots.put(d.getTypeOfResource(), d.getStored());
            }
        }
        return inDepots;
    }

    /**
     * @param whichLeaderDepot number of the leader depot to get
     * @return the resources in the specified depot
     * @throws InvalidArgumentException if the parameter indicates a depot which is not in the board
     */
    public TreeMap<Resource, Integer> getResInLeaderDepot(int whichLeaderDepot) throws InvalidArgumentException {
        switch (whichLeaderDepot) {
            case 0: {
                if (depotLeaders.size() == 0) throw new InvalidArgumentException("The number of depot selected is invalid");
                return depotLeaders.get(whichLeaderDepot).getDepot().getStoredResources();
            }

            case 1: {
                if (depotLeaders.size() == 0 || depotLeaders.size() == 1) throw new InvalidArgumentException("The number of depot selected is invalid");
                return depotLeaders.get(whichLeaderDepot).getDepot().getStoredResources();
            }
            default:
                throw new InvalidArgumentException("The number of depot selected is invalid");

        }
    }

    public TreeMap<Resource, Integer> getResInLeaderDepots() {
        TreeMap<Resource, Integer> inDepots = new TreeMap<>();
        for (DepotLeaderCard dlc : depotLeaders) {
            Depot dl = dlc.getDepot();
            if (!dl.isEmpty()) {
                inDepots.put(dl.getTypeOfResource(), dl.getStored());
            }
        }
        return inDepots;
    }

    /**
     * @return list of leaderCards of the player
     */
    public ArrayList<LeaderCard<? extends Requirement>> getLeaderCards() {
        return new ArrayList<>(leaderCards);
    }

    /**
     * @param id id of the card I want to get
     * @return leaderCard with the same id
     * @throws InvalidArgumentException if there is no card with this id
     */
    public LeaderCard<? extends Requirement> getLeaderCard(int id) throws InvalidArgumentException {
        for(LeaderCard<?> i: leaderCards){
            if(i.getId() == id) return i;
        }
        throw new InvalidArgumentException("Player has no card with such id: " + id);
    }

    /**
     * @return active productionLeaderCards on the board
     */
    public ArrayList<ProductionLeaderCard> getProductionLeaders() {
        return new ArrayList<>(productionLeaderSlots);
    }

    /**
     * @return active depotLeaderCards on the board
     */
    public ArrayList<DepotLeaderCard> getDepotLeaders() {
        return new ArrayList<>(depotLeaders);
    }

    /**
     * definitely removes card from the leaderCards of the board
     *
     * @param card card to be removed from the board
     * @throws InvalidArgumentException if the cards was not contained in leaderCards of the board
     */
    public void removeLeaderCard(LeaderCard<? extends Requirement> card) throws InvalidArgumentException {
        if (!leaderCards.contains(card)) throw new InvalidArgumentException("The leader card selected is not owned by the player");
        leaderCards.remove(card);
    }

    /**
     * definitely discards card from the board
     * @param card card to discard
     * @throws InvalidArgumentException the card is not contained in the LeaderCards of the board
     */
    public void discardLeaderCard(LeaderCard<? extends Requirement> card) throws InvalidArgumentException {
        if (!leaderCards.contains(card)) throw new InvalidArgumentException("The leaderCard selected is not owned by the player");

        if(card instanceof DepotLeaderCard){
            DepotLeaderCard depotCard=(DepotLeaderCard) card;
            leaderCards.get(leaderCards.indexOf(depotCard)).discard();
            leaderCards.remove(depotCard);
            if(depotCard.isActive()) depotLeaders.remove(depotCard);
        }
        else if(card instanceof ProductionLeaderCard){
            ProductionLeaderCard prodCard=(ProductionLeaderCard) card;
            leaderCards.get(leaderCards.indexOf(prodCard)).discard();
            leaderCards.remove(prodCard);
            if(prodCard.isActive()) productionLeaderSlots.remove(prodCard);
        }
        else{
            leaderCards.get(leaderCards.indexOf(card)).discard();
            leaderCards.remove(card);
        }
    }

    /**
     * definitely removes cards from the leaderCards of the board
     *
     * @param cards cards to be removed from the board
     * @throws InvalidArgumentException if the cards was not contained in leaderCards of the board
     */
    public void removeLeaderCards(ArrayList<LeaderCard<? extends Requirement>> cards) throws InvalidArgumentException {
        if (!leaderCards.containsAll(cards)) throw new InvalidArgumentException("Some of the leaderCard selected are not owned by the player");
        leaderCards.remove(cards);
    }

    /**
     * method that puts the resources gained by the market in the depots in a smart way: before it fills the leader depots, then the normal depots.
     *
     * @param resGained res gained by the market
     * @param toKeep    choice made by the player of the resources to store in the depots
     * @param game      current game
     * @throws InvalidResourcesToKeepByPlayerException if the player made an invalid decision regarding the resources to keep,
     *                                                 the resources given must be enough to not overload the depots
     * @throws InvalidArgumentException                if the treemaps toKeep and resGained contain an irregular type of resource
     *                                                 or if the amount of resources in the toKeep are greater that un the resGained
     */
    public void gainResourcesSmart(TreeMap<Resource, Integer> resGained, TreeMap<Resource, Integer> toKeep, Game<?> game) throws InvalidResourcesToKeepByPlayerException, InvalidStepsException, EndAlreadyReachedException, InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {

        //check illegal resources in toKeep
        for (Resource r : toKeep.keySet()) {
            if (!Resource.isDiscountable(r) && r != Resource.FAITH)
                throw new InvalidArgumentException("Invalid resources to keep");
        }

        //check illegal resources in resGained and check if to keep is greater than resGained
        for (Resource r : resGained.keySet()) {
            if ((resGained.getOrDefault(r, 0) < toKeep.getOrDefault(r, 0)) || (!Resource.isDiscountable(r) && r != Resource.FAITH))
                throw new InvalidArgumentException("Invalid resources in res to gain");
        }

        //check if in toKeep there is a different type of resource than in to gain
        for(Resource r: toKeep.keySet()){
            if(!resGained.containsKey(r)) throw new InvalidArgumentException("Invalid resources to keep, given what you should gain");
        }

        if (cannotAppend(toKeep)) throw new InvalidResourcesToKeepByPlayerException("Invalid resources to keep");

        TreeMap<Resource, Integer> toGain = new TreeMap<>(toKeep);
        storeInDepotLeaderNoChecks(toGain);
        storeInNormalDepotsNoChecks(toGain, this.depots);
        if (toKeep.containsKey(Resource.FAITH))
            moveOnFaithPath(toKeep.get(Resource.FAITH), game);

        TreeMap<Resource, Integer> toDiscard = new TreeMap<>() {{
            for (Resource r : resGained.keySet()) {

                if (resGained.get(r) - toKeep.getOrDefault(r, 0) > 0)
                    put(r, resGained.get(r) - toKeep.getOrDefault(r, 0));

            }
        }};
        if (!toDiscard.isEmpty())
            distributeFaithPoints(game, toDiscard);
    }

    /**
     * method that put the resources gained by the market in the depots and if there is a discard of resources make the other players move on the faithpath
     *
     * @param resGained resources gained by the market
     * @param toKeep    resources to keep chosen by the player, the argument contains also the information about where to store the resources
     * @param game      current game
     * @throws InvalidResourcesToKeepByPlayerException if the player made an invalid decision regarding the resources to keep,
     *                                                 the resources given must be enough to not overload the depots
     * @throws InvalidArgumentException                if the treemaps toKeep and resGained contain an irregular type of resource
     *                                                 or if the amount of resources in the toKeep are greater than in the resGained
     *                                                 or if the treemap toKeep contains the strongbox as Warehouse type
     */
    public void gainResources(TreeMap<Resource, Integer> resGained, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep, Game<?> game) throws InvalidResourcesToKeepByPlayerException, InvalidStepsException, EndAlreadyReachedException, InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {

        TreeMap<Resource, Integer> entireToKeep;
        entireToKeep = Utility.getTotalResources(toKeep);
        for (Resource r : entireToKeep.keySet()) {
            if (!Resource.isDiscountable(r) && r != Resource.FAITH)
                throw new InvalidArgumentException("Invalid resources to keep");
        }

        for (Resource r : resGained.keySet()) {
            if ((resGained.getOrDefault(r, 0) < entireToKeep.getOrDefault(r, 0)) || (!Resource.isDiscountable(r) && r != Resource.FAITH))
                throw new InvalidArgumentException("Invalid resources to gain");
        }

        //check if in toKeep there is a different type of resource than in to gain
        for(Resource r: entireToKeep.keySet()){
            if(!resGained.containsKey(r)) throw new InvalidArgumentException("Invalid resources to keep, given to gain");
        }

        //before control if i can append resources to depots
        for (WarehouseType w : toKeep.keySet()) {
            switch (w) {
                case LEADER:
                    if (cannotAppendToLeaderDepots(toKeep.get(w))) throw new InvalidResourcesToKeepByPlayerException("Invalid resources to keep");
                    break;
                case NORMAL:
                    if (cannotAppendToNormalDepots(toKeep.get(w))) throw new InvalidResourcesToKeepByPlayerException("Invalid resources to keep");
                    break;
                default:
                    throw new InvalidArgumentException("Invalid warehouse type for putting resources to keep " + w.name());
            }
        }

        //then append and move on the faithpath
        int steps = resGained.getOrDefault(Resource.FAITH, 0);
        if (steps > 0)
            moveOnFaithPath(steps, game);

        for (WarehouseType w : toKeep.keySet()) {
            switch (w) {
                case LEADER:
                    storeInDepotLeaderNoChecks(toKeep.get(w));
                    break;
                case NORMAL:
                    storeInNormalDepotsNoChecks(toKeep.get(w), this.depots);
                    break;
                default:
                    throw new InvalidArgumentException("Invalid warehouse type for putting resources to keep " + w.name());
            }
        }

        //then discard the resources
        entireToKeep.remove(Resource.FAITH);
        resGained.remove(Resource.FAITH);

        TreeMap<Resource, Integer> toDiscard = new TreeMap<>() {{
            for (Resource r : resGained.keySet()) {

                if (resGained.get(r) - entireToKeep.getOrDefault(r, 0) > 0)
                    put(r, resGained.get(r) - entireToKeep.getOrDefault(r, 0));

            }
        }};

        if (!toDiscard.isEmpty())
            distributeFaithPoints(game, toDiscard);
    }


    /**
     * @param toGain resources we would like to put in the leader depots.
     * @return true, if toGain cannot completely be stored in the leader depots
     */
    public boolean cannotAppendToLeaderDepots(TreeMap<Resource, Integer> toGain) {
        return cannotAppendToLeaderDepots(toGain, new TreeMap<>());
    }

    /**
     * @param toGain resources we would like to put in the normal depots.
     * @return true, if toGain cannot completely be stored in the normal depots
     */
    public boolean cannotAppendToNormalDepots(TreeMap<Resource, Integer> toGain) throws InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        return cannotAppendToNormalDepots(toGain, new TreeMap<>());
    }

    /**
     * @param toGain resources we would like to put in the leader depots.
     * @param diff   modified inside this method. Used to store resource that I cannot gain in normal depots
     * @return true, if toGain cannot completely be stored in the leader depots
     */
    private boolean cannotAppendToLeaderDepots(TreeMap<Resource, Integer> toGain, TreeMap<Resource, Integer> diff) {
        diff.clear();
        diff.putAll(toGain);
        diff.remove(Resource.FAITH);

        for (Resource r : toGain.keySet()) {
            for (DepotLeaderCard dl : depotLeaders) {
                Depot d = dl.getDepot();
                if (d.getTypeOfResource() == r) {
                    if (diff.get(r) - d.getFreeSpace() > 0)
                        diff.replace(r, diff.get(r) - d.getFreeSpace());
                    else
                        diff.remove(r);
                }
            }
        }
        return !diff.isEmpty();
    }

    /**
     * @param toGain resources we would like to put in the normal depots.
     * @param diff   modified inside this method. Used to store resource that I cannot gain in normal depots
     * @return true, if toGain cannot completely be stored in the normal depots
     */
    private boolean cannotAppendToNormalDepots(TreeMap<Resource, Integer> toGain, TreeMap<Resource, Integer> diff) throws InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        diff.clear();
        diff.putAll(toGain);
        diff.remove(Resource.FAITH);

        ArrayList<Depot> copyOfDepots = new ArrayList<>() {{
            Depot d;
            for (int i = 0; i < 3; i++) {
                d = depots.get(i);
                add(new Depot(i + 1, true));
                if (!d.isEmpty())
                    get(i).addResource(d.getTypeOfResource(), d.getStored());
            }
        }};
        storeInNormalDepotsNoChecks(diff, copyOfDepots);
        return !diff.isEmpty();
    }

    /**
     * method that checks if the treemap toKeep is valid (it can be added to the depots without any gap of resources)
     *
     * @param toKeep a Treemap chosen by the player
     * @return true if the TreeMap passed by the player is not valid
     */
    private boolean cannotAppend(TreeMap<Resource, Integer> toKeep) throws InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        TreeMap<Resource, Integer> tmp = new TreeMap<>();
        cannotAppendToLeaderDepots(toKeep, tmp);

        TreeMap<Resource, Integer> tmp2 = new TreeMap<>();
        cannotAppendToNormalDepots(tmp, tmp2);
        return !tmp2.isEmpty();
    }

    /**
     * method that search a depot which can be substituted with the depot passed
     *
     * @param depotToSwitchFrom depot that i want to substitute
     * @param depotArrayList
     * @param howMany           resources must be added to the depot
     * @return the depot to substitute or null if no valid depot is found
     */
    private Depot lookForADepotToSwitch(Depot depotToSwitchFrom, ArrayList<Depot> depotArrayList, int howMany) {
        for (Depot d : depotArrayList) {
            if (!depotToSwitchFrom.equals(d)) {
                if ((d.getMaxToStore() >= depotToSwitchFrom.getStored() + howMany) && (d.getStored() <= depotToSwitchFrom.getMaxToStore()))
                    return d;
            }
        }
        return null;
    }

    /**
     * method that checks if there is a depot that contains r
     *
     * @param r              resource
     * @param depotArrayList arraylist of depots to analyze
     * @return true if there isn't a depot which contains r
     */
    private boolean otherDepotsNotContains(Resource r, ArrayList<Depot> depotArrayList) {
        for (Depot d : depotArrayList) {
            if (d.contains(r))
                return false;
        }
        return true;
    }

    /**
     * give one point for each resource in extraResource to each player (apart from the current player)
     *
     * @param game           current game
     * @param extraResources resources that cannot be stored in depots
     */
    private void distributeFaithPoints(Game<?> game, TreeMap<Resource, Integer> extraResources) throws InvalidStepsException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        int steps;
        steps = Utility.sumOfValues(extraResources);
        if (game instanceof MultiPlayer) {
            MultiPlayer gamemp = (MultiPlayer) game;
            Player currentPlayer = gamemp.getTurn().getCurrentPlayer();
            for (Player p : gamemp.getPlayers()) {
                if (!p.equals(currentPlayer)) {
                    try {
                        p.getBoard().moveOnFaithPath(steps, game);
                    } catch (EndAlreadyReachedException e) {
                        logger.info("Player " + p.getPlayerId() + " has already reached the end of faithTrack");
                    }
                }
            }
        }
        if (game instanceof SinglePlayer) {//only the player could call this method
            SinglePlayer gamesp = (SinglePlayer) game;
            if (!gamesp.getTurn().isLorenzoPlaying()) {
                try {
                    gamesp.getLorenzo().getFaithTrack().move(steps, game);
                } catch (EndAlreadyReachedException e) {
                    logger.info("Lorenzo " + " has already reached the end of faithTrack");
                }
            } else {
                try {
                    gamesp.getPlayer().getBoard().moveOnFaithPath(steps, game);
                } catch (EndAlreadyReachedException e) {
                    logger.info("The player " + " has already reached the end of faithTrack");
                }
            }
        }
    }

    /**
     * @param toGain         resource that we want to gain. The values in the TreeMap gets changed.
     * @param depotArrayList list of depots in which i want to store the resources
     */
    private void storeInNormalDepotsNoChecks(TreeMap<Resource, Integer> toGain, ArrayList<Depot> depotArrayList) throws InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {

        toGain.remove(Resource.FAITH);
        Set<Resource> resInToGain = new TreeSet<>(toGain.keySet());
        for (Resource r : resInToGain) {
            for (Depot d : depotArrayList) {

                if ((d.isEmpty() && (d.getFreeSpace() >= toGain.get(r)) && otherDepotsNotContains(r, depotArrayList)) ||
                        (d.contains(r) && (d.getFreeSpace() >= toGain.get(r)))) {
                    d.addResource(r, toGain.get(r));
                    toGain.remove(r);
                    break;

                } else {

                    if (d.contains(r) && d.getFreeSpace() < toGain.get(r)) {
                        Depot toSwitch = lookForADepotToSwitch(d, depotArrayList, toGain.get(r));

                        if (toSwitch != null) {
                            Depot tmp = new Depot(toSwitch.getMaxToStore(), true) {{
                                addResource(toSwitch.getTypeOfResource(), toSwitch.getStored());
                            }};
                            toSwitch.clear();
                            toSwitch.addResource(d.getTypeOfResource(), d.getStored() + toGain.get(r));
                            d.clear();
                            d.addResource(tmp.getTypeOfResource(), tmp.getStored());
                            toGain.remove(r);
                            break;

                        }
                    }
                }
            }
        }
    }


    /**
     * If possible, put some resources in depotLeader from gained. Change gained accordingly
     *
     * @param toGain resource that we want to store. The values in the TreeMap gets changed.
     */
    private void storeInDepotLeaderNoChecks(TreeMap<Resource, Integer> toGain) throws InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        toGain.remove(Resource.FAITH);
        TreeSet<Resource> resInToGain= new TreeSet<>(toGain.keySet());
        for (Resource r : resInToGain) {
            for (DepotLeaderCard dl : depotLeaders) {
                Depot d = dl.getDepot();
                if (!d.isFull() && d.getTypeOfResource() == r) {
                    while (!d.isFull() && toGain.get(r) > 0) {
                        d.addResource(r, 1);
                        toGain.replace(r, toGain.get(r) - 1);
                    }
                    if(toGain.get(r)==0)
                        toGain.remove(r);
                }
            }
        }
    }

    /**
     * Put resources from toGain in depot leaders
     *
     * @param toGain resource that we want to store. The values in the TreeMap gets changed.
     * @throws TooManyResourcesToAddException if toGain cannot be added to the depot leaders
     */
    public void storeInDepotLeader(TreeMap<Resource, Integer> toGain) throws TooManyResourcesToAddException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        if (cannotAppendToLeaderDepots(toGain)) throw new TooManyResourcesToAddException();
        storeInDepotLeaderNoChecks(toGain);
    }

    /**
     * @param toGain resource that we want to gain. The values in the TreeMap gets changed.
     * @throws TooManyResourcesToAddException if toGain cannot be added to the normal leaders
     */
    public void storeInNormalDepot(TreeMap<Resource, Integer> toGain) throws TooManyResourcesToAddException, InvalidArgumentException, InvalidTypeOfResourceToDepotException, InvalidResourceQuantityToDepotException, DifferentResourceForDepotException {
        if (cannotAppendToNormalDepots(toGain)) throw new TooManyResourcesToAddException();
        storeInNormalDepotsNoChecks(toGain, depots);
    }

    /**
     * buy the develop card on top of the deck of whichColor and whichLevel and put it in the slot indexed by slotToStore
     * it removes the resources from the board in a smart way.
     *
     * @param game        current game
     * @param whichColor  color of the develop card to buy
     * @param whichLevel  level of the develop card to buy
     * @param slotToStore slot in which to store the develop card
     * @throws EmptyDeckException                if the deckDevelop with whichColor and whichLevel in game is empty
     * @throws NotEnoughResourcesException       if this does not have enoughResources to buy the card
     * @throws InvalidDevelopCardToSlotException if the card to buy cannot go in the chosen slot
     * @throws InvalidArgumentException          if whichLevel or slotToStore are out of bound
     */
    public void buyDevelopCardSmart(Game<?> game, Color whichColor, int whichLevel, int slotToStore) throws NotEnoughResourcesException, EmptyDeckException, ResourceNotDiscountableException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidArgumentException, InvalidResourceQuantityToDepotException {
        if (slotToStore < 0 || slotToStore > 2 || whichLevel < 1 || whichLevel > 3)
            throw new InvalidArgumentException("Invalid level of develop card or slot number");

        DeckDevelop deck = game.getDecksDevelop().get(whichColor).get(whichLevel);
        if (deck.isEmpty()) throw new EmptyDeckException();

        TreeMap<Resource, Integer> resToGive = deck.topCard().getCurrentCost();
        if (!enoughResToActivate(resToGive)) throw new NotEnoughResourcesException();

        developCardSlots.get(slotToStore).addDevelopCard(deck.drawCard()); // it can throw InvalidDevelopCardToSlotException

        removeResourcesSmart(resToGive);
    }

    /**
     * buy the develop card on top of the deck of whichColor and whichLevel and put it in the slot indexed by slotToStore
     *
     * @param game        current game
     * @param whichColor  color of the develop card to buy
     * @param whichLevel  level of the develop card to buy
     * @param slotToStore slot in which to store the develop card
     * @param toPay       resources to be paid for the card. They will be removed from the board
     * @throws EmptyDeckException                if the deckDevelop with whichColor and whichLevel in game is empty
     * @throws NotEnoughResourcesException       if this does not have enoughResources to buy the card
     * @throws InvalidDevelopCardToSlotException if the card to buy cannot go in the chosen slot
     * @throws InvalidArgumentException          if whichLevel or slotToStore are out of bound or toPay is in someway invalid
     */
    public void buyDevelopCard(Game<?> game, Color whichColor, int whichLevel, int slotToStore, TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws ResourceNotDiscountableException, NotEnoughResourcesException, EmptyDeckException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidArgumentException, InvalidResourceQuantityToDepotException {
        if (slotToStore < 0 || slotToStore > 2 || whichLevel < 1 || whichLevel > 3)
            throw new InvalidArgumentException("Invalid level of develop card or slot number");

        DeckDevelop deck = game.getDecksDevelop().get(whichColor).get(whichLevel);
        if (deck.isEmpty()) throw new EmptyDeckException();

        TreeMap<Resource, Integer> resToGive = deck.topCard().getCurrentCost();

        if (!Utility.checkTreeMapEquality(Utility.getTotalResources(toPay), resToGive))
            throw new InvalidArgumentException("The resources specified to pay are different from the actual due resources");

        if (!enoughResourcesToPay(toPay)) throw new NotEnoughResourcesException();

        developCardSlots.get(slotToStore).addDevelopCard(deck.drawCard());// it can throw InvalidDevelopCardToSlotException

        payResourcesNoCheck(toPay);
    }

    /**
     * @param toPay resources to be checked
     * @return true if it would be possible to remove toPay from the board
     * @throws InvalidArgumentException if toPay contains invalid type for WarehouseType
     */
    public boolean enoughResourcesToPay(TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws ResourceNotDiscountableException, InvalidArgumentException {
        for (WarehouseType w : toPay.keySet()) {
            switch (w) {
                case STRONGBOX:
                    if (!enoughResInStrongBox(toPay.get(w))) return false;
                    break;
                case LEADER:
                    if (!enoughResInLeaderDepots(toPay.get(w))) return false;
                    break;
                case NORMAL:
                    if (!enoughResInNormalDepots(toPay.get(w))) return false;
                    break;
                default:
                    throw new InvalidArgumentException("Invalid warehouse type: " + w.name());
            }
        }
        return true;
    }

    /**
     * @param toPay resources to be removed from the board
     * @throws NotEnoughResourcesException if the board does not contains enough resources
     */
    public void payResources(TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws NotEnoughResourcesException, ResourceNotDiscountableException, InvalidArgumentException, InvalidResourceQuantityToDepotException {
        if (!enoughResourcesToPay(toPay)) throw new NotEnoughResourcesException();
        payResourcesNoCheck(toPay);
    }

    /**
     * method without any check, it just modify the board
     *
     * @param toPay resources to be removed from the board
     */
    private void payResourcesNoCheck(TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws ResourceNotDiscountableException, NotEnoughResourcesException, InvalidArgumentException, InvalidResourceQuantityToDepotException {

        // then pay
        for (WarehouseType w : toPay.keySet()) {
            switch (w) {
                case STRONGBOX:
                    removeResFromStrongBoxNoCheck(toPay.get(w));
                    break;
                case LEADER:
                    removeResFromLeaderDepotNoCheck(toPay.get(w));
                    break;
                case NORMAL:
                    removeResFromNormalDepotNoCheck(toPay.get(w));
                    break;
                default:
                    throw new InvalidArgumentException("Invalid warehouse type: " + w.name());
            }
        }
    }

    /**
     * @return number of resources gained by the player. Useful to calculate the victoryPoints
     */
    public int howManyResources() {
        int count = 0;
        for (Depot d : depots) {
            count += d.getStored();
        }
        for (DepotLeaderCard dl : depotLeaders) {
            count += dl.getDepot().getStored();
        }
        count += Utility.sumOfValues(strongbox.getResources());
        return count;
    }
}


