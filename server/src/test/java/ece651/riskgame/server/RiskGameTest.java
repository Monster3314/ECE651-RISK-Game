package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import ece651.riskgame.shared.GameInfo;

@ExtendWith(MockitoExtension.class)
public class RiskGameTest {

  private RiskGame riskGame;
  
  @Test
  public void test_getCurrentGameInfo() throws Exception{
    riskGame = new RiskGame(1);
    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    GameInfo gi = (GameInfo)obj;
    assertEquals(1, gi.getBoard().getTerritoriesSet().size());
  }
  
  @Test
  public void test_waitForPlayers() throws IOException, InterruptedException {
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
  }

  @Test
  public void test_sendGameInfo() throws IOException, Exception{
    riskGame = new RiskGame(1);

    // insert socket map
    Socket socket = Mockito.mock(Socket.class);
    Map<Socket, Integer> sm = new HashMap<Socket, Integer>();
    sm.put(socket, 1);
    Whitebox.setInternalState(riskGame, "sockets", sm);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);

    // get GameInfo
    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    GameInfo gi = (GameInfo)obj;

    // get byte array
    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
    ObjectOutputStream oos1 = new ObjectOutputStream(bos) ;
    oos1.writeObject(gi);

    Whitebox.invokeMethod(riskGame, "sendGameInfo", gi);
    assertArrayEquals(byteArrayOutputStream.toByteArray(), bos.toByteArray());
  }

}
