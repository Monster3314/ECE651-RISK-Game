package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UpgradeUnitAction;

public class GUIPlayerTest extends PlayerTest{
  @Test
  public void test_SendActions() {
    GameInfo game = getDefaultGame();
    GUIPlayer player = new GUIPlayer("Red", game);
    List<Unit> toMove = new ArrayList<Unit>();
    List<Unit> toAttack = new ArrayList<Unit>();
    toMove.add(new BasicUnit(3));
    player.addActionToSend(new Move(toMove,
                                    "Durham",
                                    "Cary",
                                    "Red"));
    toAttack.add(new BasicUnit(4, 1));
    player.addActionToSend(new UpgradeUnitAction("Cary", 0, 1, 4, "Red"));
    player.addActionToSend(new Attack(toAttack, "Cary", "Chapel Hill", "Red"));
    assertEquals(3, player.getActionsToSend().size());
    player.clearActionsToSend();
    assertEquals(0, player.getActionsToSend().size());
  }
  @Test
  public void test_tryPlace() {
    GameInfo game = getInitialGame();
    GUIPlayer player = new GUIPlayer("Red", game);
    List<Unit> toAllocate = new ArrayList<>();
    toAllocate.add(new BasicUnit(30));
    Map<String, List<Unit>> placements;
    //valid placements
    player.updateGame(getInitialGame());
    placements = new HashMap<>();
    placements.put("Durham", List.of(new BasicUnit(10)));
    placements.put("Cary", List.of(new BasicUnit(20)));
    assertNull(player.tryPlace(placements, toAllocate));
    assertEquals(10, player.getTerritory("Durham").getUnitByLevel(0).getNum());
    assertEquals(20, player.getTerritory("Cary").getUnitByLevel(0).getNum());
    //valid placements
    player.updateGame(getInitialGame());
    placements = new HashMap<>();
    placements.put("Durham", List.of(new BasicUnit(15)));
    placements.put("Cary", List.of(new BasicUnit(15)));
    assertNull(player.tryPlace(placements, toAllocate));
    assertEquals(15, player.getTerritory("Durham").getUnitByLevel(0).getNum());
    assertEquals(15, player.getTerritory("Cary").getUnitByLevel(0).getNum());


    //more units placed
    player.updateGame(getInitialGame());
    placements = new HashMap<>();
    placements.put("Durham", List.of(new BasicUnit(10)));
    placements.put("Cary", List.of(new BasicUnit(10)));
    assertNotNull(player.tryPlace(placements, toAllocate));
    assertNull(player.getTerritory("Durham").getUnitByLevel(0));
    assertNull(player.getTerritory("Cary").getUnitByLevel(0));

    //place on enemy territory
    player.updateGame(getInitialGame());
    placements = new HashMap<>();
    placements.put("Durham", List.of(new BasicUnit(10)));
    placements.put("Raleigh", List.of(new BasicUnit(20)));
    assertNotNull(player.tryPlace(placements, toAllocate));
    assertNull(player.getTerritory("Durham").getUnitByLevel(0));
    assertNull(player.getTerritory("Raleigh").getUnitByLevel(0));


  }

}
