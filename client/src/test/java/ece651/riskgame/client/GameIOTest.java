package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Unit;

public class GameIOTest {
  @BeforeAll
  public static void start() {
    System.out.println("GameIOTest starting...");
  }
  @Test
  public void test_GameIO_Socket() throws Exception {
    Thread serverThread = new Thread() {
        @Override
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1751);
            Socket client = ss.accept(); 
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            oos.writeObject(new String("Red"));
            oos.writeObject(new GameInfo(new Board(), new HashMap<String, Clan>()));
            oos.writeObject(new ArrayList<Unit>());
            assertTrue(ois.readObject() instanceof Map);
            oos.writeObject(new GameInfo(new Board(), new HashMap<String, Clan>()));
            assertTrue(ois.readObject() instanceof List);
            client.close();
            ss.close();
          } catch (Exception e) {
          }
        }
      };
    serverThread.start();
    Thread.sleep(100);

    Socket server = new Socket("0.0.0.0", 1751);
    assertTrue(server.isConnected());
    GameIO io = new GameIO(server);
    assertEquals(io.recvColor(), "Red");
    assertTrue(io.recvGame() instanceof GameInfo);
    assertTrue(io.recvUnitsToPlace() instanceof List);
    io.sendPlacements(new HashMap<String, List<Unit>>());
    assertTrue(io.recvGame() instanceof GameInfo);
    io.sendActions(new ArrayList<Action>());
    io.close();
    server.close();

    serverThread.join();
  }
  @Test
  public void test_GameIO_ObjectStream() throws Exception {
    Thread serverThread = new Thread() {
        @Override
        public void run() {
          try {
            ServerSocket ss = new ServerSocket(1751);
            Socket client = ss.accept(); 
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            oos.writeObject(new String("Red"));
            oos.writeObject(new GameInfo(new Board(), new HashMap<String, Clan>()));
            oos.writeObject(new ArrayList<Unit>());
            assertTrue(ois.readObject() instanceof Map);
            oos.writeObject(new GameInfo(new Board(), new HashMap<String, Clan>()));
            assertTrue(ois.readObject() instanceof List);
            client.close();
            ss.close();
          } catch (Exception e) {
          }
        }
      };
    serverThread.start();
    Thread.sleep(100);

    Socket server = new Socket("0.0.0.0", 1751);
    assertTrue(server.isConnected());
    ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
    GameIO io = new GameIO(ois, oos);
    assertEquals(io.recvColor(), "Red");
    assertTrue(io.recvGame() instanceof GameInfo);
    assertTrue(io.recvUnitsToPlace() instanceof List);
    io.sendPlacements(new HashMap<String, List<Unit>>());
    assertTrue(io.recvGame() instanceof GameInfo);
    io.sendActions(new ArrayList<Action>());
    io.setSocket(server);
    io.close();
    server.close();

    serverThread.join();
  }

}
