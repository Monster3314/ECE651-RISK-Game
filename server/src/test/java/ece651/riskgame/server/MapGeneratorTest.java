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
  public static Set<String> territoryNames1;

  @BeforeAll
  public static void initTerritoryNames() {
    territoryNames1 = new HashSet<String>();

    territoryNames1.add("North Carolina");
  }
  
  @Test
  public void test_apply() {
    // Here all the territories for specified player numner is determined
    Board board1 = new Board();
    MapGenerator.apply(board1, 1);
    for (Territory t: board1.getTerritories()) {
      assertTrue(territoryNames1.contains(t.getName()));
    }
  }

}
