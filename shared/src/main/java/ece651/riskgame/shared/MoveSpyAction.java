package ece651.riskgame.shared;

import java.io.Serializable;

public class MoveSpyAction implements Action, Serializable {
    private final String color;
    private final String fromTerritory;
    private final String toTerritory;

    private Logger logger = Logger.getInstance();
    public MoveSpyAction(String color, String fromTerritory, String toTerritory) {
        this.color = color;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
    }

    public String getColor() {
        return color;
    }

    public String getFromTerritory() {
        return fromTerritory;
    }

    public String getToTerritory() {
        return toTerritory;
    }

    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        Spy spy = clan.getSpy(fromTerritory, true);
        spy.moveTo(toTerritory);
        String s = "[RiscGame Room] : " + color + " mvoe his Spy from " + fromTerritory + " to " + toTerritory;
        // cost resource
        if (world.getTerritoryOwnership(fromTerritory).equals(color) && world.getTerritoryOwnership(toTerritory).equals(color)) {
            int cost = world.getUnitMoveCost(fromTerritory, color).get(toTerritory);
            clan.getResource().costFood(cost);
            s += ". Cost food " + cost;
        }
        else {
            Board board = world.getBoard();
            int cost = board.getTerritory(fromTerritory).getSize() + board.getTerritory(toTerritory).getSize();
            clan.getResource().costFood(cost);
            s += ". Cost food " + cost;
            spy.setCanMove(false);
        }
        logger.writeLog(s);
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
