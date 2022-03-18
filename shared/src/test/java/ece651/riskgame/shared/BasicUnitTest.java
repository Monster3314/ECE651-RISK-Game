package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BasicUnitTest {
  @Test
  public void test_getAttack_getName_getNum() {
    BasicUnit bu = new BasicUnit(10);
    assertEquals(1, bu.getAttack());
    assertEquals(1, bu.getHP());
    assertEquals(10, bu.getNum());
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
    assertThrows(IllegalArgumentException.class, ()->new BasicUnit(-2));
  }
}
