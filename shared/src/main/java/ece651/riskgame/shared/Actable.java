package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Implemented by World and GameInfo
 */
public abstract class Actable implements Serializable{
  protected Board board;
  protected Map<String, Clan> clans;  //the map for color of player and his clan

  public Actable(Board board, Map<String, Clan> clans) {
    this.board = board;
    this.clans = clans;
  }
  /**
   * @return Board topology relationship between territories
   */
  public Board getBoard() {
    return board;
  }
  /**
   * @return Map from color to Clan
   */
  public Map<String, Clan> getClans() {
    return clans;
  }
  /**
   * @param name of territory
   * @return Clan who occupies the territory (color)
   */
  public String getTerritoryOwnership(String name) {
    for (Map.Entry<String, Clan> clan : clans.entrySet()) {
      if (clan.getValue().occupyTerritory(name)) {
        return clan.getKey();
      }
    }
    return null;
  }
  /**
   * @return color of winner. return null if game is not end.
   */
 public String getWinner() {
    ArrayList<String> alivePlayer = new ArrayList<>();
    for(Map.Entry<String, Clan> clan : clans.entrySet()) {
      if(clan.getValue().isActive()) alivePlayer.add(clan.getKey());
    }
    if(alivePlayer.size() > 1) return null;
    else return alivePlayer.get(0);
 }
  public Map<String, Integer> getUnitMoveCost(String src, String color) {
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
  public boolean hasVisibilityOf(String color, String territoryName) throws IllegalArgumentException {
    Territory toCheck = getBoard().getTerritory(territoryName);
    //Ownership
    if (getClans().get(color).occupyTerritory(territoryName)) {
      return true;
    }
    //Adjacency
    for (Territory neighbour: getBoard().getNeighbors(toCheck)) {
      if (getClans().get(color).occupyTerritory(neighbour.getName())) {
        return true;
      }
    }
    //Spy
    if (getClans().get(color).getSpy(territoryName, false) != null) {
      return true;
    }
    //TODO:cloak
    return false;
  }
}
