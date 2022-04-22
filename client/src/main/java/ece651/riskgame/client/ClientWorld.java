package ece651.riskgame.client;

import java.util.Map;

import ece651.riskgame.shared.Actable;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class ClientWorld extends Actable {

  /**
   * Constructor which takes the gameinfo recieved from server
   */
  public ClientWorld(GameInfo newGame) {
    super(newGame.getBoard(), newGame.getClans()); 
  }
  public ClientWorld(Board board, Map<String, Clan> clans) {
    super(board, clans);
  }
  public void updateTerritoryOwnership(String territoryName, String color) throws IllegalArgumentException {
    if (!board.containsTerritory(territoryName)) {
      throw new IllegalArgumentException("Territory not existed.");
    }
    if (!clans.containsKey(color)) {
      throw new IllegalArgumentException("Color not existed.");
    }
    Territory toUpdate = null;
    for (Clan c: clans.values()) {
      toUpdate = c.removeTerritory(territoryName);
      if (toUpdate != null) {
        clans.get(color).addTerritory(toUpdate);
      }
    }
  }
  public void updateTerritory(Territory latestTerritory) throws IllegalArgumentException{
    Territory toUpdate = board.getTerritory(latestTerritory.getName());
    toUpdate.update(latestTerritory);
  }

  public void updateClanAsset(String color, Clan latestClan) throws IllegalArgumentException{
    if (!clans.containsKey(color)) {
      throw new IllegalArgumentException("Color not existed");
    }
    if (latestClan == null) {
      throw new IllegalArgumentException("Clan can not be null");
    }
    Clan toUpdate = clans.get(color);
    toUpdate.updateAsset(latestClan);
  }
  

}
