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
    territoryNameList.add("Jiangsu");
    territoryNameList.add("Zhejiang");
    // for 2 players
    territoryNameList.add("Anhui");
    territoryNameList.add("Shandong");
    territoryNameList.add("Fujian");
    // for 3 players
    territoryNameList.add("Taiwan");
    territoryNameList.add("Guangdong");
    territoryNameList.add("Henan");
    // for 4 players
    territoryNameList.add("Hubei");
    territoryNameList.add("Hunan");
    territoryNameList.add("Jiangxi");
    // for 5 players
    territoryNameList.add("Hongkong");
    territoryNameList.add("Macau");
    territoryNameList.add("Hebei");    
  }

  public List<String> getTerritoryNameList() {
    return territoryNameList;
  }
  
  private void apply1Player(Board board) {
    Territory t1 = new BasicTerritory(territoryNameList.get(0));
    board.addTerritory(t1);
    board.putEntry(t1, new LinkedList<Territory> ());
  }

  private void apply2Player(Board board) {
    // add board names
    for (int i = 0; i < 6; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i));
      board.addTerritory(t);
    }
    // TODO: add adjancency
  }

  private void apply3Player(Board board) {
    // add board names
    for (int i = 0; i < 9; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i));
      board.addTerritory(t);
    }
    // TODO: add adjancency
  }

  private void apply4Player(Board board) {
    // add board names
    for (int i = 0; i < 12; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i));
      board.addTerritory(t);
    }
    // TODO: add adjancency
  }

  private void apply5Player(Board board) {
    // add board names
    for (int i = 0; i < 15; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i));
      board.addTerritory(t);
    }
    // TODO: add adjancency
  }
  
  /**
   * Generator game map based on given player number
   */
  public void apply(Board m, int playerNum) {
    if (playerNum == 1) {
      apply1Player(m);  // only for testing and early version
    }
    else if (playerNum == 2) {
      apply2Player(m);
    }
    
  }
}
