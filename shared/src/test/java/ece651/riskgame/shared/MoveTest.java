package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveTest {

  @Test
  void test_constructor_getter_setter() {    
    Unit units = new BasicUnit(10);
    Move move = new Move(List.of(units), "SD", "JS", null);
    assertEquals("JS", move.getToTerritory());
  }

  @Disabled
  @Test
  void test_apply() {
    Actable act = mock(Actable.class);
    List<Unit> list = new ArrayList<>();
    list.add(mock(Unit.class));    
    Move move = new Move(list, null, null, null);

    Board board = mock(Board.class);
    when(act.getBoard()).thenReturn(board);
    Territory t = mock(Territory.class);
    doReturn(t).when(board).getTerritory(any());
    doNothing().when(t).addUnit(any());
    doNothing().when(t).decUnit(any());
    
    
  }
  
}
