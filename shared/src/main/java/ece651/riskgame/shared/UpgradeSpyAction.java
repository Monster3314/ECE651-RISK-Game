package ece651.riskgame.shared;

import java.io.Serializable;

public class UpgradeSpyAction implements Action, Serializable {
    private final String color;
    private final String territoryName;

    public UpgradeSpyAction(String color, String territory) {
        this.color = color;
        this.territoryName = territory;
    }

    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        clan.getResource().costGold(20);
        Territory territory = world.getBoard().getTerritory(territoryName);
        territory.decUnit(new BasicUnit());
        clan.addSpy(new Spy(territoryName));
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
