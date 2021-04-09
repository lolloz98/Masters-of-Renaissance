package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.leader.*;
import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiPlayer;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.utility.Utility;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * class that models the board of each player
 */
public class Board implements VictoryPointCalculator {
    private final FaithTrack faithtrack;
    private final StrongBox strongbox;
    private final ArrayList<LeaderCard<? extends Requirement>> leaderCards;
    private final ArrayList<DevelopCardSlot> developCardSlots;
    private final ArrayList<ProductionLeaderCard> productionLeaderSlots;
    private final Production normalProduction;
    private final ArrayList<DepotLeaderCard> depotLeaders;
    private final ArrayList<Depot> depots;

    public Board() {
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
        depotLeaders = new ArrayList<>();
        depots = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            depots.add(new Depot(i + 1, true));
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
     * method that handle the apply of the production chosen
     *
     * @param resToGive and resToGain are given by the player with the hypothesis that (@requires) they don't contain "ANYTHING" type resources
     * @param whichprod if zero it refers to the normalProduction, if 1,2 or 3 it refers to which productionSlot,
     *                  if 4 or 5 it refers to the leaderCardProductionSlot.
     */
    public void activateProduction(int whichprod, TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if (whichprod < 0 || whichprod > developCardSlots.size() + productionLeaderSlots.size())
            throw new InvalidProductionChosenException();
        if (resToGive.containsKey(Resource.ANYTHING)) throw new InvalidSelectionByPlayer();
        if (resToGain.containsKey(Resource.ANYTHING)) throw new InvalidSelectionByPlayer();
        if (enoughResToActivate(resToGive)) {
            if (whichprod == 0) {
                normalProduction.applyProduction(resToGive, resToGain, this);
            }
            if (whichprod > 0 && whichprod <= 3)
                developCardSlots.get(whichprod - 1).applyProduction(resToGive, resToGain, this);
            if (whichprod >= 4) { //branch taken if the production choosen is a LeaderProduction
                if (!theLeaderProductionIsActivated(whichprod - 4)) throw new InvalidProductionChosenException();
                productionLeaderSlots.get(whichprod - 4).getProduction().applyProduction(resToGive, resToGain, this);
            }
        }
    }

    /**
     * checks if i have enough resources compared to resToGive
     *
     * @param resToGive to be checked if in board there are at least this amount of resources
     * @return true if there are enough resources on the board, false otherwise
     * @throws ResourceNotDiscountableException if resToGive contains any resource notDiscountable
     */
    public boolean enoughResToActivate(TreeMap<Resource, Integer> resToGive) {
        TreeMap<Resource, Integer> diffMap = new TreeMap<>();
        diffMap.putAll(resToGive);
        for (Resource r : diffMap.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (Depot d : depots) {
                if (d.contains(r))
                    diffMap.replace(r, diffMap.get(r) - d.getStored());
            }
            for (DepotLeaderCard dl : depotLeaders) {
                if (dl.getDepot().contains(r))
                    diffMap.replace(r, diffMap.get(r) - dl.getDepot().getStored());
            }
            for (Resource inStrongBox : strongbox.getResources().keySet()) {
                if (inStrongBox.equals(r))
                    diffMap.replace(r, diffMap.get(r) - strongbox.getResources().get(inStrongBox));
            }
        }
        for (Resource r : diffMap.keySet()) {
            if (diffMap.get(r) > 0)
                return false;
        }
        return true;
    }


    private boolean theLeaderProductionIsActivated(int whichLeader) {
        if (whichLeader < productionLeaderSlots.size() && whichLeader >= 0)
            return productionLeaderSlots.get(whichLeader).isActive();
        return false;
    }

    /**
     * remove resToGive from the board.
     * It removes the resources in this order: from the normal Depots, from LeaderDepots (if any) and from the strongbox
     *
     * @param resToGive resources to be removed from the board
     * @throws NotEnoughResourcesException      if there are not enough resources on the board
     * @throws ResourceNotDiscountableException if there are any resources which are not discountable in resToGive
     */
    public void removeResources(TreeMap<Resource, Integer> resToGive) {
        if (!enoughResToActivate(resToGive)) throw new NotEnoughResourcesException();
        int tmp;
        TreeMap<Resource, Integer> resToSpend = new TreeMap<>();
        resToSpend.putAll(resToGive);
        for (Resource r : resToSpend.keySet()) {
            if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
            for (Depot d : depots) {
                if (d.contains(r)) {
                    if (!d.enoughResources(resToSpend.get(r))) {
                        tmp = d.clear();
                        resToSpend.replace(r, resToSpend.get(r) - tmp);
                    } else {
                        d.spendResources(resToSpend.get(r));
                        resToSpend.replace(r, 0);
                    }
                }
            }
            if (resToSpend.get(r) != 0) {
                for (DepotLeaderCard dl : depotLeaders) {
                    if (dl.getDepot().contains(r)) {
                        if (!dl.getDepot().enoughResources(resToSpend.get(r))) {
                            tmp = dl.getDepot().clear();
                            resToSpend.replace(r, resToSpend.get(r) - tmp);
                        } else {
                            dl.getDepot().spendResources(resToSpend.get(r));
                            resToSpend.replace(r, 0);
                        }
                    }
                }
            }
        }
        strongbox.spendResources(resToSpend);
    }

    public void moveOnFaithPath(int steps, Game<?> game) {
        this.faithtrack.move(steps, game);
    }

    @Override
    public int getVictoryPoints() {
        int points = 0;
        points += faithtrack.getVictoryPoints();
        int res = howManyResources();
        points += Math.floorDiv(res, 5);
        for (LeaderCard lc : leaderCards) {
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
    public void flushGainedResources(TreeMap<Resource, Integer> gainedResources, Game<?> game) {
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
     * @throws IllegalArgumentException if whichDepot is lower than 0 or greater than 2
     */
    public TreeMap<Resource, Integer> getResInDepot(int whichDepot) {
        if (whichDepot < 0 || whichDepot > 2) throw new IllegalArgumentException();
        return depots.get(whichDepot).getStoredResources();
    }

    /**
     * @return list of leaderCards of the player
     */
    public ArrayList<LeaderCard<? extends Requirement>> getLeaderCards() {
        return new ArrayList<>(leaderCards);
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
     * @throws IllegalArgumentException if the cards was not contained in leaderCards of the board
     */
    public void removeLeaderCard(LeaderCard<? extends Requirement> card) {
        if (!leaderCards.contains(card)) throw new IllegalArgumentException();
        leaderCards.remove(card);
    }

    /**
     * @param resGained
     * @param toKeep
     * @param game
     * @throws InvalidResourcesToKeepByPlayerException
     * @throws IllegalArgumentException
     */
    public void gainResources(TreeMap<Resource, Integer> resGained, TreeMap<Resource, Integer> toKeep, Game<?> game) throws InvalidResourcesToKeepByPlayerException {
        for (Resource r : toKeep.keySet()) {
            if (!Resource.isDiscountable(r) && r != Resource.FAITH)
                throw new IllegalArgumentException();
        }
        for (Resource r : resGained.keySet()) {
            if ((resGained.getOrDefault(r, 0) < toKeep.getOrDefault(r, 0)) || (!Resource.isDiscountable(r) && r != Resource.FAITH))
                throw new IllegalArgumentException();
        }
        if (cannotAppend(toKeep)) throw new InvalidResourcesToKeepByPlayerException();
        TreeMap<Resource, Integer> toGain = new TreeMap<>(toKeep);
        storeInDepotLeader(toGain);
        storeInNormalDepots(toGain, this.depots);
        moveOnFaithPath(toKeep.get(Resource.FAITH), game);
        TreeMap<Resource, Integer> toDiscard = new TreeMap<>() {{
            for (Resource r : resGained.keySet()) {
                put(r, resGained.get(r) - toKeep.getOrDefault(r, 0));
            }
        }};
        distributeFaithPoints(game, toDiscard);
    }

    /**
     * method that checks if the treemap toKeep is valid (it can be added to the depots without any gap of resources)
     *
     * @param toKeep a Treemap chosen by the player
     * @return true if the TreeMap passed by the player is not valid
     */
    private boolean cannotAppend(TreeMap<Resource, Integer> toKeep) {
        TreeMap<Resource, Integer> toGain = new TreeMap<>(toKeep);
        toGain.remove(Resource.ANYTHING);
        for (Resource r : toGain.keySet()) {
            for (DepotLeaderCard dl : depotLeaders) {
                Depot d = dl.getDepot();
                if (d.getTypeOfResource() == r) {
                    if (toGain.get(r) - d.getFreeSpace() > 0)
                        toGain.replace(r, toGain.get(r) - d.getFreeSpace());
                    else
                        toGain.remove(r);
                }
            }
        }
        ArrayList<Depot> copyOfDepots = new ArrayList<>() {{
            Depot d;
            for (int i = 0; i < 3; i++) {
                d = depots.get(i);
                add(new Depot(i + 1, true));
                get(i).addResource(d.getTypeOfResource(), d.getStored());
            }
        }};
        storeInNormalDepots(toGain, copyOfDepots);
        if (toGain.isEmpty())
            return false;
        else
            return true;
    }

    /**
     * method that search a depot which can be substituted with the depot passed
     *
     * @param depotToSwitchFrom depot that i want to substitute
     * @param depotArrayList
     * @param howMany           resources must be added to the depot
     * @return the depot to substitute
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
    private void distributeFaithPoints(Game<?> game, TreeMap<Resource, Integer> extraResources) {
        int steps;
        steps = Utility.sumOfValues(extraResources);
        if (game instanceof MultiPlayer) {
            MultiPlayer gamemp = (MultiPlayer) game;
            Player currentPlayer = gamemp.getTurn().getCurrentPlayer();
            for (Player p : gamemp.getPlayers()) {
                if (!p.equals(currentPlayer))
                    p.getBoard().moveOnFaithPath(steps, game);
            }
        }
        if (game instanceof SinglePlayer) {//only the player could call this method
            SinglePlayer gamesp = (SinglePlayer) game;
            if (!gamesp.getTurn().isLorenzoPlaying()) {
                gamesp.getLorenzo().getFaithTrack().move(steps, game);
            } else
                gamesp.getPlayer().getBoard().moveOnFaithPath(steps, game);
        }
    }

    /**
     * @param toGain         resource that we want to gain. The values in the TreeMap gets changed.
     * @param depotArrayList list of depots in which i want to store the resources
     */
    private void storeInNormalDepots(TreeMap<Resource, Integer> toGain, ArrayList<Depot> depotArrayList) {
        for (Resource r : toGain.keySet()) {
            for (Depot d : depotArrayList) {
                if ((d.isEmpty() && (d.getFreeSpace() >= toGain.get(r)) && otherDepotsNotContains(r, depotArrayList)) ||
                        (d.contains(r) && (d.getFreeSpace() >= toGain.get(r)))) {
                    d.addResource(r, toGain.get(r));
                    toGain.remove(r);
                } else {
                    if (d.contains(r) && d.getFreeSpace() < toGain.get(r)) {
                        Depot toSwitch = lookForADepotToSwitch(d, depotArrayList, toGain.get(r));
                        if (toSwitch != null) {
                            Depot tmp = new Depot(toSwitch.getMaxToStore(), true) {{
                                addResource(toSwitch.getTypeOfResource(), toSwitch.getStored());
                            }};
                            toSwitch.clear();
                            toSwitch.addResource(d.getTypeOfResource(), d.getStored() + toGain.get(r));
                            d.addResource(tmp.getTypeOfResource(), tmp.getStored());
                            toGain.remove(r);
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
    private void storeInDepotLeader(TreeMap<Resource, Integer> toGain) {
        for (Resource r : toGain.keySet()) {
            for (DepotLeaderCard dl : depotLeaders) {
                Depot d = dl.getDepot();
                if (!d.isFull() && d.getTypeOfResource() == r) {
                    while (!d.isFull() || toGain.get(r) == 0) {
                        d.addResource(r, 1);
                        toGain.replace(r, toGain.get(r) - 1);
                    }
                }
            }
        }
    }

    /**
     * buy the develop card on top of the deck of whichColor and whichLevel and put it in the slot indexed by slotToStore
     *
     * @param game        current game
     * @param whichColor  color of the develop card to buy
     * @param whichLevel  level of the develop card to buy
     * @param slotToStore slot in which to store the develop card
     * @throws EmptyDeckException                if the deckDevelop with whichColor and whichLevel in game is empty
     * @throws NotEnoughResourcesException       if this does not have enoughResources to buy the card
     * @throws InvalidDevelopCardToSlotException if the card to buy cannot go in the chosen slot
     * @throws IllegalArgumentException          if whichLevel or slotToStore are out of bound
     */
    public void buyDevelopCard(Game<?> game, Color whichColor, int whichLevel, int slotToStore) {
        if (slotToStore < 0 || slotToStore > 2 || whichLevel < 1 || whichLevel > 3)
            throw new IllegalArgumentException();

        // think about checking for the color in the method below
        DeckDevelop deck = game.getDecksDevelop().get(whichColor).get(whichLevel);
        if (deck.isEmpty()) throw new EmptyDeckException();

        TreeMap<Resource, Integer> resToGive = deck.topCard().getCurrentCost();
        if (!enoughResToActivate(resToGive)) throw new NotEnoughResourcesException();

        developCardSlots.get(slotToStore).addDevelopCard(deck.drawCard()); // it can throw InvalidDevelopCardToSlotException

        removeResources(resToGive);
    }

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


