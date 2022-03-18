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

        if(c.occupyTerritory(toTerritory)) return; // player has already get this territory

        Territory t = board.getTerritory(toTerritory);
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

    public void onTheWay(Actable world) {
        Board board = world.getBoard();

        board.getTerritory(fromTerritory).decUnit(Unit);
    }
}
