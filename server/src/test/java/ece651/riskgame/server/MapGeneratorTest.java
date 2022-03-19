package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;

public class MapGeneratorTest{
  public MapGenerator mapGenerator;
  

  @Test
  public void test_Constructor() throws IOException, FileNotFoundException {
    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    List<String> names = mapGenerator.getTerritoryNameList();
    assertEquals(names.get(0), "Shanghai");
    assertEquals(names.get(6), "Taiwan");
    assertEquals(names.get(14), "Hebei");
    assertThrows(IndexOutOfBoundsException.class, () -> {names.get(15);});

    // test adjancency
    boolean[][] am = mapGenerator.getAdjacencyMatrix();
    assertEquals(am[4][1], true);
    assertEquals(am[6][6], false);
    assertEquals(am[9][3], true);
    assertEquals(am[14][14], false);
  }

  @Test
  public void test_apply() throws IOException, FileNotFoundException {
    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    // Here all the territories for specified player numner is determined
    Board board1 = new Board();
    // test throws
    assertThrows(IndexOutOfBoundsException.class, () -> {mapGenerator.apply(board1, 0);});
    assertThrows(IndexOutOfBoundsException.class, () -> {mapGenerator.apply(board1, 6);});
    // test 1 player
    mapGenerator.apply(board1, 1);
    assertEquals(3, board1.getTerritoriesSet().size());
    assertEquals("Shanghai", mapGenerator.getAddedTerritories().get(0).getName());
    assertEquals("Jiangsu", mapGenerator.getAddedTerritories().get(1).getName());
    assertEquals("Zhejiang", mapGenerator.getAddedTerritories().get(2).getName());
    for (int i = 0; i < 3; i++) {
      assertNotEquals(0, board1.getNeighbors(mapGenerator.getAddedTerritories().get(i)));
    }
    assertEquals(2, board1.getNeighbors(mapGenerator.getAddedTerritories().get(0)).size());
    assertEquals(2, board1.getNeighbors(mapGenerator.getAddedTerritories().get(1)).size());
    assertEquals(2, board1.getNeighbors(mapGenerator.getAddedTerritories().get(2)).size());

    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    // test 2 player
    Board board2 = new Board();
    mapGenerator.apply(board2, 2);
    assertEquals(6, board2.getTerritoriesSet().size());
    assertEquals("Shanghai", mapGenerator.getAddedTerritories().get(0).getName());
    assertEquals("Jiangsu", mapGenerator.getAddedTerritories().get(1).getName());
    assertEquals("Zhejiang", mapGenerator.getAddedTerritories().get(2).getName());    
    assertEquals("Anhui", mapGenerator.getAddedTerritories().get(3).getName());
    assertEquals("Shandong", mapGenerator.getAddedTerritories().get(4).getName());
    assertEquals("Fujian", mapGenerator.getAddedTerritories().get(5).getName());
    for (int i = 0; i < 6; i++) {
      assertNotEquals(0, board2.getNeighbors(mapGenerator.getAddedTerritories().get(i)));
    }
    assertEquals(2, board2.getNeighbors(mapGenerator.getAddedTerritories().get(0)).size());
    assertEquals(4, board2.getNeighbors(mapGenerator.getAddedTerritories().get(1)).size());
    assertEquals(4, board2.getNeighbors(mapGenerator.getAddedTerritories().get(2)).size());
    assertEquals(3, board2.getNeighbors(mapGenerator.getAddedTerritories().get(3)).size());    
    assertEquals(2, board2.getNeighbors(mapGenerator.getAddedTerritories().get(4)).size());
    assertEquals(1, board2.getNeighbors(mapGenerator.getAddedTerritories().get(5)).size());

    mapGenerator = new MapGenerator("TerritoryNames.txt", "AMinit.csv");
    // test 5 player
    Board board5 = new Board();
    mapGenerator.apply(board5, 5);
    assertEquals(15, board5.getTerritoriesSet().size());
    assertEquals("Hebei", mapGenerator.getAddedTerritories().get(14).getName());
    for (int i = 0; i < 15; i++) {
      assertNotEquals(0, board5.getNeighbors(mapGenerator.getAddedTerritories().get(i)));
    }
    assertEquals(4, board5.getNeighbors(mapGenerator.getAddedTerritories().get(4)).size());
    assertEquals(4, board5.getNeighbors(mapGenerator.getAddedTerritories().get(5)).size());
    assertEquals(1, board5.getNeighbors(mapGenerator.getAddedTerritories().get(6)).size());
    assertEquals(5, board5.getNeighbors(mapGenerator.getAddedTerritories().get(7)).size());
    assertEquals(4, board5.getNeighbors(mapGenerator.getAddedTerritories().get(8)).size());
    assertEquals(4, board5.getNeighbors(mapGenerator.getAddedTerritories().get(9)).size());
    assertEquals(3, board5.getNeighbors(mapGenerator.getAddedTerritories().get(10)).size());
    assertEquals(6, board5.getNeighbors(mapGenerator.getAddedTerritories().get(11)).size());
    assertEquals(1, board5.getNeighbors(mapGenerator.getAddedTerritories().get(12)).size());
    assertEquals(1, board5.getNeighbors(mapGenerator.getAddedTerritories().get(13)).size());    
    assertEquals(2, board5.getNeighbors(mapGenerator.getAddedTerritories().get(14)).size());    
  }

}
