package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.BasicTerritory;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Resource;
import ece651.riskgame.shared.Territory;

public class PlayerTest {
  @BeforeAll
  public static void start() {
    System.out.println("PlayerTest starting...");
  }
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
  public void test_apaptPlacements() {
    GameInfo game = getDefaultGame();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    redPlayer.adaptPlacements(List.of(new PlaceAction(new BasicUnit(), "Durham")));
  }
  @Test
  public void test_adaptPlacements() {
    GameInfo game = getDefaultGame();
    TextPlayer redPlayer = new TextPlayer("Red", game);
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
  public void test_getters() {
    GameInfo game = mock(GameInfo.class);
    Map<String, Clan> clans = mock(Map.class);
    when(clans.containsKey("Red")).thenReturn(true);
    when(clans.containsKey("Blue")).thenReturn(true);
    when(game.getClans()).thenReturn(clans);
    TextPlayer p = new TextPlayer("Red", game);
    
    try {
      p.isGameOver();
    } catch(Exception e) {
    }
  }

  @Test
  public void test_getTerritory() {
    GameInfo game = getDefaultGame();
    final TextPlayer redPlayer = new TextPlayer("Red", game);
    assertEquals("Durham", redPlayer.getTerritory("Durham").getName());
    assertEquals("Raleigh", redPlayer.getTerritory("Raleigh").getName());
    assertThrows(IllegalArgumentException.class, () -> redPlayer.getTerritory("Jiangsu"));
  }
  @Test
  public void test_occupyTerritory() {
    GameInfo game = getDefaultGame();
    final TextPlayer redPlayer = new TextPlayer("Red", game);
    assertTrue(redPlayer.occupyTerritory("Durham"));
    assertTrue(redPlayer.occupyTerritory("Cary"));
    assertFalse(redPlayer.occupyTerritory("Raleigh"));
    assertFalse(redPlayer.occupyTerritory("Chapel Hill"));
    assertThrows(IllegalArgumentException.class, () -> redPlayer.occupyTerritory("Jiangsu"));
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
  @Test
  public void test_getInfo() {
    GameInfo game = getDefaultGame();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    TextPlayer bluePlayer = new TextPlayer("Blue", game);

    assertEquals("Red", redPlayer.getColor());
    assertEquals("Blue", bluePlayer.getColor());
    
    assertEquals(1, redPlayer.getTechLevel());
    assertEquals(Clan.INITIAL_FOOD, redPlayer.getFood());
    assertEquals(Clan.INITIAL_GOLD, redPlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Durham", "Cary")), redPlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));
    
    assertEquals(1, bluePlayer.getTechLevel());    
    assertEquals(Clan.INITIAL_FOOD, bluePlayer.getFood());
    assertEquals(Clan.INITIAL_GOLD, bluePlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Raleigh", "Chapel Hill")), bluePlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));

    redPlayer.updateGame(getAnotherGame());
    bluePlayer.updateGame(getAnotherGame());

    assertEquals(3, redPlayer.getTechLevel());
    assertEquals(100, redPlayer.getFood());
    assertEquals(200, redPlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Durham", "Raleigh")), redPlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));
    
    assertEquals(6, bluePlayer.getTechLevel());    
    assertEquals(0, bluePlayer.getFood());
    assertEquals(0, bluePlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Cary", "Chapel Hill")), bluePlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));
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
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t2)), 3, new Resource(new int[] {100, 200}));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t3, t4)), 6, new Resource(new int[] {0, 0}));
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
  protected GameInfo getEmptyGame() {
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    GameInfo g = new GameInfo(b, players);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList()));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t1,t2,t3,t4)));
    players.put("Red", c1);
    players.put("Blue", c2);
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
    return g;
  }
  protected GameInfo getInitialGame() {
     Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    GameInfo g = new GameInfo(b, players);
    Territory t1 = new BasicTerritory("Durham");
    Territory t2 = new BasicTerritory("Raleigh");
    Territory t3 = new BasicTerritory("Cary");
    Territory t4 = new BasicTerritory("Chapel Hill");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1, t3)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2, t4)));
    players.put("Red", c1);
    players.put("Blue", c2);
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t2, t3)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t1, t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return g;
  }
  protected GameInfo getFourPlayerGame() {
    Board b = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
    GameInfo game = new GameInfo(b, clans);
    Territory t1 = new BasicTerritory("Jiangsu");
    Territory t2 = new BasicTerritory("Zhejiang");
    Territory t3 = new BasicTerritory("Shanghai");
    Territory t4 = new BasicTerritory("Anhui");
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(t1)));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(t2)));
    Clan c3 = new Clan(new LinkedList<Territory>(Arrays.asList(t3)));
    Clan c4 = new Clan(new LinkedList<Territory>(Arrays.asList(t4)));
    clans.put("Red", c1);
    clans.put("Blue", c2);
    clans.put("Yellow", c3);
    clans.put("Green", c4);
    t1.addUnit(new BasicUnit(4));
    t2.addUnit(new BasicUnit(3));
    t3.addUnit(new BasicUnit(1));
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t3, t4)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return game;
  }

}
