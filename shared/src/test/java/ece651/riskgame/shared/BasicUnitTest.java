package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BasicUnitTest {
  @Test
  public void test_getAttack_getName_getNum() {
    BasicUnit bu = new BasicUnit(10);
    assertEquals(1, bu.getAttack());
    assertEquals(1, bu.getHP());
    assertEquals(10, bu.getNum());
    assertEquals("10 Units", bu.toString());
  }

  @Test
  public void test_add_dec() {
    BasicUnit bu = new BasicUnit(20);
    bu.addSoldiers(10);
    assertEquals(30, bu.getNum());
    bu.decSoldiers(15);
    assertEquals(15, bu.getNum());
    assertThrows(IllegalArgumentException.class, () -> bu.decSoldiers(16));
  }

  @Test
  public void test_throw() {
    assertThrows(IllegalArgumentException.class, () -> new BasicUnit(-2));
  }

  @Test
  public void test_getRandomAttack() {
    BasicUnit bu = Mockito.spy(new BasicUnit(1));
    when(bu.getRandomAttack()).thenReturn(10);
    assertEquals(10, bu.getRandomAttack());
  }

  @Test
  public void test_toString() {
    BasicUnit u = new BasicUnit(2);
    assertEquals("2 Units", u.toString());
    BasicUnit u2 = new BasicUnit();
    assertEquals("1 Unit", u2.toString());
  }
}
