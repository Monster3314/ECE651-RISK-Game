package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnitsRuleCheckerTest {

  public class DemoUnit extends Unit {
    public DemoUnit(int number) {
      super(2, 2, number);
    }

    @Override
    public int getRandomAttack() {
      // TODO Auto-generated method stub
      return 0;
    }
  }

  
  @Test
  public void test_checkMyRule() {
    Board b = new Board();
    Territory t = new BasicTerritory("from");
    b.addTerritory(t);

    Clan clan = new Clan();
    Map<String, Clan> map = new HashMap<String, Clan>();
    map.put("Red", clan);

    Actable actable = Mockito.mock(Actable.class);
    when(actable.getBoard()).thenReturn(b);

    Unit unit = new BasicUnit(5);
    Action act = new Move(unit, "from", "to", "color");

    ActionRuleChecker urc = new UnitsRuleChecker(null);

    assertEquals("No specified Unit found", urc.checkMyRule(actable, act));

    b.getTerritory("from").addUnit(new BasicUnit(5));
    assertEquals(null, urc.checkMyRule(actable, act));

    b.getTerritory("from").decUnit(new BasicUnit(1));
    when(actable.getBoard()).thenReturn(b);
    assertEquals("No enough number of Unit remaining", urc.checkMyRule(actable, act));

    Unit unit2 = new DemoUnit(5);
    Action act2 = new Move(unit2, "from", "to", "color");
    assertEquals("No specified Unit found", urc.checkMyRule(actable, act2));

    // irrelevent test for coverage
    assertEquals(0, unit2.getRandomAttack());
  }

}
