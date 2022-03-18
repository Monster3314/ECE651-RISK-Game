package ece651.riskgame.shared;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TerritoryTest {

  public class DemoUnit extends Unit {
    public DemoUnit(int number) {
      super(2, 2, number);
    }

    @Override
    public int getRandomAttack() {    
      return 0;
    }
    
  }

  @Test
  public void test_addUnit_decUnit() {
    Territory t = new BasicTerritory("Sugar Mountain");
    t.addUnit(new BasicUnit(1));
    assertEquals(1, t.getUnits().get(0).getNum());
    
    assertThrows(IllegalArgumentException.class, () -> {t.decUnit(new DemoUnit(1));});
    assertEquals(1, t.getUnits().get(0).getNum());

    t.decUnit(new BasicUnit(1));
    assertEquals(0, t.getUnits().get(0).getNum());
    
    t.addUnit(new DemoUnit(1));
    assertEquals(1, t.getUnits().get(1).getNum());

    t.addUnit(new DemoUnit(1));
    assertEquals(2, t.getUnits().get(1).getNum());

    t.addUnit(new BasicUnit(1));
    assertEquals(1, t.getUnits().get(0).getNum());

    Unit du = new DemoUnit(1);
    assertEquals(0, du.getRandomAttack()); 
  }

  @Test
  public void test_beattacked() {
    Territory t = new BasicTerritory("A");
    t.addUnit(new BasicUnit(1));

    assertThrows(IllegalArgumentException.class, ()->t.beAttacked(new DemoUnit(1)));

    
  }

}
