package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class GameTextViewTest {
  @Test
  public void test_displayClan() {
    /*
    String expected;
    Board b = new Board();
    GameInfo g = new GameInfo(b);
    GameTextView view = new GameTextView(g);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    expected = "Durham (next to: Raleigh, Cary)\n";
    assertEquals(expected, view.displayGame());
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    expected = expected +
        "Raleigh (next to: Durham, Cary)\n";
    assertEquals(expected, view.displayGame());
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    expected = expected +
        "Cary (next to: Durham, Raleigh)\n";
    assertEquals(expected, view.displayGame());
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    expected = expected +
        "Chapel Hill (next to: Durham)\n";
        assertEquals(expected, view.displayGame());*/
  }
  @Test
  public void test_displayTerritory() {
    String expected;
    Board b = new Board();
    GameInfo g = new GameInfo(b, null);
    GameTextView view = new GameTextView(g);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    expected = "Durham (next to: Raleigh, Cary)\n";
    assertEquals(expected, view.displayTerritory(t1));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    expected = "Raleigh (next to: Durham, Cary)\n";
    assertEquals(expected, view.displayTerritory(t2));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    expected = "Cary (next to: Durham, Raleigh)\n";
    assertEquals(expected, view.displayTerritory(t3));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    expected = "Chapel Hill (next to: Durham)\n";
    assertEquals(expected, view.displayTerritory(t4));
  }

}
