package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedDiscountForResException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.enums.Resource;

import java.util.TreeMap;

public class DevelopCard implements Card, VictoryPointCalculator {
    private static final long serialVersionUID = 1014L;

    private final int id;
    /**
     * level must be between 1 and 3
     */
    private final int level;
    private final Color color;
    private final int victoryPoints;
    private final TreeMap<Resource, Integer> cost;
    private final TreeMap<Resource, Integer> discountApplied;
    private final Production production;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card){
            Card t = (Card) obj;
            return id == t.getId();
        }
        return false;
    }

    public DevelopCard(TreeMap<Resource, Integer> cost, Production production, Color color, int level, int victoryPoints, int id) {
        this.color = color;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.discountApplied = new TreeMap<>();
        this.cost = new TreeMap<>(cost);
        this.production = production;
        this.id = id;
    }

    public TreeMap<Resource, Integer> getCost() {
        return new TreeMap<Resource,Integer>(cost);
    }

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * Reduce the cost of the card
     *
     * @param r        resource to discount
     * @param quantity how many r to discount
     * @throws ResourceNotDiscountableException      if r is notDiscountable
     * @throws AlreadyAppliedDiscountForResException if a discount has already been applied on that resource
     */
    public void applyDiscount(Resource r, int quantity) throws AlreadyAppliedDiscountForResException, ResourceNotDiscountableException {
        if (!Resource.isDiscountable(r)) throw new ResourceNotDiscountableException();
        if (cost.containsKey(r)) {
            if (discountApplied.containsKey(r)) throw new AlreadyAppliedDiscountForResException(r);
            if(cost.get(r)-quantity>=0)
                cost.replace(r, cost.get(r) - quantity);
            else
                cost.replace(r, 0);
            discountApplied.put(r, quantity);
        }
    }

    /**
     * @return cost of the card (if discounted get the discounted cost)
     */
    public TreeMap<Resource, Integer> getCurrentCost() {
        return new TreeMap<>(cost);
    }

    /**
     * @return true if any discount is now applied
     */
    public boolean isDiscounted() {
        return !discountApplied.isEmpty();
    }

    /**
     * remove all discounts and restore the original cost of the card
     */
    public void removeDiscounts() {
        for (Resource r : discountApplied.keySet()) {
            // no check on cost.get(r): if I have applied the discount -> cost.get(r) != null
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

    /**
     * remove the discount on a specific res if present
     *
     * @param res resource on which to remove the discount
     */
    public void removeDiscount(Resource res) {
        if (discountApplied.getOrDefault(res, 0) != 0)
            cost.replace(res, discountApplied.get(res) + cost.get(res));
        discountApplied.remove(res);
    }
}
