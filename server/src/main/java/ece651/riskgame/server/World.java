package ece651.riskgame.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class World {
  private Board board;
  private Map<String, Clan> clans;

  private MapGenerator mapGenerator;
  private List<String> colors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Pink"));
  private int colorUsed;
  private int playerNum;

  public World(int playerNum) throws IOException, FileNotFoundException {
    board = new Board();
    clans = new HashMap<String, Clan>();

    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    mapGenerator.apply(getBoard(), playerNum);

    colorUsed = 0;
    this.playerNum = playerNum;
  }
  
  public Board getBoard() {
    return board;
  }

  public String addClan() throws IllegalAccessException {
    if (colorUsed==playerNum) {
      throw new IllegalAccessException("All clans are already initialized");
    }
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
}
