package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GameInfoTest {
  @Test
  public void test_get_set_board() {
    Board b = new Board();
    GameInfo gi = new GameInfo(b);
    assertSame(b, gi.getBoard());
    gi.setBoard(null);
    assertSame(null, gi.getBoard());
  }

}
