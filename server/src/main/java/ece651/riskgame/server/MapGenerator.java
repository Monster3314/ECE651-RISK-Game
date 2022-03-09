package ece651.riskgame.server;

import java.util.LinkedList;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Board;


public class MapGenerator {
  /**
   * Generator game map based on given player number
   */
  static void apply(Board m, int playerNum) {
    // currently only work for 1 player
    Territory t1 = new BasicTerritory("North Carolina");
    m.addTerritory(t1);
    m.putEntry(t1, new LinkedList<Territory> ());
  }
}
