package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import ece651.riskgame.shared.GameInfo;
import javassist.bytecode.ByteArray;

@ExtendWith(MockitoExtension.class)
public class RiskGameTest {

  @InjectMocks
  private RiskGame riskGame;
  
  @Test
  public void test_getCurrentGameInfo() throws Exception{
    riskGame = new RiskGame();
    Whitebox.invokeMethod(riskGame, "initBoard", 1);
    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    GameInfo gi = (GameInfo)obj;
    assertEquals(1, gi.getBoard().getTerritories().size());
  }

  //  @Test
  public void test_sendGameInfo() throws IOException, Exception{
    riskGame = new RiskGame();
    Whitebox.invokeMethod(riskGame, "initBoard", 1);
    
    Socket socket = Mockito.mock(Socket.class);
    Map<Socket, Integer> sm = new HashMap<Socket, Integer>();
    sm.put(socket, 1);
    Whitebox.setInternalState(riskGame, "sockets", sm);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);

    Whitebox.invokeMethod(riskGame, "sendGameInfo");

    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    
  }

}
