package it.polimi.ingsw.model.utility;

import it.polimi.ingsw.model.game.Resource;

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
}
