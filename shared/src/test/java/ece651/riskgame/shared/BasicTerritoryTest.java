package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BasicTerritoryTest {
  @Test
  public void test_getName() {
    BasicTerritory bt = new BasicTerritory("Ox");
    assertEquals("Ox", bt.getName());
  }

  @Test
  public void test_units() {
    //add unit
    BasicTerritory bt = new BasicTerritory("Ox");
    bt.addUnit(new BasicUnit());
    assertEquals(bt.getUnitNumber(), 1);
    //add unit list
    List<Unit> ul = new ArrayList<Unit>();
    ul.add(new BasicUnit());
    ul.add(new BasicUnit());
    bt.addUnitList(ul);
    assertEquals(bt.getUnitNumber(), 3);
  }

  @Test
  public void test_equal() {
    BasicTerritory a = new BasicTerritory("A");
    BasicTerritory b = new BasicTerritory("B");
    BasicTerritory a2 = new BasicTerritory("A");
    BasicTerritory c = new BasicTerritory(null);
    assertNotEquals(c, a);
    assertEquals(a, a2);
    assertNotEquals(b, a);
    assertTrue(a.equals(a));
    assertNotEquals(a, null);
  }

  @Test
  public void test_hashCode() {
    BasicTerritory a = new BasicTerritory("A");
    BasicTerritory b = new BasicTerritory("B");
    BasicTerritory a2 = new BasicTerritory("A");
    assertEquals(a.hashCode(), a2.hashCode());
    assertNotEquals(a.hashCode(), b.hashCode());
    BasicTerritory c = new BasicTerritory(null);
    assertEquals(0, c.hashCode());
  }

}
