package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

// TODO: Rename
public class Move extends MigrationAction implements Serializable {

    public Move(List<Unit> moveUnit, String fromTerritory, String toTerritory, String color) {
        super(moveUnit, fromTerritory, toTerritory, color);
    }

    @Override
    public void apply(Actable world) {
        Board board = world.getBoard();

        int unitCost = world.getUnitMoveCost(fromTerritory, color).get(toTerritory);
        world.getClans().get(color).getResource().costFood(unitCost * getTotalUnits());
        Logger.getInstance().writeLog("[RiscGame Room] : " + color + " player moves " + getUnit() + " from " + getFromTerritory() + " to " + getToTerritory() + ". Cost food " + unitCost * getTotalUnits());
        for (Unit unit : units) {
            board.getTerritory(fromTerritory).decUnit(unit);
            board.getTerritory(toTerritory).addUnit(unit);
        }
    }
    @Override
    public void clientApply(Actable game) {
      apply(game);
    }
  
}
