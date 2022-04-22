package ece651.riskgame.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Resource;
import ece651.riskgame.shared.Territory;

public class MapGenerator {

  private List<String> territoryNameList;
  private List<Territory> addedTerritories;
  private Map<String, List<String>> adjacencyList;
  private final int MaxTerritoryNum = 15;

  public MapGenerator(String territoryNameSource, String adjacencyMatrixSource) throws IOException {
    territoryNameList = new ArrayList<String>();
    addedTerritories = new ArrayList<Territory>();
    adjacencyList = new HashMap<String, List<String>>();
    initTerritoryNameList(territoryNameSource);
    initAdjacencyList(adjacencyMatrixSource);
  }

  private void initTerritoryNameList(String sourceFile) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
      String line;
      for (int i = 0; i < MaxTerritoryNum; i++) {
        line = br.readLine();
        territoryNameList.add(line);
      }
    }
  }

  /**
   * @param sourceFile recording the topology relationship between territories
   * @throws IOException when failed to read source file
   */
  private void initAdjacencyList(String sourceFile) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
      String line;
      int row = 0;
      for (int i = 0; i < MaxTerritoryNum; i++) {
        line = br.readLine();
        /*
         * if (line == null) { throw new
         * IllegalArgumentException("adjacency matrix initialization file format is illegal"
         * ); }
         */
        String[] values = line.split(",");
        /*
         * if (values.length != MaxTerritoryNum) { throw new
         * IllegalArgumentException("adjacency matrix initialization file format is illegal"
         * ); }
         */
        List<String> valueList = new ArrayList<String>();
        adjacencyList.put(values[0], valueList);
        for (int c = 1; c < values.length; c++) {
          valueList.add(values[c]);
        }
        row += 1;
      }
      /*
       * if (row != 15) { throw new
       * IllegalArgumentException("adjacency matrix initialization file format is illegal"
       * ); }
       */
    }
  }

  public List<String> getTerritoryNameList() {
    return territoryNameList;
  }

  public Map<String, List<String>> getAdjacencyList() {
    return this.adjacencyList;
  }

  public List<Territory> getAddedTerritories() {
    return addedTerritories;
  }

  /**
   * Generator game map based on given player number
   */
  public void apply(Board board, int playerNum) {
    if (playerNum < 1 || playerNum > 5) {
      throw new IndexOutOfBoundsException("Player Number should be 1-5");
    }
    int numTer = 3 * playerNum;
    // add board names
    for (int i = 0; i < numTer; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i), 2, new Resource(new int[] {20, 500})); // TODO
      board.addTerritory(t);
      addedTerritories.add(t);
    }
    // add adjacency
    for (int i = 0; i < numTer; i++) {
      Territory ter = addedTerritories.get(i);
      List<String> neighborNames = adjacencyList.get(ter.getName());
      List<Territory> neighbors = neighborNames.stream().collect(ArrayList<Territory>::new, (l, name) -> {
        if (board.containsTerritory(name)) {
          l.add(board.getTerritory(name));
        }
      }, (a, b) -> a.addAll(b));
      board.putEntry(ter, neighbors);
    }
  }
}
