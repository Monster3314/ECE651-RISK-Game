package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.*;

import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WorldTest {
  @Test
  public void test_AddClan() throws Exception {
    World w = new World(3);
    assertEquals("Red", w.addClan());
    assertEquals("Blue", w.addClan());
    assertEquals("Green", w.addClan());
    assertThrows(IllegalAccessException.class, ()->{w.addClan();});
  }

  @Test
  public void test_moveAction() throws Exception{
    World w = new World(2);
    w.addClan();
    w.addClan();
    List<Territory> b = w.getBoard().getTerritoriesList();
    b.get(0).addUnit(new BasicUnit(5));
    b.get(1).addUnit(new BasicUnit());
    assertEquals(b.get(0).getUnits().get(0).getNum(), 5);
    Move m = new Move(new BasicUnit(3), b.get(0).getName(), b.get(1).getName(), "Red");
    w.acceptAction(m);
    assertEquals(b.get(0).getUnits().get(0).getNum(), 2);
    assertEquals(b.get(1).getUnits().get(0).getNum(), 4);
  }
}
