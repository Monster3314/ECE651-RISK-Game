package ece651.riskgame.shared;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    
//    assertThrows(IllegalArgumentException.class, () -> {t.decUnit(new DemoUnit(1));});
    assertEquals(1, t.getUnits().get(0).getNum());

//    t.decUnit(new BasicUnit(1));
//    assertEquals(0, t.getUnits().get(0).getNum());
//
//    t.addUnit(new DemoUnit(1));
//    assertEquals(1, t.getUnits().get(1).getNum());
//
//    t.addUnit(new DemoUnit(1));
//    assertEquals(2, t.getUnits().get(1).getNum());
//
//    t.addUnit(new BasicUnit(1));
//    assertEquals(1, t.getUnits().get(0).getNum());

    Unit du = new DemoUnit(1);
    assertEquals(0, du.getRandomAttack());
    assertDoesNotThrow(() -> {t.decUnit(new DemoUnit(1));});
  }

  @Test
  public void test_beattacked() {
    Territory t = new BasicTerritory("A");
    Unit bu1 = Mockito.spy(new BasicUnit(1));
    when(bu1.getRandomAttack()).thenReturn(10);
    t.addUnit(bu1);

    Unit bu2 = Mockito.spy(new BasicUnit(1));
    when(bu2.getRandomAttack()).thenReturn(5);
    Unit bu3 = Mockito.spy(new BasicUnit(1));
    when(bu3.getRandomAttack()).thenReturn(15);

    assertFalse(t.beAttacked(bu2));
    assertTrue(t.beAttacked(bu3));
    
    Unit du = new DemoUnit(1);      

    assertThrows(IllegalArgumentException.class, ()->t.beAttacked(du));
    
  }

}
