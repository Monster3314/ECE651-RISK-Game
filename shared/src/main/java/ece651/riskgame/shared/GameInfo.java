package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Describe Game State
 * board: topology relationship between territories
 * players: map from color to Clan
 */
public class GameInfo extends Actable{

  public GameInfo(Board b, Map<String, Clan> players) {
    super(b, players);
  }
  public void setBoard(Board b) {
    board = b;
  }

  public Map<String, Clan> getPlayers() {
    return clans;
  }

  public void setPlayers(Map<String, Clan> players) {
    this.clans = players;
  }



}
