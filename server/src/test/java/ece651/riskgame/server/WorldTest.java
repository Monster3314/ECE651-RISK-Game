package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.*;

import ece651.riskgame.shared.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class WorldTest {
  @Test
  public void test_AddClan() throws Exception {
    World w = new World(3);
    assertEquals("Red", w.addClan());
    assertEquals("Blue", w.addClan());
    assertEquals("Green", w.addClan());
    //assertThrows(IllegalAccessException.class, ()->{w.addClan();});
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

  @Test
  public void test_ruleChecker() throws Exception {
    ActionRuleChecker actionRuleChecker = new MovePathChecker(null);
    World w = new World(2);
    w.addClan();
    w.addClan();
    Move m1 = new Move(new BasicUnit(), "Shanghai", "Jiangsu", "Red");
    Move m2 = new Move(new BasicUnit(), "Shandong", "Fujian", "Blue");
    Move m3 = new Move(new BasicUnit(), "Anhui", "Jiangsu", "Blue");
    Move m4 = new Move(new BasicUnit(), "Anhui", "Jiangsu", "Red");
    Move m5 = new Move(new BasicUnit(), "Anhui", "Anhui", "Blue");
    assertNull(actionRuleChecker.checkAction(w, m1));
    assertNotNull(actionRuleChecker.checkAction(w, m2));
    assertNotNull(actionRuleChecker.checkAction(w, m3));
    assertNotNull(actionRuleChecker.checkAction(w, m4));
    assertNotNull(actionRuleChecker.checkAction(w, m5));
  }

  @Test
  public void test_getTerritoryOwnerShip() throws IllegalAccessException, IOException {
    World w = new World(2);
    w.addClan();
    w.addClan();
    assertNull(w.getTerritoryOwnership("A"));
  }
}
