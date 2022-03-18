package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.Map;

public class Move extends MigrationAction implements Serializable {

    public Move(Unit moveUnit, String fromTerritory, String toTerritory, String color) {
        super(moveUnit, fromTerritory, toTerritory, color);
    }

    @Override
    public void apply(Actable world) {
        Board board = world.getBoard();

        for(Territory t: board.getTerritoriesList()) {
            if(t.getName().equals(fromTerritory)) {
                t.decUnit(moveUnit);
            }
            if(t.getName().equals(toTerritory)) {
                t.addUnit(moveUnit);
            }
        }
    }
  public String getDst() {
    return toTerritory;
  }
  public Unit getUnit() {
    return moveUnit;
  }
  
}
