package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {
     private HashMap<Territory, LinkedList<Territory>> adjacency;
     private List<Territory> territories;

     public Board() {
         adjacency = new HashMap<Territory, LinkedList<Territory>>();
         territories = new ArrayList<>();
     }

    public void putEntry(Territory territory, LinkedList<Territory> neighbors) {
         adjacency.put(territory, neighbors);
    }

    public LinkedList<Territory> getNeighbors(Territory territory) {
         return adjacency.get(territory);
    }

    public void addTerritory(Territory t) {
         territories.add(t);
         adjacency.put(t, new LinkedList<>());
    }

    public Set<Territory> getTerritoriesSet() {
         return adjacency.keySet();
    }

  /*public List<Territory> getTerritoriesList() {
         return territories;
         }*/
}
