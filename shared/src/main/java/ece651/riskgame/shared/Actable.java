package ece651.riskgame.shared;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implemented by World and GameInfo
 */
public interface Actable {

    /**
     * @return Board topology relationship between territories
     */
    Board getBoard();

    /**
     * @return Map from color to Clan
     */
    Map<String, Clan> getClans();

    /**
     * @param name of territory
     * @return Clan who occupies the territory (color)
     */
    String getTerritoryOwnership(String name);

    default Map<String, Integer> getUnitMoveCost(String src, String color) {
        Map<String, Integer> ret = new HashMap<>();
        Territory t = getBoard().getTerritory(src);
        int cost = t.getSize();
        if (!getBoard().containsTerritory(src) || !getTerritoryOwnership(src).equals(color)) {
            throw new IllegalArgumentException("Source territory not found or not belonging to this clan.");
        }
        ret.put(src, cost);
        TreeMap<Territory, Integer> treeMap = new TreeMap<>(Comparator.comparingInt(Territory::getSize));
        for (Territory territory : getBoard().getNeighbors(t)) {
            if (getTerritoryOwnership(territory.getName()).equals(color)) {
                treeMap.put(territory, cost + territory.getSize());
            }
        }
        while (!treeMap.isEmpty()) {
            Map.Entry<Territory, Integer> entry = treeMap.pollFirstEntry();
            cost = entry.getValue();
            ret.put(entry.getKey().getName(), cost);
            for (Territory territory : getBoard().getNeighbors(entry.getKey())) {
                if (!ret.containsKey(territory.getName()) && getTerritoryOwnership(territory.getName()).equals(color)) {
                    Integer value = treeMap.get(territory);
                    int newCost = cost + territory.getSize();
                    treeMap.put(territory, value == null ? newCost : Math.min(newCost, value));
                }
            }
        }
        ret.remove(src);
        return ret;
    }
}
