package ece651.riskgame.shared;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * check if there is a path consisting of Clan's own territories from departure to destination.
 */
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
        return findPath(src, dest, move.getColor(), actable);
    }

    public static String findPath(String src, String dest, String color, Actable actable) {
        Board b = actable.getBoard();

        if (src.equals(dest)) {
            return "The departure and destination territory are the same territory!";
        }
        if (!color.equals(actable.getTerritoryOwnership(src))) {
            return "The departure territory does not belong to " + color + " player.";
        }
        if (!color.equals(actable.getTerritoryOwnership(dest))) {
            return "The destination territory does not belong to " + color + " player.";
        }
        // apply a BFS to look for destination
        Queue<Territory> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        queue.add(b.getTerritory(src));
        visited.add(src);
        while (!queue.isEmpty()) {
            Territory territory = queue.remove();
            List<Territory> neighbors = b.getNeighbors(territory);
            for (Territory t : neighbors) {
                String name = t.getName();
                if (!visited.contains(name) && actable.getTerritoryOwnership(name).equals(color)) {
                    if (name.equals(dest)) {
                        return null;
                    }
                    visited.add(name);
                    queue.add(t);
                }
            }
        }
        return "Failed to find a path from " + src + " to " + dest + ".";
    }
}
