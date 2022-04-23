package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describe Game State
 * board: topology relationship between territories
 * players: map from color to Clan
 */
public class GameInfo extends Actable{

  private Map<String, List<String>> mesg;

  public GameInfo(Board b, Map<String, Clan> players) {
    super(b, players);
  }

  public GameInfo(Board b, Map<String, Clan> players, Map<String, List<String>> mesg) {
    super(b, players);
    this.mesg = mesg;
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

  public Map<String, List<String>> getMesg() {
    return mesg;
  }

}
