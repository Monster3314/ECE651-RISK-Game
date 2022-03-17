package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.Map;

public class Move implements Action, Serializable {

    private Unit moveUnit;
    private String fromTerritory;
    private String toTerritory;

    public Move(Unit moveUnit, String fromTerritory, String toTerritory) {
        this.moveUnit = moveUnit;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
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
