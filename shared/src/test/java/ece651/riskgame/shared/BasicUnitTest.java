package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BasicUnitTest {
  @Test
  public void test_getAttack_getName() {
    BasicUnit bu = new BasicUnit();
    assertEquals(1, bu.getAttack());
    assertEquals(1, bu.getHP());
  }

}
