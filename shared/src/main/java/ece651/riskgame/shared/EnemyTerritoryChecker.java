package ece651.riskgame.shared;

import java.util.LinkedList;


/**
 * Check if the attacker owns the departure territory
 * Check if the target territory is an enemy
 * Both by checking the color of the territory owner
 */
public class EnemyTerritoryChecker extends ActionRuleChecker {
    public EnemyTerritoryChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MigrationAction ma = (MigrationAction) action;

        String from = ma.getFromTerritory();
        String to = ma.getToTerritory();
        if (!ma.color.equals(actable.getTerritoryOwnership(from))) {
            return "The departure territory does not belong to " + ma.color + " player.";
        }
        if (actable.getTerritoryOwnership(from).equals(actable.getTerritoryOwnership(to))) {
            return "Territories belong to same clan.";
        }
        return null;
    }
}
