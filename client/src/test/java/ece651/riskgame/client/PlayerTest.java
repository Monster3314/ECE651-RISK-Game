package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class PlayerTest {
  @Test
  public void test_PlayerConstructer() {
    GameInfo game = mock(GameInfo.class);
    Map<String, Clan> clans = mock(Map.class);
    when(clans.containsKey("Red")).thenReturn(true);
    when(clans.containsKey("Blue")).thenReturn(false);
    when(game.getClans()).thenReturn(clans);
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer(null, game));
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer("Red", null));
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer("Blue", game));
    assertDoesNotThrow(() -> new TextPlayer("Red", game));
  }
  @Test
  public void test_tryApplyAction() {
    //String color = "red";
    //GameInfo game = mock(GameInfo.class);
    

  }
  @Test
  public void test_updateGame() {
    Map<String, Clan> validClans = mock(Map.class);
    when(validClans.containsKey("Red")).thenReturn(true);
    Map<String, Clan> invalidClans = mock(Map.class);
    when(invalidClans.containsKey("Red")).thenReturn(false);
    GameInfo oldGame = mock(GameInfo.class);
    when(oldGame.getClans()).thenReturn(validClans);
    GameInfo newGame = mock(GameInfo.class);
    when(newGame.getClans()).thenReturn(validClans);
    
    GameInfo invalidGame = mock(GameInfo.class);
    when(invalidGame.getClans()).thenReturn(invalidClans);
    Player p = new TextPlayer("Red", oldGame);
    assertEquals(p.getGame(), oldGame);
    p.updateGame(newGame);
    assertEquals(p.getGame(), newGame);
    assertThrows(IllegalArgumentException.class, ()->p.updateGame(null));
    assertThrows(IllegalArgumentException.class, ()->p.updateGame(invalidGame));
  }
  @Test
  public void test_getColor() {
    GameInfo game = mock(GameInfo.class);
    Map<String, Clan> clans = mock(Map.class);
    when(clans.containsKey("Red")).thenReturn(true);
    when(clans.containsKey("Blue")).thenReturn(true);
    when(game.getClans()).thenReturn(clans);
    TextPlayer p = new TextPlayer("Red", game);
    assertEquals("Red", p.getColor());
    p = new TextPlayer("Blue", game);
    assertEquals("Blue", p.getColor());
  }
  @Test
  public void test_getters() {
    GameInfo game = mock(GameInfo.class);
    Map<String, Clan> clans = mock(Map.class);
    when(clans.containsKey("Red")).thenReturn(true);
    when(clans.containsKey("Blue")).thenReturn(true);
    when(game.getClans()).thenReturn(clans);
    TextPlayer p = new TextPlayer("Red", game);
    try {
      p.getOccupies();
    } catch(Exception e) {
    }
    try {
      p.getTechLevel();
    } catch(Exception e) {
    }
    try {
      p.getFood();
    } catch(Exception e) {
    }
    try {
      p.getGold();
    } catch(Exception e) {
    }
    try {
      p.isGameOver();
    } catch(Exception e) {
    }
    try {
      p.isLost();
    } catch(Exception e) {
    }
     
      
      
  }
  @Test
  public void test_getEnemyTerritoryNames() {
    GameInfo game = getDefaultGame();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    TextPlayer bluePlayer = new TextPlayer("Blue", game);
    assertEquals(new HashSet<String>(Arrays.asList("Raleigh", "Chapel Hill")), redPlayer.getEnemyTerritoryNames());
    assertEquals(new HashSet<String>(Arrays.asList("Durham", "Cary")), bluePlayer.getEnemyTerritoryNames());
  }
  @Test
  public void test_getTerritoryNames() {
    GameInfo game = getDefaultGame();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    TextPlayer bluePlayer = new TextPlayer("Blue", game);
    Set<String> expectedTerritoryNames = new HashSet<String>(Arrays.asList("Durham",
                                                                           "Raleigh",
                                                                           "Cary",
        "Chapel Hill"));
    assertEquals(expectedTerritoryNames, redPlayer.getTerritoryNames());
    assertEquals(expectedTerritoryNames, bluePlayer.getTerritoryNames());
  }
  protected GameInfo getDefaultGame() {
    Board b = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
    GameInfo game = new GameInfo(b, clans);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t3)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2, t4)));
    clans.put("Red", c1);
    clans.put("Blue", c2);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return game;
  }
  protected GameInfo getAnotherGame() {
    Board b = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
    GameInfo game = new GameInfo(b, clans);
    Territory t1 = new BasicTerritory("Jiangsu");
    Territory t2 = new BasicTerritory("Anhui");
    Territory t3 = new BasicTerritory("Shanghai");
    Territory t4 = new BasicTerritory("Zhejiang");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t3)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2, t4)));
    clans.put("Red", c1);
    clans.put("Blue", c2);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return game;
  }

}
