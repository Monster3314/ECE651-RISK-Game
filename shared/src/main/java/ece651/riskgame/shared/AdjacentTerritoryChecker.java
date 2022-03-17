package ece651.riskgame.shared;

import java.util.LinkedList;

public class AdjacentTerritoryChecker extends ActionRuleChecker {
    public AdjacentTerritoryChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(GameInfo gameInfo, Action action) {
        // TODO : test broken data (territory not exist)
        MigrationAction ma = (MigrationAction) action;
        Board b = gameInfo.getBoard();
        String toTerritory = ma.getToTerritory();
        LinkedList<Territory> adjacent = b.getNeighbors(b.getTerritory(ma.getFromTerritory()));
        for (Territory territory : adjacent) {
            if (territory.getName().equals(toTerritory)) {
                return null;
            }
        }
        return "Departure and destination territories are not adjacent";
    }
}
