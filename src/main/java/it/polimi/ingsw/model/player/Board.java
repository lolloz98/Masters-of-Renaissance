package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.model.cards.leader.*;
import it.polimi.ingsw.model.exception.InvalidProductionChosenException;
import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
import it.polimi.ingsw.model.exception.InvalidSelectionByPlayer;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * class that models the board of each player
 */
public class Board  implements VictoryPointCalculator{
    private final FaithTrack faithtrack;
    private final StrongBox strongbox;
    private final ArrayList<LeaderCard<? extends Requirement>> leaderCards;
    private final ArrayList<DevelopCardSlot> developCardSlots;
    private ArrayList<ProductionLeaderCard> productionLeaderSlots;
    private final Production normalProduction;
    private ArrayList<DepotLeaderCard> depotLeaders;
    private final ArrayList<Depot> depots;

    public Board() {
        this.faithtrack = new FaithTrack();
        this.strongbox=new StrongBox();
        leaderCards=new ArrayList<>();
        developCardSlots=new ArrayList<>();
        for(int i=0;i<3;i++)
            developCardSlots.add(new DevelopCardSlot());
        productionLeaderSlots=new ArrayList<>();
        TreeMap<Resource,Integer> resToGive=new TreeMap<>();
        TreeMap<Resource,Integer> resToGain=new TreeMap<>();
        resToGive.put(Resource.ANYTHING,2);
        resToGain.put(Resource.ANYTHING,1);
        normalProduction= new Production(resToGive,resToGain);
        depotLeaders=new ArrayList<>();
        depots= new ArrayList<>();
        for(int i=0;i<3;i++)
            depots.add(new Depot(i+1,true));
    }

    public ArrayList<DevelopCardSlot> getDevelopCardSlots() {
        ArrayList<DevelopCardSlot> copy=new ArrayList<>();
        copy.addAll(developCardSlots);
        return copy;
    }

    public FaithTrack getFaithtrack() {
        return faithtrack;
    }

    public void addLeaderCards(ArrayList<LeaderCard> l) {

    }

    /**
     * method that adds a LeaderProduction to the board
     */
    public void addProductionLeader(ProductionLeaderCard c){
        productionLeaderSlots.add(c);
    }

    /**
    *method that handle the apply of the production chosen
    *@param resToGive and resToGain are given by the player with the hypothesis that (@requires) they don't contain "ANYTHING" type resources
    *@param whichprod if zero it refers to the normalProduction, if 1,2 or 3 it refers to which productionSlot,
    *if 4 or 5 it refers to the leaderCardProductionSlot.
     */

   public void activateProduction(int whichprod, TreeMap<Resource,Integer> resToGive, TreeMap<Resource,Integer> resToGain) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if(whichprod<0||whichprod>developCardSlots.size()+productionLeaderSlots.size()) throw new InvalidProductionChosenException();
       if(resToGive.containsKey(Resource.ANYTHING)) throw new InvalidSelectionByPlayer();
       if(resToGain.containsKey(Resource.ANYTHING)) throw new InvalidSelectionByPlayer();
        if(enoughResToActivate(resToGive)) {
            if (whichprod==0) {
                normalProduction.applyProduction(resToGive,resToGain,this);
            }
            if(whichprod>0&&whichprod<=3)
            developCardSlots.get(whichprod-1).applyProduction(resToGive,resToGain,this);
            if(whichprod>=4){ //branch taken if the production choosen is a LeaderProduction
                if(!theLeaderProductionIsActivated(whichprod-4)) throw new InvalidProductionChosenException();
                productionLeaderSlots.get(whichprod-4).getProduction().applyProduction(resToGive,resToGain,this);
            }
        }
    }

    /**
    *@requires a TreeMap with only discountable resources
    *method that checks if i have enough resources to activate the production
    *the idea is to copy @param resToGive into diffMap and foreach resource of that type that i have in the Depot/LeaderDepot/StrongBox
    *subtract to diff map the value associated to the resource r. if at the end every value of the diffmap is less or equal
    *to zero @return true
     */
    public boolean enoughResToActivate(TreeMap<Resource,Integer> resToGive){
        if(resToGive.containsKey(Resource.ANYTHING)) throw new InvalidSelectionByPlayer();
       TreeMap<Resource,Integer> diffMap=new TreeMap<>();
       diffMap.putAll(resToGive);
       for(Resource r: diffMap.keySet()){
           for(Depot d: depots){
               if(d.contains(r))
                   diffMap.replace(r,diffMap.get(r)-d.getStored());
           }
           for(DepotLeaderCard dl:depotLeaders){
               if(dl.getDepot().contains(r))
                   diffMap.replace(r,diffMap.get(r)-dl.getDepot().getStored());
           }
           for(Resource inStrongBox: strongbox.getResources().keySet()){
               if(inStrongBox.equals(r))
                   diffMap.replace(r,diffMap.get(r)-strongbox.getResources().get(inStrongBox));
           }
       }
       for(Resource r: diffMap.keySet()){
           if(diffMap.get(r)>0)
               return false;
       }
       return true;
    }


    private boolean theLeaderProductionIsActivated(int whichLeader){
       if(productionLeaderSlots.size()<whichLeader)
       return productionLeaderSlots.get(whichLeader).isActive();
       return false;
    }
/**
*@requires the board has enough resToGive
*method that takes in input a @param TreeMap of resources to give and remove these from the board.
*it gives the priority to the normal Depots, than it removes resources from LeaderDepots and at the end it removes the res
*from the strongbox
 */
    public void removeResources(TreeMap<Resource, Integer> resToGive){
        int tmp;
        TreeMap<Resource,Integer> resToSpend=new TreeMap<>();
        resToSpend.putAll(resToGive);
        for(Resource r:resToSpend.keySet()){
            for(Depot d: depots) {
                if (d.contains(r)) {
                    if (!d.enoughResources(resToSpend.get(r))) {
                        tmp = d.clear();
                        resToSpend.replace(r, resToSpend.get(r) - tmp);
                    } else {
                        d.spendResources(resToSpend.get(r));
                        resToSpend.replace(r,0);
                    }
                }
            }
            if(resToSpend.get(r)!=0){
                for(DepotLeaderCard dl: depotLeaders){
                    if(dl.getDepot().contains(r)){
                        if (!dl.getDepot().enoughResources(resToSpend.get(r))) {
                            tmp = dl.getDepot().clear();
                            resToSpend.replace(r, resToSpend.get(r) - tmp);
                        } else {
                            dl.getDepot().spendResources(resToSpend.get(r));
                            resToSpend.replace(r,0);
                        }
                    }
                }
            }
        }
        strongbox.spendResources(resToSpend);
   }

   //TODO:
   //public void moveOnFaithPath(){}

//TODO:
    @Override
    public int getVictoryPoints() {
        return 0;
    }
}


