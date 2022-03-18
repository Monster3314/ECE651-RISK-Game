package ece651.riskgame.shared;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.List;
import java.util.Queue;

public class MovePathChecker extends ActionRuleChecker {
    public MovePathChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        // TODO: throw exception if action is not Move.class
        Move move = (Move) action;
        String src = move.getFromTerritory();
        String dest = move.getToTerritory();
        Board b = actable.getBoard();

        if (src.equals(dest)) {
            return "The departure and destination territory are the same territory!";
        }
        if (!move.getColor().equals(actable.getTerritoryOwnership(src))) {
            return "The departure territory does not belong to " + move.getColor() + " player.";
        }
        if (!move.getColor().equals(actable.getTerritoryOwnership(dest))) {
            return "The destination territory does not belong to " + move.getColor() + " player.";
        }
        Queue<Territory> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        queue.add(b.getTerritory(src));
        visited.add(src);
        while (!queue.isEmpty()) {
            Territory territory = queue.remove();
            List<Territory> neighbors = b.getNeighbors(territory);
            for (Territory t : neighbors) {
                String name = t.getName();
                if (!visited.contains(name) && actable.getTerritoryOwnership(name).equals(move.getColor())) {
                    if (name.equals(dest)) {
                        return null;
                    }
                    visited.add(name);
                    queue.add(t);
                }
            }
        }
        return "Failed to find a path from " + move.getFromTerritory() + " to " + move.getToTerritory() + ".";
    }
}
