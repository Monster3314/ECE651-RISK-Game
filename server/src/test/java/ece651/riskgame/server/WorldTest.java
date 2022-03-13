package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class WorldTest {
  @Test
  public void test_AddClan() throws Exception {
    World w = new World(3);
    assertEquals("Red", w.addClan());
    assertEquals("Blue", w.addClan());
    assertEquals("Green", w.addClan());
    assertThrows(IllegalAccessException.class, ()->{w.addClan();});
  }

}
