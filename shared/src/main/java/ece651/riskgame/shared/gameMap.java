package ece651.riskgame.shared;

import java.util.HashMap;
import java.util.LinkedList;

public class gameMap {
     private HashMap<Territory, LinkedList<Territory>> adjacency;

     public gameMap() {
         adjacency = new HashMap<Territory, LinkedList<Territory>>();
     }

    public void putEntry(Territory territory, LinkedList<Territory> neighbors) {
         adjacency.put(territory, neighbors);
    }

    public LinkedList<Territory> getNeighbors(Territory territory) {
         return adjacency.get(territory);
    }

}
