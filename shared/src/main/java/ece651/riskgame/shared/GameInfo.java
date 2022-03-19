package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Describe Game State
 * board: topology relationship between territories
 * players: map from color to Clan
 */
public class GameInfo implements Serializable, Actable{
  private Board board;
  private Map<String, Clan> players;// clan

  public GameInfo(Board b, Map<String, Clan> players) {
    this.board = b;
    this.players = players;
  }
  @Override
  public Board getBoard() {
    return board;
  }
  @Override
  public Map<String, Clan> getClans() {
    return players;
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

  /**
   * @param name of territory
   * @return Clan's color
   */
  public String getTerritoryOwnership(String name) {
    for (Map.Entry<String, Clan> clan : players.entrySet()) {
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
    for(Map.Entry<String, Clan> clan : players.entrySet()) {
      if(clan.getValue().isActive()) alivePlayer.add(clan.getKey());
    }
    if(alivePlayer.size() > 1) return null;
    else return alivePlayer.get(0);
 }
}
