package ece651.riskgame.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import ece651.riskgame.shared.GameInfo;

public class PlayerTest {
  @Test
  public void test_PlayerConstructer() {
    GameInfo game = mock(GameInfo.class);
    when(game.getClans().containsKey("Red")).thenReturn(true);
    when(game.getClans().containsKey("Blue")).thenReturn(false);
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer(null, game));
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer("Red", null));
    assertThrows(IllegalArgumentException.class, () -> new TextPlayer("Blue", game));
    assertDoesNotThrow(() -> new TextPlayer("Red", game));
  }
  @Test
  public void test_tryApplyAction() {
    String color = "red";
    GameInfo game = mock(GameInfo.class);
    

  }
  @Test
  public void test_updateGame() {
    GameInfo oldGame = mock(GameInfo.class);
    GameInfo newGame = mock(GameInfo.class);
    GameInfo invalidGame = mock(GameInfo.class);
    when(oldGame.getClans().containsKey("Red")).thenReturn(true);
    when(newGame.getClans().containsKey("Red")).thenReturn(true);
    when(invalidGame.getClans().containsKey("Red")).thenReturn(false);
    Player p = new TextPlayer("Red", oldGame);
    assertEquals(p.getGame(), oldGame);
    p.updateGame(newGame);
    assertEquals(p.getGame(), newGame);
    assertThrows(IllegalArgumentException.class, ()->p.updateGame(null));
    assertThrows(IllegalArgumentException.class, ()->p.updateGame(invalidGame));
  }

}
