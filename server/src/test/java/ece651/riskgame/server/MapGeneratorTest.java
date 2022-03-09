package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;

public class MapGeneratorTest {
  public MapGenerator mapGenerator = new MapGenerator();
  
  @Test
  public void test_apply1() {
    // Here all the territories for specified player numner is determined
    Board board1 = new Board();    
    mapGenerator.apply(board1, 1);
    for (Territory t: board1.getTerritories()) {
      assertEquals(mapGenerator.getTerritoryNameList().get(0), t.getName());
    }
  }

}
