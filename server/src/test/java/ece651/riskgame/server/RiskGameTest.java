package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import ece651.riskgame.shared.GameInfo;

@ExtendWith(MockitoExtension.class)
public class RiskGameTest {

  @InjectMocks
  private RiskGame riskGame;
  
  @Test
  public void test_getCurrentGameInfo() throws Exception{
    riskGame = new RiskGame();
    Whitebox.invokeMethod(riskGame, "initGameMap", 1);
    Object obj = Whitebox.invokeMethod(riskGame, "getCurrentGameInfo");
    GameInfo gi = (GameInfo)obj;
    assertEquals(1, gi.getBoard().getTerritories().size());
  }

}
