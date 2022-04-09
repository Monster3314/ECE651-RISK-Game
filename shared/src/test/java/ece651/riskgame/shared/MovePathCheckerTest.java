package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MovePathCheckerTest {
  
  @Test
  public void test_checkMyRule() {
    Board b = new Board();
    Territory t1 = new BasicTerritory("t1");
    Territory t2 = new BasicTerritory("t2");
    Territory t3 = new BasicTerritory("t3");
    Territory t4 = new BasicTerritory("t4");
    Territory t5 = new BasicTerritory("t5");        
    b.addTerritory(t1);
    b.addTerritory(t2);
    b.addTerritory(t3);
    b.addTerritory(t4);
    b.addTerritory(t5);
    
    List<Territory> neighbors1 = new LinkedList<Territory>();
    neighbors1.add(t2);
    neighbors1.add(t3);
    b.putEntry(t1, neighbors1);
    
    List<Territory> neighbors2 = new LinkedList<Territory>();
    neighbors2.add(t1);
    neighbors2.add(t3);
    b.putEntry(t2, neighbors2);

    List<Territory> neighbors3 = new LinkedList<Territory>();
    neighbors3.add(t1);
    neighbors3.add(t2);
    neighbors3.add(t4);
    b.putEntry(t3, neighbors3);

    List<Territory> neighbors4 = new LinkedList<Territory>();
    neighbors4.add(t3);
    b.putEntry(t4, neighbors4);
        
    Clan clan1 = new Clan();
    clan1.addTerritory(t1);
    clan1.addTerritory(t2);
    clan1.addTerritory(t4);
    
    Clan clan2 = new Clan();
    clan2.addTerritory(t3);
    
    Map<String, Clan> map = new HashMap<>();
    map.put("Red", clan1);
    map.put("Blue", clan2);    

    Actable actable = Mockito.mock(Actable.class);
    when(actable.getBoard()).thenReturn(b);
    when(actable.getTerritoryOwnership("t1")).thenReturn("Red");
    when(actable.getTerritoryOwnership("t2")).thenReturn("Red");
    when(actable.getTerritoryOwnership("t3")).thenReturn("Blue");
    when(actable.getTerritoryOwnership("t4")).thenReturn("Red");    

    Unit unit = new BasicUnit(5);
    Action act1 = new Move(List.of(unit), "t1", "t1", "Red");
    Action act2 = new Move(List.of(unit), "t1", "t2", "Blue");
    Action act3 = new Move(List.of(unit), "t1", "t2", "Red");
    Action act4 = new Move(List.of(unit), "t1", "t4", "Red");
    Action act5 = new Move(List.of(unit), "t1", "t3", "Red");

    ActionRuleChecker urc = new MovePathChecker(null);

    assertEquals("The departure and destination territory are the same territory!", urc.checkMyRule(actable, act1));
    assertNotNull(urc.checkMyRule(actable, act2));
    assertNull(urc.checkMyRule(actable, act3));
    String result =  urc.checkMyRule(actable, act4);
    assertTrue(result.startsWith("Failed to find a path from"));
    assertEquals("The destination territory does not belong to Red player.", urc.checkMyRule(actable, act5));
  }

  
}
