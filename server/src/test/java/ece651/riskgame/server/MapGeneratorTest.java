package ece651.riskgame.server;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.Board;

public class MapGeneratorTest {
  @Test
  public void test_apply() {
    Board gm = new Board();
    MapGenerator.apply(gm, 1);
    
  }

}
