package ece651.riskgame.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import static org.mockito.Mockito.*;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AppTest {
  @Test
  public void test_main() throws Exception {
    // RiskGame riskGame = new RiskGame(2);
    String serverOn = "on";
    Thread th_server = new Thread() {
      @Override()
      public void run() {
        try {
          // riskGame.run(2020);
          ServerSocket ss = new ServerSocket(2020);
          Socket s = ss.accept();
          ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
          ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
          System.out.println("Server is connected");

          String color = "Red";
          Board board = new Board();
          Map<String, Clan> clans = new HashMap<String, Clan>();
          GameInfo gi = new GameInfo(board, clans);
          oos.writeObject(color);
          oos.writeObject(clans);

          // this.interrupt();
          throw new RuntimeException();
        } catch (Exception e) {

        }
      }
    };

    Thread th_client = new Thread() {
      @Override()
      public void run() {
        try {
          App.main(new String[] { "0.0.0.0", "2020" });
        } catch (Exception e) {

        }
      }
    };
    th_server.start();
    th_client.start();
  }

  @Test
  public void test_printWaitingMsg() throws Exception {
    App app = new App(null, null, null);
    Whitebox.invokeMethod(app, "printWaitingMsg");
  }

  @Test
  public void test_recvGame() throws Exception {
    String color = "Red";
    Board board = new Board();
    Map<String, Clan> clans = new HashMap<String, Clan>();
    GameInfo gi = new GameInfo(board, clans);

    Thread th_server = new Thread() {
      @Override()
      public void run() {
        try {
          // riskGame.run(2020);
          ServerSocket ss = new ServerSocket(2030);
          Socket s = ss.accept();
          ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
          ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
          System.out.println("Server is connected");

          oos.writeObject(gi);
        } catch (Exception e) {

        }
      }
    };
    th_server.start();
    Thread.sleep(100);

    Socket socket = new Socket("0.0.0.0", 2030);
    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

    TextPlayer tp = new TextPlayer("Red", gi);
    App app = new App(tp, ois, oos);
    Whitebox.invokeMethod(app, "recvGame");

    th_server.interrupt();
    th_server.join();
  }



  @Test
  public void test_dopostdeath() throws IOException, ClassNotFoundException {
    TextPlayer t = mock(TextPlayer.class);

    ObjectInputStream oi = mock(ObjectInputStream.class);
    ObjectOutputStream os = mock(ObjectOutputStream.class);
    App a = spy(new App(t, oi, os));

    when(t.getPostDeathChoice()).thenReturn("Q");
    when(t.isGameOver()).thenReturn(false);
    doNothing().when(t).update(any(GameInfo.class));
    doNothing().when(t).doGameOverPhase();
    doNothing().when(oi).close();
    doNothing().when(os).close();

    a.doPostDeathPhase();
  }

}
