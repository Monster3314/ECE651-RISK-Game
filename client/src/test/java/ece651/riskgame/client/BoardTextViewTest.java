package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;

public class BoardTextViewTest {
  @Test
  public void test_displayBoard() {
    Board b = new Board();
    BoardTextView view = new BoardTextView(b);
    b.addTerritory(new BasicTerritory("Durham"));
    assertEquals("Durham\n", view.displayBoard());
    b.addTerritory(new BasicTerritory("Raleigh"));
    assertEquals("Raleigh\nDurham\n", view.displayBoard());
  }

}
