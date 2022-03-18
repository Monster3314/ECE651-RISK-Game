package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MigrationActionTest {

  public class DemoMigrationAction extends MigrationAction {
    public DemoMigrationAction(Unit moveUnit, String fromTerritory, String toTerritory, String color) {
      super(moveUnit, fromTerritory, toTerritory, color);
    }

    @Override
    public void apply(Actable world) {

    }
  }
  
  @Test
  public void test_all() {
    MigrationAction ma = Mockito.spy(new DemoMigrationAction(null, "from", "to", "color"));
    assertEquals(null, ma.getMoveUnit());
    assertEquals("from", ma.getFromTerritory());
    assertEquals("to", ma.getToTerritory());
  }

}
