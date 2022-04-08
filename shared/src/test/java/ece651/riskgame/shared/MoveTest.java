package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class MoveTest {

  @Test
  void test_constructor_getter_setter() {    
    List<Unit> units = List.of(new BasicUnit(10));
    Move move = new Move(units, "SD", "JS", null);
    assertEquals("JS", move.getToTerritory());
    assertEquals(units, move.getUnit());
  }

  @Disabled
  @Test
  void test_apply() {
    Board b = new Board();
    Territory t1 = new BasicTerritory("HK");
    Territory t2 = new BasicTerritory("Taiwan");
    t1.addUnit(new BasicUnit(15));
    t2.addUnit(new BasicUnit(5));
    b.addTerritory(t1);
    b.addTerritory(t2);
    b.putEntry(t1, List.of(t2));
    b.putEntry(t2, List.of(t1));

    Actable fakeWorld = mock(Actable.class);
    when(fakeWorld.getBoard()).thenReturn(b);


    assertEquals(15, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
    assertEquals(5, fakeWorld.getBoard().getTerritory("Taiwan").getUnits().get(0).getNum());
    
    Unit units = new BasicUnit(10);
    Move move = new Move(List.of(units), "HK", "Taiwan", "Red");
    assertEquals("Taiwan", move.getToTerritory());
    move.clientApply(fakeWorld);

   assertEquals(5, fakeWorld.getBoard().getTerritory("HK").getUnits().get(0).getNum());
   assertEquals(15, fakeWorld.getBoard().getTerritory("Taiwan").getUnits().get(0).getNum()); 
  }

}
