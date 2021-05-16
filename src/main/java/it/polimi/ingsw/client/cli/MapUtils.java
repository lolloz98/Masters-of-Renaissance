package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;

import java.util.TreeMap;

public class MapUtils {

    public static boolean isMapEmpty(TreeMap<Resource, Integer> map) {
        int sum = 0;
        for (Resource r : map.keySet()) {
            sum += map.get(r);
        }
        return sum == 0;
    }

    public static void addToResMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        if (resMap.containsKey(resource)) {
            resMap.replace(resource, 1 + resMap.get(resource));
        } else {
            resMap.put(resource, 1);
        }
    }

    public static void addToResMapWarehouse(TreeMap<WarehouseType, TreeMap<Resource, Integer>> resMap, Resource resource, WarehouseType warehouseType) {
        if (!resMap.containsKey(warehouseType)) {
            resMap.put(warehouseType, new TreeMap<>());
        }
        addToResMap(resMap.get(warehouseType), resource);
    }

    public static void removeResFromMap(TreeMap<Resource, Integer> resMap, Resource resource) {
        resMap.replace(resource, resMap.get(resource) - 1);
        if (resMap.get(resource) == 0) {
            resMap.remove(resource);
        }
    }
}
