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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ece651.riskgame.server.World;
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
    ClientWorld game = mock(ClientWorld.class);
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
    ClientWorld game = getDefaultWorld();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    redPlayer.adaptPlacements(List.of(new PlaceAction(new BasicUnit(), "Durham")));
  }
  @Test
  public void test_adaptPlacements() {
    ClientWorld game = getDefaultWorld();
    TextPlayer redPlayer = new TextPlayer("Red", game);
  }
  @Test
  public void test_updateGame() {
    ClientWorld w = getDefaultWorld();
    Player p = new TextPlayer("Red", w);
    GameInfo newGame = getGameUpdate();
    //p.updateGame(getGameUpdate());
    //assertEquals(newGame.getBoard(), p.getGame().getBoard());
    //assertEquals(newGame.getClans(), p.getGame().getClans());

    assertThrows(IllegalArgumentException.class, ()->p.updateGame(null));
    
    Map<String, Clan> invalidClans = mock(Map.class);
    GameInfo invalidGame = mock(GameInfo.class);
    when(invalidClans.containsKey("Red")).thenReturn(false);
    when(invalidGame.getClans()).thenReturn(invalidClans);
    assertThrows(IllegalArgumentException.class, ()->p.updateGame(invalidGame));
    
  }
  @Test
  public void test_getters() {
    GameInfo game = mock(GameInfo.class);
    Map<String, Clan> clans = mock(Map.class);
    when(clans.containsKey("Red")).thenReturn(true);
    when(clans.containsKey("Blue")).thenReturn(true);
    when(game.getClans()).thenReturn(clans);
    TextPlayer p = new TextPlayer("Red", new ClientWorld(game));
    
    try {
      p.isGameOver();
    } catch(Exception e) {
    }
  }

  @Test
  public void test_getTerritory() {
    ClientWorld game = getDefaultWorld();
    final TextPlayer redPlayer = new TextPlayer("Red", game);
    assertEquals("Durham", redPlayer.getTerritory("Durham").getName());
    assertEquals("Raleigh", redPlayer.getTerritory("Raleigh").getName());
    assertThrows(IllegalArgumentException.class, () -> redPlayer.getTerritory("Jiangsu"));
  }
  @Test
  public void test_occupyTerritory() {
    ClientWorld game = getDefaultWorld();
    final TextPlayer redPlayer = new TextPlayer("Red", game);
    assertTrue(redPlayer.occupyTerritory("Durham"));
    assertTrue(redPlayer.occupyTerritory("Cary"));
    assertFalse(redPlayer.occupyTerritory("Raleigh"));
    assertFalse(redPlayer.occupyTerritory("Chapel Hill"));
    assertThrows(IllegalArgumentException.class, () -> redPlayer.occupyTerritory("Jiangsu"));
  }
  @Test
  public void test_getEnemyTerritoryNames() {
    ClientWorld game = getDefaultWorld();
    TextPlayer redPlayer = new TextPlayer("Red", game);
    TextPlayer bluePlayer = new TextPlayer("Blue", game);
    assertEquals(new HashSet<String>(Arrays.asList("Raleigh", "Chapel Hill")), redPlayer.getEnemyTerritoryNames());
    assertEquals(new HashSet<String>(Arrays.asList("Durham", "Cary")), bluePlayer.getEnemyTerritoryNames());
  }
  @Test
  public void test_getTerritoryNames() {
    ClientWorld game = getDefaultWorld();
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
    ClientWorld game = getDefaultWorld();
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

    redPlayer.updateGame(getGameUpdate());
    bluePlayer.updateGame(getGameUpdate());

    assertEquals(3, redPlayer.getTechLevel());
    assertEquals(100, redPlayer.getFood());
    assertEquals(200, redPlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Durham", "Raleigh")), redPlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));
    
    assertEquals(6, bluePlayer.getTechLevel());    
    assertEquals(0, bluePlayer.getFood());
    assertEquals(0, bluePlayer.getGold());
    assertEquals(new HashSet<String>(Arrays.asList("Cary", "Chapel Hill")), bluePlayer.getOccupies().stream().map(t -> t.getName()).collect(Collectors.toSet()));
  }
  @Test
  public void test_hasVisibilityOf() {
    ClientWorld theGame = getFourPlayerWorld();
    final TextPlayer redPlayer = new TextPlayer("Red", theGame);
    assertTrue(redPlayer.hasVisibilityOf("Jiangsu"));
    assertTrue(redPlayer.hasVisibilityOf("Shanghai"));
    assertTrue(redPlayer.hasVisibilityOf("Anhui"));
    assertFalse(redPlayer.hasVisibilityOf("Zhejiang"));
    assertThrows(IllegalArgumentException.class, () -> redPlayer.hasVisibilityOf("Jiangxi"));
    assertThrows(IllegalArgumentException.class, () -> redPlayer.hasVisibilityOf("Guangzhou"));
    assertThrows(IllegalArgumentException.class, () -> redPlayer.hasVisibilityOf("Fujian"));

    final TextPlayer yellowPlayer = new TextPlayer("Yellow", theGame);
    assertTrue(yellowPlayer.hasVisibilityOf("Jiangsu"));
    assertTrue(yellowPlayer.hasVisibilityOf("Shanghai"));
    assertTrue(yellowPlayer.hasVisibilityOf("Zhejiang"));
    assertFalse(yellowPlayer.hasVisibilityOf("Anhui"));

    assertThrows(IllegalArgumentException.class, () -> yellowPlayer.hasVisibilityOf("Jiangxi"));
    assertThrows(IllegalArgumentException.class, () -> yellowPlayer.hasVisibilityOf("Guangzhou"));
    assertThrows(IllegalArgumentException.class, () -> yellowPlayer.hasVisibilityOf("Fujian"));
    
  }

  public static ClientWorld getDefaultWorld() {
    Board b = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
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
    return new ClientWorld(b, clans);
  }
  public static GameInfo getGameUpdate() {
    Board b = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
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
    return new GameInfo(b, clans);
  }
  public static ClientWorld getEmptyWorld() {
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
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
    return new ClientWorld(b, players);
  }
  public static ClientWorld getInitialWorld() {
    Board b = new Board();
    Map<String, Clan> players = new HashMap<String, Clan>();
    ClientWorld w = new ClientWorld(b, players);
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
    return w;
  }
  public static ClientWorld getFourPlayerWorld() {
   
    Map<String, Clan> clans = new HashMap<String, Clan>();
    Board b = getChinaBoard();
    Clan c1 = new Clan(new LinkedList<Territory>(Arrays.asList(b.getTerritory("Jiangsu"))));
    Clan c2 = new Clan(new LinkedList<Territory>(Arrays.asList(b.getTerritory("Zhejiang"))));
    Clan c3 = new Clan(new LinkedList<Territory>(Arrays.asList(b.getTerritory("Shanghai"))));
    Clan c4 = new Clan(new LinkedList<Territory>(Arrays.asList(b.getTerritory("Anhui"))));
    clans.put("Red", c1);
    clans.put("Blue", c2);
    clans.put("Yellow", c3);
    clans.put("Green", c4);


    return new ClientWorld(b, clans);
  }
  
  public static Board getNCBoard() {
    Board b = new Board();
    Territory t1 = new BasicTerritory("Jiangsu");
    Territory t2 = new BasicTerritory("Zhejiang");
    Territory t3 = new BasicTerritory("Shanghai");
    Territory t4 = new BasicTerritory("Anhui");
    b.addTerritory(t1);
    b.putEntry(t1, new LinkedList<Territory>(Arrays.asList(t3, t4)));
    b.addTerritory(t2);
    b.putEntry(t2, new LinkedList<Territory>(Arrays.asList(t3)));
    b.addTerritory(t3);
    b.putEntry(t3, new LinkedList<Territory>(Arrays.asList(t1, t2)));
    b.addTerritory(t4);
    b.putEntry(t4, new LinkedList<Territory>(Arrays.asList(t1)));
    return b;
  }

  public static Board getChinaBoard() {
    Board b = new Board();
    Territory t1 = new BasicTerritory("Jiangsu");
    Territory t2 = new BasicTerritory("Zhejiang");
    Territory t3 = new BasicTerritory("Shanghai");
    Territory t4 = new BasicTerritory("Anhui");
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
    return b;
  }

}
