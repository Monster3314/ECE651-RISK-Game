package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AdjacentTerritoryCheckerTest {

  @Test
  public void test_checkMyRule() {
    Board b = new Board();
    Territory t1 = new BasicTerritory("t1");
    Territory t2 = new BasicTerritory("t2");
    Territory t3 = new BasicTerritory("t3");    
    b.addTerritory(t1);
    b.addTerritory(t2);
    b.addTerritory(t3);
    List<Territory> neighbors = new LinkedList<Territory>();
    neighbors.add(t2);
    b.putEntry(t1, neighbors);
        
    Clan clan = new Clan();
    Map<String, Clan> map = new HashMap<String, Clan>();
    map.put("Red", clan);

    Actable actable = Mockito.mock(Actable.class);
    when(actable.getBoard()).thenReturn(b);

    Unit unit = new BasicUnit(5);
    Action act1 = new Move(List.of(unit), "t1", "t2", "color");
    Action act2 = new Move(List.of(unit), "t1", "t3", "color");

    ActionRuleChecker urc = new AdjacentTerritoryChecker(null);

    assertNull(urc.checkMyRule(actable, act1));
    assertEquals("Departure and destination territories are not adjacent", urc.checkMyRule(actable, act2));    
  }

}
