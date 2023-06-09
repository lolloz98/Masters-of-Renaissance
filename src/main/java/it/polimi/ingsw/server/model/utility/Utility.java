package it.polimi.ingsw.server.model.utility;

import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * class with useful static methods
 */
public class Utility {
    /**
     * @param t1 treeMap 1
     * @param t2 treeMap 2
     * @return true if the treeMap have the same amount of resources. If one of the tree map contains
     * 0 resources of a certain type and the other does not have the key of the resource, it is still equal
     */
    public static boolean checkTreeMapEquality(TreeMap<Resource, Integer> t1, TreeMap<Resource, Integer> t2){
        Set<Resource> s = new TreeSet<>(t1.keySet());
        s.addAll(t2.keySet());
        for(Resource r: s){
            if(!t1.getOrDefault(r, 0).equals(t2.getOrDefault(r, 0))) return false;
        }
        return true;
    }

    public static int sumOfValues(TreeMap<Resource,Integer> treeMap){
        int sum=0;
        for(Resource r: treeMap.keySet()){
            sum+=treeMap.get(r);
        }
        return sum;
    }

    /**
     * @param toPay TreeMap<WarehouseType, TreeMap<Resource, Integer>>
     * @return TreeMap<Resource, Integer> with all the resources in toPay, regardless of the type of the Warehouse
     */
    public static TreeMap<Resource, Integer> getTotalResources(TreeMap<WarehouseType, TreeMap<Resource, Integer>> toPay) throws InvalidArgumentException {
        TreeMap<Resource, Integer> res = new TreeMap<>();
        for (WarehouseType w : toPay.keySet()) {
            switch (w) {
                case STRONGBOX:
                case LEADER:
                case NORMAL:
                    addResToTreeMap(toPay.get(w), res);
                    break;
                default:
                    throw new InvalidArgumentException("Warehouse type specified, invalid");
            }
        }
        return res;
    }

    /**
     * take resources from toBeAdded and add them to whereToAdd (where to add is modified in this method)
     */
    private static void addResToTreeMap(TreeMap<Resource, Integer> toBeAdded, TreeMap<Resource, Integer> whereToAdd){
        for(Resource r: toBeAdded.keySet()){
            if(whereToAdd.containsKey(r)){
                whereToAdd.replace(r, whereToAdd.get(r) + toBeAdded.get(r));
            }else{
                whereToAdd.put(r, toBeAdded.get(r));
            }
        }
    }
}
