package ece651.riskgame.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.*;

public class World extends Actable{

  private MapGenerator mapGenerator;
  private List<String> colors = new ArrayList<>(Arrays.asList("red", "blue", "green", "mediumvioletred", "black"));
  private int colorUsed;
  private int playerNum;

  /**
   * Create board and clans, but only initialize board.
   */
  public World(int playerNum, String territoryList, String adjacencyList) throws IOException {
    board = new Board();
    clans = new HashMap<>();

    mapGenerator = new MapGenerator(territoryList, adjacencyList);
    mapGenerator.apply(getBoard(), playerNum);

    colorUsed = 0;
    this.playerNum = playerNum;
  }
  
  /**
   * Create board and clans, but only initialize board.
   */
  @Deprecated
  public World(int playerNum) throws IOException {
    this(playerNum, "territories.csv", "adjacency_list.csv");
  }

  /**
   * Initialize Clan with allocating its color
   */
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

  /**
   * @param action to take effect
   */
  public void acceptAction(Action action) {
    action.apply(this);
  }

}
