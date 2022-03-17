package ece651.riskgame.shared;

import java.util.LinkedList;

public class EnemyTerritoryChecker extends ActionRuleChecker {
    public EnemyTerritoryChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(GameInfo gameInfo, Action action) {
        MigrationAction ma = (MigrationAction) action;
        String from = gameInfo.getTerritoryOwnership(ma.getFromTerritory());
        String to = gameInfo.getTerritoryOwnership(ma.getToTerritory());
        if (from.equals(to)) {
            return "Territories belong to same clan";
        }
        return null;
    }
}
