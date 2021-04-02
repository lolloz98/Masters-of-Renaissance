package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.AlreadyAppliedDiscountForResException;
import it.polimi.ingsw.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.player.Board;

import java.util.TreeMap;

public class DevelopCard implements Card, VictoryPointCalculator{
    private final int id;
    private final int level;
    private final Color color;
    private final int victoryPoints;
    private final TreeMap<Resource, Integer> cost;
    private final TreeMap<Resource, Integer> discountApplied;
    private final Production production;

    public DevelopCard(TreeMap<Resource, Integer> cost, Production production, Color color, int level, int victoryPoints, int id){
        this.color = color;
        this.level = level;
        this. victoryPoints = victoryPoints;
        this.discountApplied = new TreeMap<>();
        this.cost = new TreeMap<>();
        this.cost.putAll(cost);
        this.production = production;
        this.id = id;
    }

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    @Override
    public int getId(){
        return id;
    }

    /**
     * Reduce the cost of the card
     *
     * @param r resource to discount
     * @param quantity how many r to discount
     * @throws ResourceNotDiscountableException if r is notDiscountable
     * @throws AlreadyAppliedDiscountForResException if a discount has already been applied on that resource
     */
    public void applyDiscount(Resource r, int quantity) {
        if(!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
        if(cost.containsKey(r)){
            if(discountApplied.containsKey(r)) throw new AlreadyAppliedDiscountForResException();
            cost.replace(r, cost.get(r) - quantity);
            discountApplied.put(r, quantity);
        }
    }

    /**
     * @return cost of the card (if discounted get the discounted cost)
     */
    public TreeMap<Resource, Integer> getCurrentCost(){
        TreeMap<Resource, Integer> copyCost = new TreeMap<>();
        copyCost.putAll(cost);
        return copyCost;
    }

    /**
     * @return true if any discount is now applied
     */
    public boolean isDiscounted(){
        return !discountApplied.isEmpty();
    }

    /**
     * remove all discounts and restore the original cost of the card
     */
    public void removeDiscounts(){
        for(Resource r: discountApplied.keySet()){
            cost.replace(r, discountApplied.get(r) + cost.get(r));
        }
        discountApplied.clear();
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public Production getProduction() {
        return production;
    }
}
