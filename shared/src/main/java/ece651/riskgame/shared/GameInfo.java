package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.Map;

public class GameInfo implements Serializable{
  private Board board;
  private Map<String, Clan> players;// clan

  public GameInfo(Board b, Map<String, Clan> players) {
    this.board = b;
    this.players = players;
  }
  
  public Board getBoard() {
    return board;
  }

  public void setBoard(Board b) {
    board = b;
  }

  public Map<String, Clan> getPlayers() {
    return players;
  }

  public void setPlayers(Map<String, Clan> players) {
    this.players = players;
  }

  public String getTerritoryOwnership(String name) {
    for (Map.Entry<String, Clan> clan : players.entrySet()) {
      if (clan.getValue().occupyTerritory(name)) {
        return clan.getKey();
      }
    }
    return null;
  }
}
