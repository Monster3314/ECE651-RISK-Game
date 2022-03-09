package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;

public class MapGeneratorTest{
  public MapGenerator mapGenerator;
  

  @Test
  public void test_Constructor() throws IOException, FileNotFoundException {
    mapGenerator = new MapGenerator();
    List<String> names = mapGenerator.getTerritoryNameList();
    assertEquals(names.get(0), "Shanghai");
    assertEquals(names.get(6), "Taiwan");
    assertEquals(names.get(14), "Hebei");
    assertThrows(IndexOutOfBoundsException.class, () -> {names.get(15);});

    // test adjancency
    boolean[][] am = mapGenerator.getAdjancencyMatrix();
    assertEquals(am[4][1], true);
    assertEquals(am[6][6], false);
    assertEquals(am[9][3], true);
    assertEquals(am[14][14], false);
  }

  @Test
  public void test_apply() throws IOException, FileNotFoundException {
    mapGenerator = new MapGenerator();
    // Here all the territories for specified player numner is determined
    Board board1 = new Board();
    // test throws
    assertThrows(IndexOutOfBoundsException.class, () -> {mapGenerator.apply(board1, 0);});
    assertThrows(IndexOutOfBoundsException.class, () -> {mapGenerator.apply(board1, 6);});
    // test 1 player
    mapGenerator.apply(board1, 1);
    assertEquals(3, board1.getTerritoriesList().size());
    assertEquals("Zhejiang", board1.getTerritoriesList().get(2).getName());

    // test 5 player
    Board board5 = new Board();
    mapGenerator.apply(board5, 5);
    assertEquals(15, board5.getTerritoriesList().size());
    assertEquals("Hebei", board5.getTerritoriesList().get(14).getName());

  }

}
