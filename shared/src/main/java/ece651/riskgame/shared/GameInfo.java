package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
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

  /*
    Input: territory name
    Return: Clan's color
   */
  public String getTerritoryOwnership(String name) {
    for (Map.Entry<String, Clan> clan : players.entrySet()) {
      if (clan.getValue().occupyTerritory(name)) {
        return clan.getKey();
      }
    }
    return null;
  }

 public String getWinner() {
    ArrayList<String> alivePlayer = new ArrayList<>();
    for(Map.Entry<String, Clan> clan : players.entrySet()) {
      if(clan.getValue().isActive()) alivePlayer.add(clan.getKey());
    }
    if(alivePlayer.size() > 1) return null;
    else return alivePlayer.get(0);
 }
}
