package ece651.riskgame.shared;

import java.io.Serializable;

public class UpgradeUnitAction implements Action, Serializable {
    private final String territoryName;
    private final int baseLevel;
    private final int targetLevel;
    private final int num;
    private final String color;

    public UpgradeUnitAction(String territoryName, int baseLevel, int targetLevel, int num, String color) {
        if (num <= 0) {
            throw new IllegalArgumentException("Please enter a positive number.");
        }
        if (baseLevel >= targetLevel) {
            throw new IllegalArgumentException("Target level should be higher than base level.");
        }
        this.territoryName = territoryName;
        this.baseLevel = baseLevel;
        this.targetLevel = targetLevel;
        this.num = num;
        this.color = color;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public int getNum() {
        return num;
    }

    public String getColor() {
        return color;
    }

    @Override
    public void apply(Actable world) {
        int cost = Unit.getLevelUpCost(baseLevel, targetLevel) * num;
        Clan clan = world.getClans().get(color);
        clan.getResource().costGold(cost);
        Territory territory = world.getBoard().getTerritory(territoryName);
        Unit baseUnit = new BasicUnit(num, baseLevel);
        Unit targetUnit = new BasicUnit(num, targetLevel);
        territory.decUnit(baseUnit);
        territory.addUnit(targetUnit);
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
