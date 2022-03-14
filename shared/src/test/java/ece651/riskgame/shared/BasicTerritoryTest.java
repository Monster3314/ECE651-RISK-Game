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
    Unit a = new BasicUnit();
    List<Unit> l = new ArrayList<>();
    l.add(a);
    bt.addUnitList(l);
    assertEquals(bt.getUnits(), l);
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

  @Test
  public void test_add_dec() {
    BasicTerritory a = new BasicTerritory("A");
    a.addUnit(new BasicUnit());
    BasicUnit u = new BasicUnit(10);
    a.addUnit(u);
    assertEquals(11, a.getUnits().get(0).getNum());
    BasicUnit u2 = new BasicUnit(5);
    a.decUnit(u2);
    assertEquals(6, a.getUnits().get(0).getNum());

    BasicTerritory b = new BasicTerritory("B");
    assertThrows(IllegalArgumentException.class, ()->b.decUnit(u));
  }

}
