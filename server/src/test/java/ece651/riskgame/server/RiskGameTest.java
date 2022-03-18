package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

@ExtendWith(MockitoExtension.class)
public class RiskGameTest {

  private RiskGame riskGame;
  
  @Test
  public void test_getCurrentGameInfo() throws Exception{
    riskGame = new RiskGame(1);
    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    GameInfo gi = (GameInfo)obj;
    assertEquals(3, gi.getBoard().getTerritoriesSet().size());
  }
  
  @Test
  public void test_waitForPlayers1() throws IOException, InterruptedException {
    riskGame = new RiskGame(1);
    Thread th = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(2651);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);
          } catch (Exception e) {
            
          }
        }
      };
    th.start();
    Thread.sleep(100); // this is abit of a hack

    Socket s = new Socket("0.0.0.0", 2651);
    assertTrue(s.isConnected());    
    s.close();
    
    th.interrupt();
    th.join();
    //System.out.println("Test wait for 1 player complete");
  }

  @Test
  public void test_waitForPlayers2() throws IOException, InterruptedException {
    riskGame = new RiskGame(2);
    Thread th_server = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(2751);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 2);
          } catch (Exception e) {
            
          }
        }
      };
    th_server.start();
    Thread.sleep(100); // this is abit of a hack

    Socket s1 = new Socket("0.0.0.0", 2751);
    Socket s2 = new Socket("0.0.0.0", 2751);
    assertTrue(s1.isConnected());
    assertTrue(s2.isConnected());
    s1.close();
    s2.close();
    
    th_server.interrupt();
    th_server.join();

    //    System.out.println("Test wait for 2 player complete");
  }

  @Test
  public void test_init1Player1() throws IOException, InterruptedException, ClassNotFoundException {
    riskGame = new RiskGame(1);
    Thread th = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(2801);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);
            Whitebox.invokeMethod(riskGame, "initPlayers");            
          } catch (Exception e) {
            
          }
        }
      };
    th.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 2801);
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());
    String color = (String)ois.readObject();
    assertEquals("Red", color);    
    s1.close();
    th.interrupt();
    th.join();
    //    System.out.println("Test initplayers complete");
  }
  
  @Test
  public void test_init2Players() throws IOException, InterruptedException, ClassNotFoundException {
    riskGame = new RiskGame(2);
    Thread th = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(2851);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 2);
            Whitebox.invokeMethod(riskGame, "initPlayers");            
          } catch (Exception e) {
            
          }
        }
      };
    th.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 2851);
    Thread.sleep(100);
    Socket s2 = new Socket("0.0.0.0", 2851);
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());
    ObjectInputStream ois2 = new ObjectInputStream(s2.getInputStream());
    ObjectOutputStream oos2 = new ObjectOutputStream(s2.getOutputStream());
    String color = (String)ois.readObject();    
    String color2 = (String)ois2.readObject();
    assertTrue(color.equals("Red") || color.equals("Blue"));
    assertTrue(color2.equals("Red") || color2.equals("Blue")); // The order of Red and Blue is not as expected
    s1.close();
    s2.close();    
    th.interrupt();
    th.join();
    //    System.out.println("Test initplayers complete");
  }

  
  @Test
  public void test_sendGameInfo() throws Exception {
    riskGame = new RiskGame(1);
    GameInfo gi_expected = new GameInfo(new Board(), new HashMap<String, Clan>());
    Thread th_server = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1751);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);            
            Whitebox.invokeMethod(riskGame, "sendGameInfo", gi_expected);
          } catch (Exception e) {
            
          }
        }
      };
    th_server.start();
    Thread.sleep(100); // this is abit of a hack

    Socket s1 = new Socket("0.0.0.0", 1751);
    assertTrue(s1.isConnected());
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());
    GameInfo gi = (GameInfo) ois.readObject();
    assertEquals(gi_expected.getBoard(), gi.getBoard());
    
    s1.close();
    
    th_server.interrupt();
    th_server.join();
  }

  @Test
  public void test_assignUnitsto1() throws Exception {
    riskGame = new RiskGame(1);
    GameInfo gi_expected = new GameInfo(new Board(), new HashMap<String, Clan>());
    Thread th_server = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1761);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);            
            Whitebox.invokeMethod(riskGame, "initPlayers");            
            Whitebox.invokeMethod(riskGame, "assignUnits", 10);
            // TODO check result
          } catch (Exception e) {
            
          }
        }
      };
    th_server.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 1761);
    assertTrue(s1.isConnected());
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());

    ois.readObject(); // read color
    List<Unit> needToAssign = (List<Unit>) ois.readObject();
    assertEquals(10, needToAssign.get(0).getNum());

    Map<String, List<Unit>> assignResults = new HashMap<String, List<Unit>>();
    List<Unit> l1 = new ArrayList<Unit>();
    List<Unit> l2 = new ArrayList<Unit>();
    List<Unit> l3 = new ArrayList<Unit>();
    l1.add(new BasicUnit(2));
    l2.add(new BasicUnit(3));
    l3.add(new BasicUnit(4));
    assignResults.put("Shanghai", l1);
    assignResults.put("Jiangsu", l2);
    assignResults.put("Zhejiang", l3);
    oos.writeObject(assignResults);    
    
    s1.close();
    
    th_server.interrupt();
    th_server.join();
  }

  @Test
  public void test_doAction() throws IOException, ClassNotFoundException, InterruptedException {
    riskGame = new RiskGame(1);
    GameInfo gi_expected = new GameInfo(new Board(), new HashMap<String, Clan>());
    Thread th_server = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket ss = new ServerSocket(1771);
          Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);
          Whitebox.invokeMethod(riskGame, "initPlayers");
          Whitebox.invokeMethod(riskGame, "assignUnits", 10);
          Whitebox.invokeMethod(riskGame, "doAction");
          Whitebox.invokeMethod(riskGame, "afterTurn");
          // TODO check result
        } catch (Exception e) {

        }
      }
    };
    th_server.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 1771);
    assertTrue(s1.isConnected());
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());

    ois.readObject(); // read color
    List<Unit> needToAssign = (List<Unit>) ois.readObject();
    assertEquals(10, needToAssign.get(0).getNum());

    Map<String, List<Unit>> assignResults = new HashMap<>();
    List<Unit> l1 = new ArrayList<>();
    List<Unit> l2 = new ArrayList<>();
    List<Unit> l3 = new ArrayList<>();
    l1.add(new BasicUnit(2));
    l2.add(new BasicUnit(3));
    l3.add(new BasicUnit(5));
    assignResults.put("Shanghai", l1);
    assignResults.put("Jiangsu", l2);
    assignResults.put("Zhejiang", l3);
    oos.writeObject(assignResults);

    List<Action> actions = new ArrayList<>();
    actions.add(new Move(new BasicUnit(2), "Shanghai", "Jiangsu", "Red"));
    actions.add(new Attack(new BasicUnit(3), "Jiangsu", "Zhejiang", "Red"));
    oos.writeObject(actions);

    s1.close();

    th_server.interrupt();
    th_server.join();
  }

  @Test
  public void test_doMoveAction() throws IOException, Exception {
    RiskGame riskGame = new RiskGame(2);
    // add units to add territories
    World world = Whitebox.getInternalState(riskGame, "world");
    Board board = world.getBoard();
    for (Territory t: board.getTerritoriesList()) {
      t.addUnit(new BasicUnit(5));
    }
    world.addClan();
    world.addClan();

    // do the move
    List<Move> moves0 = new ArrayList<Move>();
    moves0.add(new Move(new BasicUnit(2), "Shanghai", "Shandong", "Red"));

    Whitebox.invokeMethod(riskGame, "doMoveAction", moves0);
    assertEquals(5, board.getTerritory("Shanghai").getUnits().get(0).getNum());
    assertEquals(5, board.getTerritory("Shandong").getUnits().get(0).getNum());
    
    List<Move> moves1 = new ArrayList<Move>();
    moves1.add(new Move(new BasicUnit(2), "Shanghai", "Jiangsu", "Red"));

    Whitebox.invokeMethod(riskGame, "doMoveAction", moves1);
    assertEquals(3, board.getTerritory("Shanghai").getUnits().get(0).getNum());
    assertEquals(7, board.getTerritory("Jiangsu").getUnits().get(0).getNum());        
  }

  
  @Test
  public void test_doAttackAction() throws IOException, Exception {
    RiskGame riskGame = new RiskGame(2);
    // add units to add territories
    World world = Whitebox.getInternalState(riskGame, "world");
    Board board = world.getBoard();
    for (Territory t: board.getTerritoriesList()) {
      Unit bu = Mockito.spy(new BasicUnit(5));
      if (t.getName().equals("Jiangsu")) {
        when(bu.getRandomAttack()).thenReturn(-1); // Shandong ppl can dominate anyone
      }
      t.addUnit(bu);
    }
    world.addClan();
    world.addClan();

    // do the move
    List<Attack> attacks0 = new ArrayList<Attack>();
    attacks0.add(new Attack(Mockito.spy(new BasicUnit(3)), "Shanghai", "Shandong", "Red"));
    attacks0.add(new Attack(Mockito.spy(new BasicUnit(3)), "Shanghai", "Jiangsu", "Red"));

    Whitebox.invokeMethod(riskGame, "doAttackAction", attacks0);
    assertEquals(5, board.getTerritory("Shanghai").getUnits().get(0).getNum());
    assertEquals(5, board.getTerritory("Shandong").getUnits().get(0).getNum());
    
    List<Attack> attacks1 = new ArrayList<Attack>();
    attacks1.add(new Attack(Mockito.spy(new BasicUnit(2)), "Shandong", "Jiangsu", "Blue"));

    Whitebox.invokeMethod(riskGame, "doAttackAction", attacks1);
    assertEquals(3, board.getTerritory("Shandong").getUnits().get(0).getNum());
    assertEquals(2, board.getTerritory("Jiangsu").getUnits().get(0).getNum());        
  }
}
