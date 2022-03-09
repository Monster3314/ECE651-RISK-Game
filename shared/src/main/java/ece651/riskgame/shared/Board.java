package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Board implements Serializable {
     private HashMap<Territory, LinkedList<Territory>> adjacency;
     private Set<Territory> territories;

     public Board() {
         adjacency = new HashMap<Territory, LinkedList<Territory>>();
         territories = new HashSet<>();
     }

    public void putEntry(Territory territory, LinkedList<Territory> neighbors) {
         adjacency.put(territory, neighbors);
    }

    public LinkedList<Territory> getNeighbors(Territory territory) {
         return adjacency.get(territory);
    }

    public void addTerritory(Territory t) {
         territories.add(t);
    }

    public Set<Territory> getTerritories() {
         return territories;
    }
}
