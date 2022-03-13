package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class GameTextViewTest {
  @Test
  public void test_displayGame() {
    String expected;
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    GameInfo g = new GameInfo(b, players);
    GameTextView view = new GameTextView(g);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t3)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2, t4)));
    players.put("Green", c1);
    players.put("Blue", c2);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));

    expected = "Blue player:\n"
      + GameTextView.getDelimiter()
      + "3 units in Raleigh (next to: Durham, Cary)\n"
      + "0 unit in Chapel Hill (next to: Durham)\n"
      + "\n"
      + "Green player:\n"
      + GameTextView.getDelimiter()
      + "4 units in Durham (next to: Raleigh, Cary)\n"
      + "1 unit in Cary (next to: Durham, Raleigh)\n"
      + "\n";
    
    assertEquals(expected, view.displayGame());
  }

}
