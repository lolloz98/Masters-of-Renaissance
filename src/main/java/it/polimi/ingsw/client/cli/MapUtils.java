package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;

import java.util.TreeMap;

/**
 * helper class to handle TreeMaps in flush resources view
 */
public class MapUtils {

    /**
     * @return if the map is empty
     */
    public static boolean isMapEmpty(TreeMap<Resource, Integer> map) {
        int sum = 0;
        for (Resource r : map.keySet()) {
            sum += map.get(r);
        }
        return sum == 0;
    }

    /**
     * adds a resource to a treemap
     */
    public static void addToResMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        if (resMap.containsKey(resource)) {
            resMap.replace(resource, 1 + resMap.get(resource));
        } else {
            resMap.put(resource, 1);
        }
    }

    /**
     * adds a resource to a treemap with information about the warehouse
     *
     * @param resMap the map to which the resource must be added
     * @param resource the resource to add
     * @param warehouseType the warehouse to which the resource must be added
     */
    public static void addToResMapWarehouse(TreeMap<WarehouseType, TreeMap<Resource, Integer>> resMap, Resource resource, WarehouseType warehouseType) {
        if (!resMap.containsKey(warehouseType)) {
            resMap.put(warehouseType, new TreeMap<>());
        }
        addToResMap(resMap.get(warehouseType), resource);
    }

    /**
     * removes a resource from a map
     */
    public static void removeResFromMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        resMap.replace(resource, resMap.get(resource) - 1);
        if (resMap.get(resource) == 0) {
            resMap.remove(resource);
        }
    }
}
