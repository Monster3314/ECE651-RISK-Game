package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;

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
  public void test_waitForPlayers1() throws IOException, InterruptedException, ClassNotFoundException {
    riskGame = new RiskGame(1);
    Thread th = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1651);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 1);
          } catch (Exception e) {
            
          }
        }
      };
    th.start();
    Thread.sleep(100); // this is abit of a hack

    Socket s = new Socket("0.0.0.0", 1651);
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
            ServerSocket ss = new ServerSocket(1651);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 2);
          } catch (Exception e) {
            
          }
        }
      };
    th_server.start();
    Thread.sleep(100); // this is abit of a hack

    Socket s1 = new Socket("0.0.0.0", 1651);
    Socket s2 = new Socket("0.0.0.0", 1651);
    assertTrue(s1.isConnected());
    assertTrue(s2.isConnected());
    s1.close();
    s2.close();
    
    th_server.interrupt();
    th_server.join();

    //    System.out.println("Test wait for 2 player complete");
  }

  @Test
  public void test_initPlayers() throws IOException, InterruptedException, ClassNotFoundException {
    riskGame = new RiskGame(2);
    Thread th = new Thread() {
        @Override()
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1651);            
            Whitebox.invokeMethod(riskGame, "waitForPlayers", ss, 2);
            Whitebox.invokeMethod(riskGame, "initPlayers");            
          } catch (Exception e) {
            
          }
        }
      };
    th.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 1651);
    Socket s2 = new Socket("0.0.0.0", 1651);
    ObjectInputStream ois = new ObjectInputStream(s1.getInputStream());
    String color = (String)ois.readObject();
    assertEquals("Red", color);    
    ObjectInputStream ois2 = new ObjectInputStream(s2.getInputStream());
    String color2 = (String)ois2.readObject();
    assertEquals("Blue", color2);    
    s1.close();
    s2.close();    
    th.interrupt();
    th.join();
    //    System.out.println("Test initplayers complete");
  }

  
  @Test
  public void test_sendGameInfo() throws IOException, InterruptedException, ClassNotFoundException, Exception {
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
    GameInfo gi = (GameInfo) ois.readObject();
    assertEquals(gi_expected.getBoard(), gi.getBoard());
    
    s1.close();
    
    th_server.interrupt();
    th_server.join();
  }
}
