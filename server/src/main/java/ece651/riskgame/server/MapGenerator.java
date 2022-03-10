package ece651.riskgame.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;


public class MapGenerator {

  private List<String> territoryNameList;
  private List<Territory> addedTerritories;
  private boolean[][] adjancencyMatrix;
  private final int MaxTerritoryNum = 15;
  
  public MapGenerator() throws IOException, FileNotFoundException{
    territoryNameList = new ArrayList<String>();
    addedTerritories = new ArrayList<Territory>();
    adjancencyMatrix = new boolean[MaxTerritoryNum][MaxTerritoryNum];
    initTerritoryNameList();
    initAdjancencyMatrix();
  }

  private void initTerritoryNameList() throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("TerritoryNames.txt"))) {
      String line;
      for (int i =0 ; i < MaxTerritoryNum; i++) {
        line = br.readLine();
        territoryNameList.add(line);
      }
    }
  }

  private void initAdjancencyMatrix() throws IOException, FileNotFoundException {
    try (BufferedReader br = new BufferedReader(new FileReader("AMinit.csv"))) {
      String line;
      int row = 0;
      while ((line = br.readLine()) != null) {
        /*if (row == 15) {
          throw new IllegalArgumentException("adjancency matrix initialization file format is illegal");
        }*/
        String[] values = line.split(",");
        /*if (values.length != MaxTerritoryNum) {
          throw new IllegalArgumentException("adjancency matrix initialization file format is illegal");
          }*/
        for (int c = 0; c < MaxTerritoryNum; c++) {
          if (Integer.parseInt(values[c]) == 1) {
            adjancencyMatrix[row][c] = true;
          }
        }
        row+=1;
      }
      /*if (row != 15) {
        throw new IllegalArgumentException("adjancency matrix initialization file format is illegal");
        }*/
    }
  }

  public List<String> getTerritoryNameList() {
    return territoryNameList;
  }

  public boolean[][] getAdjancencyMatrix() {
    return this.adjancencyMatrix;
  }
  
  /**
   * Generator game map based on given player number
   */
  public void apply(Board board, int playerNum) {
    if (playerNum < 1|| playerNum > 5) {
      throw new IndexOutOfBoundsException("Player Number should be 1-5");
    }
    int numTer = 3 * playerNum;
    // add board names
    for (int i = 0; i < numTer; i++) {
      Territory t = new BasicTerritory(territoryNameList.get(i));
      board.addTerritory(t);
      addedTerritories.add(t);
    }
    // add adjancency
    for (int i = 0; i < numTer; i++) {
      Territory ter = addedTerritories.get(i);
      LinkedList<Territory> neighbors = new LinkedList<Territory>();
      for (int j = 0; j < numTer; j++) {        
        if (this.adjancencyMatrix[i][j]) {
          neighbors.add(addedTerritories.get(j));
        }
      }
      board.putEntry(ter, neighbors);      
    }
  }
}
