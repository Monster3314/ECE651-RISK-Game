package ece651.riskgame.shared;

import java.util.*;

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
        Board board = getBoard();
        Territory t = board.getTerritory(src);
        int cost = t.getSize();
        if (!board.containsTerritory(src) || !getTerritoryOwnership(src).equals(color)) {
            throw new IllegalArgumentException("Source territory not found or not belonging to this clan.");
        }
        ret.put(src, cost);
        PriorityQueue<Map.Entry<String, Integer>> treeMap = new PriorityQueue<>(Comparator.comparingInt(entry -> board.getTerritory(entry.getKey()).getSize()));
        HashMap<String, Integer> distance = new HashMap<>();
        for (Territory territory : board.getNeighbors(t)) {
            if (getTerritoryOwnership(territory.getName()).equals(color)) {
                treeMap.add(Map.entry(territory.getName(), cost + territory.getSize()));
                distance.put(territory.getName(), cost + territory.getSize());
            }
        }
        while (!treeMap.isEmpty()) {
            Map.Entry<String, Integer> entry = treeMap.poll();
            cost = entry.getValue();
            ret.put(entry.getKey(), cost);
            for (Territory territory : board.getNeighbors(board.getTerritory(entry.getKey()))) {
                if (!ret.containsKey(territory.getName()) && getTerritoryOwnership(territory.getName()).equals(color)) {
                    Integer value = distance.get(territory.getName());
                    int newCost = cost + territory.getSize();
                    int newValue = value == null ? newCost : Math.min(newCost, value);
                    treeMap.add(Map.entry(territory.getName(), newValue));
                    distance.put(territory.getName(), newValue);
                }
            }
        }
        ret.remove(src);
        return ret;
    }
}
