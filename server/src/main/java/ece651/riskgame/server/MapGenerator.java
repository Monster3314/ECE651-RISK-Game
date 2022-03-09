package ece651.riskgame.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;


public class MapGenerator {

  private List<String> territoryNameList;

  public MapGenerator() {
    territoryNameList = new ArrayList<String>();
    territoryNameList.add("Shanghai");
  }

  public List<String> getTerritoryNameList() {
    return territoryNameList;
  }
  
  private void apply1Player(Board board) {
    Territory t1 = new BasicTerritory(territoryNameList.get(0));
    board.addTerritory(t1);
    board.putEntry(t1, new LinkedList<Territory> ());
  }
  
  /**
   * Generator game map based on given player number
   */
  public void apply(Board m, int playerNum) {
    if (playerNum == 1) {
      apply1Player(m);  // only for testing and early version
    }
    
  }
}
