package ece651.riskgame.shared;

import java.util.List;

public class AdjacentTerritoryChecker extends ActionRuleChecker {
    public AdjacentTerritoryChecker(ActionRuleChecker next) {
        super(next);
    }

  /**
   * Check if the destination territory is adjacent to source territory
   */
    @Override
    protected String checkMyRule(Actable actable, Action action) {
        // TODO : test broken data (territory not exist)
        MigrationAction ma = (MigrationAction) action;
        Board b = actable.getBoard();
        String toTerritory = ma.getToTerritory();
        List<Territory> adjacent = b.getNeighbors(b.getTerritory(ma.getFromTerritory()));
        for (Territory territory : adjacent) {
            if (territory.getName().equals(toTerritory)) {
                return null;
            }
        }
        return "Departure and destination territories are not adjacent";
    }
}
