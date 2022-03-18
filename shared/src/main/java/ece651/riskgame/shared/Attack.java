package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.Map;

public class Attack extends MigrationAction implements Serializable {

    public Attack(Unit attackUnit, String fromTerritory, String toTerritory, String color) {
        super(attackUnit, fromTerritory, toTerritory, color);
    }

    @Override
    public void apply(Actable world) {
        Board board = world.getBoard();
        Map<String, Clan> clans = world.getClans();

        Clan c = clans.get(color);
        Territory t = board.getTerritory(toTerritory);

        if(c.occupyTerritory(toTerritory)) {
            t.addUnit(Unit);
            return;
        }

        if(t.beAttacked(getMoveUnit())) {
            for(Clan i : clans.values()) {
                if(i.occupyTerritory(toTerritory)) {
                    i.removeTerritory(t);
                    break;
                }
            }
            c.addTerritory(t);
        }

    }
  public void clientApply(Actable game) {
    onTheWay(game);
  }

    public void onTheWay(Actable world) {
        Board board = world.getBoard();

        board.getTerritory(fromTerritory).decUnit(Unit);
    }
}
