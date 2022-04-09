package ece651.riskgame.shared;

import java.io.Serializable;

public class UpgradeTechAction implements Action, Serializable {
    private final String color;

    public UpgradeTechAction(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        clan.upgradeLevel();
    }

    @Override
    public void clientApply(Actable game) {
        Clan clan = game.getClans().get(color);
        clan.getResource().costGold(Clan.COST[clan.getMaxTechLevel()]);
    }
}
