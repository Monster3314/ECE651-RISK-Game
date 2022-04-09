package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class UpgradeTechActionTest {
  @Test
  public void test_all() {
    UpgradeTechAction uta = new UpgradeTechAction("green");
    assertEquals("green", uta.getColor());
    Actable act = mock(Actable.class);
    
  }

}
