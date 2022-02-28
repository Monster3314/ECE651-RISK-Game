package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    

}
