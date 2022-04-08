package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {
  private static final long serialVersionUID = -5196897839043527760L;
  private HashMap<Territory, List<Territory>> adjacency;
  private Map<String, Territory> territories;
  
  public Board() {
    adjacency = new HashMap<Territory, List<Territory>>();
    territories = new LinkedHashMap<>();
  }

  /**
   * @param neighbors List of territories neighboring the @param territory
   */
  public void putEntry(Territory territory, List<Territory> neighbors) {
    adjacency.put(territory, neighbors);
  }

  public List<Territory> getNeighbors(Territory territory) {
    return adjacency.get(territory);
  }

  public void addTerritory(Territory t) {
    territories.put(t.getName(), t);
    adjacency.put(t, new LinkedList<>());
  }

  public Set<Territory> getTerritoriesSet() {
    return adjacency.keySet();
  }
  
  public List<Territory> getTerritoriesList() {
    return new ArrayList<Territory>(territories.values());
  }

  public Collection<String> getTerritoryNames() {
    return territories.keySet();
  }

  public boolean containsTerritory(String name) {
    return territories.containsKey(name);
  }
  
  /**
   * Get territory by its name
   * @throws IllegalArgumentException if no territory in list has the name
   */
  public Territory getTerritory(String name) {
    if (territories.containsKey(name)) {
      return territories.get(name);
    }
    throw new IllegalArgumentException("Territory name not found.");
  }

  public Map<String, Integer> getUnitMoveCost(String src) {
    Map<String, Integer> ret = new HashMap<>();
    Territory t = getTerritory(src);
    int cost = t.getSize();
    if (!territories.containsKey(src)) {
      throw new IllegalArgumentException("Source territory not found.");
    }
    ret.put(src, cost);
    TreeMap<Territory, Integer> treeMap = new TreeMap<>(Comparator.comparingInt(Territory::getSize));
    for (Territory territory : getNeighbors(t)) {
      treeMap.put(territory, cost + territory.getSize());
    }
    while (!treeMap.isEmpty()) {
      Map.Entry<Territory, Integer> entry = treeMap.pollFirstEntry();
      cost = entry.getValue();
      ret.put(entry.getKey().getName(), cost);
      for (Territory territory : getNeighbors(entry.getKey())) {
        if (!ret.containsKey(territory.getName())) {
          Integer value = treeMap.get(territory);
          int newCost = cost + territory.getSize();
          treeMap.put(territory, value == null ? newCost : Math.min(newCost, value));
        }
      }
    }
    ret.remove(src);
    return ret;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Board board = (Board) o;
    // compare list
    List<Territory> myList = this.getTerritoriesList();
    List<Territory> otherList = board.getTerritoriesList();
    if(myList.size() != otherList.size()) {
      return false;
    }
    for (int i = 0; i < myList.size(); i++) {
      if (!myList.get(i).equals(otherList.get(i))) {
        return false;
      }
    }

    // compare adjacency
    Set<Territory> mySet = this.getTerritoriesSet();
    Set<Territory> otherSet = board.getTerritoriesSet();
    if (mySet.size() != otherSet.size()) {
      return false;
    }
    Iterator ot = otherSet.iterator();
    for (Territory myt: mySet) {
      List<Territory> myNs = this.getNeighbors(myt);
      List<Territory> otherNs = board.getNeighbors((Territory)ot.next());
      if (myNs.size() != otherNs.size()) {
        return false;
      }
      for (int i = 0; i < myNs.size(); i++) {
        if (!myNs.get(i).equals(otherNs.get(i))) {
          return false;
        }
      }
    }

    return true;
  }
}
