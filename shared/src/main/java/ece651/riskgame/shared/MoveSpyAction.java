package ece651.riskgame.shared;

import java.io.Serializable;

public class MoveSpyAction implements Action, Serializable {
    private String color;
    private String fromTerritory;
    private String toTerritory;

    public MoveSpyAction(String color, String fromTerritory, String toTerritory) {
        this.color = color;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
    }

    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        Spy spy = clan.getSpy(fromTerritory, true);
        spy.moveTo(toTerritory);
        spy.setCanMove(false);
        // cost resource
        if (world.getTerritoryOwnership(fromTerritory).equals(color) && world.getTerritoryOwnership(fromTerritory).equals(color)) {
            clan.getResource().costFood(1);
        }
        else {
            clan.getResource().costFood(world.getUnitMoveCost(fromTerritory, color).get(toTerritory));
        }
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
