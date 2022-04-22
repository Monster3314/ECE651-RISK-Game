package ece651.riskgame.shared;

import java.io.Serializable;

public class DoCloakAction implements Action, Serializable {
    private String color;
    private String territoryName;

    public DoCloakAction(String color, String territoryName) {
        this.color = color;
        this.territoryName = territoryName;
    }

    public String getColor() {
        return color;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        clan.getResource().costGold(20);
        Territory t = world.getBoard().getTerritory(territoryName);
        t.setCloakNum(4);
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
