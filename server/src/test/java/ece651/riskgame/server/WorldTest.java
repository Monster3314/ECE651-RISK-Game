package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.*;

import ece651.riskgame.shared.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WorldTest {
  @Test
  public void test_AddClan() throws Exception {
    World w = new World(3);
    assertEquals("red", w.addClan());
    assertEquals("blue", w.addClan());
    assertEquals("green", w.addClan());
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
    Move m = new Move(List.of(new BasicUnit(3)), b.get(0).getName(), b.get(1).getName(), "Red");
    //TODO: change move
//    w.acceptAction(m);
//    assertEquals(b.get(0).getUnits().get(0).getNum(), 2);
//    assertEquals(b.get(1).getUnits().get(0).getNum(), 4);
  }

  @Test
  public void test_ruleChecker() throws Exception {
    ActionRuleChecker actionRuleChecker = new MovePathChecker(null);
    World w = new World(2);
    w.addClan();
    w.addClan();
    Move m1 = new Move(List.of(new BasicUnit()), "Shanghai", "Jiangsu", "Red");
    Move m2 = new Move(List.of(new BasicUnit()), "Shandong", "Fujian", "Blue");
    Move m3 = new Move(List.of(new BasicUnit()), "Anhui", "Jiangsu", "Blue");
    Move m4 = new Move(List.of(new BasicUnit()), "Anhui", "Jiangsu", "Red");
    Move m5 = new Move(List.of(new BasicUnit()), "Anhui", "Anhui", "Blue");
    assertNull(actionRuleChecker.checkAction(w, m1));
    assertNotNull(actionRuleChecker.checkAction(w, m2));
    assertNotNull(actionRuleChecker.checkAction(w, m3));
    assertNull(actionRuleChecker.checkAction(w, m4));
    assertNotNull(actionRuleChecker.checkAction(w, m5));
  }

  @Test
  public void test_getTerritoryOwnerShip() throws IllegalAccessException, IOException {
    World w = new World(2);
    w.addClan();
    w.addClan();
    assertNull(w.getTerritoryOwnership("A"));
  }

  @Test
  public void testGetUnitMoveCost() throws IOException {
    Board map = new Board();
    Territory t1 = new BasicTerritory("A", 1, new Resource());
    LinkedList<Territory> neig = new LinkedList<>();
    Territory t2 = new BasicTerritory("B", 2, new Resource());
    Territory t3 = new BasicTerritory("C", 3, new Resource());
    Territory t4 = new BasicTerritory("D", 4, new Resource());
    map.addTerritory(t1);
    neig.add(t2);
    neig.add(t3);
    map.putEntry(t1, neig);
    map.putEntry(t2, Arrays.asList(t1, t4));
    map.putEntry(t3, Arrays.asList(t1, t4));
    map.putEntry(t4, Arrays.asList(t2, t3));

    World world = new World(2);
    world.addClan();
    world.addClan();
    world.getUnitMoveCost("Shanghai", "Red");
//    assertEquals(7, world.getUnitMoveCost("A", "Red").get("D"));
  }
}
