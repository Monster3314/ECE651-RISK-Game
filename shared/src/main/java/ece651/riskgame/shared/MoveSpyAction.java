package ece651.riskgame.shared;

import java.io.Serializable;

public class MoveSpyAction implements Action, Serializable {
    private final String color;
    private final String fromTerritory;
    private final String toTerritory;

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
        // cost resource
        if (world.getTerritoryOwnership(fromTerritory).equals(color) && world.getTerritoryOwnership(toTerritory).equals(color)) {
            clan.getResource().costFood(world.getUnitMoveCost(fromTerritory, color).get(toTerritory));
        }
        else {
            clan.getResource().costFood(1);
            spy.setCanMove(false);
        }
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
