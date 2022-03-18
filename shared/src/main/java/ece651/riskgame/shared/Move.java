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

        board.getTerritory(fromTerritory).decUnit(Unit);
        board.getTerritory(toTerritory).addUnit(Unit);
    }
    @Override
    public void clientApply(Actable game) {
      apply(game);
    }

    public String getDst() {
        return toTerritory;
    }

    public Unit getUnit() {
        return Unit;
    }
  
}
