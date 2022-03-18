package ece651.riskgame.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.*;

public class World implements Actable{
  private Board board;
  private Map<String, Clan> clans;  //the map for color of player and his clan

  private MapGenerator mapGenerator;
  private List<String> colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Pink"));
  private int colorUsed;
  private int playerNum;

  public World(int playerNum) throws IOException {
    board = new Board();
    clans = new HashMap<>();

    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    mapGenerator.apply(getBoard(), playerNum);

    colorUsed = 0;
    this.playerNum = playerNum;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public Map<String, Clan> getClans() {
    return clans;
  }

  public String addClan() {
    String color = colors.get(colorUsed);
    initClan(colorUsed, color);
    colorUsed++;
    return color;
  }

  public GameInfo getGameInfo() {
    return new GameInfo(board, clans);
  }

  /**
   * Allocate color and territories for each clan
   */
  private void initClan(int id, String color) {
    List<Territory> occupies = new ArrayList<>();
    occupies.add(getBoard().getTerritoriesList().get(id*3));
    occupies.add(getBoard().getTerritoriesList().get(id*3+1));
    occupies.add(getBoard().getTerritoriesList().get(id*3+2));
    clans.put(color, new Clan(occupies));
  }

  public void acceptAction(Action action) {
    action.apply(this);
  }

  public String getTerritoryOwnership(String name) {
    for (Map.Entry<String, Clan> clan : clans.entrySet()) {
      if (clan.getValue().occupyTerritory(name)) {
        return clan.getKey();
      }
    }
    return null;
  }
}
