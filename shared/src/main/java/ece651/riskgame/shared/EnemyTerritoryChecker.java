package ece651.riskgame.shared;

import java.util.LinkedList;

public class EnemyTerritoryChecker extends ActionRuleChecker {
    public EnemyTerritoryChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MigrationAction ma = (MigrationAction) action;
        String from = actable.getTerritoryOwnership(ma.getFromTerritory());
        String to = actable.getTerritoryOwnership(ma.getToTerritory());
        if (!ma.color.equals(actable.getTerritoryOwnership(from))) {
            return "The departure territory does not belong to Action maker";
        }
        if (from.equals(to)) {
            return "Territories belong to same clan";
        }
        return null;
    }
}
