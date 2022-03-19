package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnemyTerritoryCheckerTest {
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
        
    Clan clan1 = new Clan();
    Clan clan2 = new Clan();
    clan1.addTerritory(t1);
    clan2.addTerritory(t2);
    Map<String, Clan> map = new HashMap<String, Clan>();
    map.put("Red", clan1);
    map.put("Pink", clan2);    

    Actable actable = Mockito.mock(Actable.class);
    //    when(actable.getBoard()).thenReturn(b);
    when(actable.getTerritoryOwnership("t1")).thenReturn("Red");
    when(actable.getTerritoryOwnership("t2")).thenReturn("Blue");    
    ActionRuleChecker urc = new EnemyTerritoryChecker(null);
    
    Unit unit = new BasicUnit(5);
    Action act1 = new Attack(unit, "t1", "t1", "Red");
    Action act2 = new Attack(unit, "t1", "t2", "Red");
    Action act3 = new Attack(unit, "t2", "t1", "Red");

    assertEquals("Territories belong to same clan.", urc.checkMyRule(actable, act1));
    assertEquals(null, urc.checkMyRule(actable, act2));
    assertNotNull(urc.checkMyRule(actable, act3));
  }


}
