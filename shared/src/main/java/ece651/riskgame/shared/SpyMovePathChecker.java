package ece651.riskgame.shared;

public class SpyMovePathChecker extends ActionRuleChecker{
    /**
     * @param next rule checker in the chain
     */
    public SpyMovePathChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MoveSpyAction msa = (MoveSpyAction) action;
        String src = msa.getFromTerritory();
        String dest = msa.getToTerritory();
        String color = msa.getColor();
        if (actable.getTerritoryOwnership(src).equals(color) && actable.getTerritoryOwnership(dest).equals(color)) {
            return MovePathChecker.findPath(src, dest, color, actable);
        }
        else {
            Board board = actable.getBoard();
            Territory from = board.getTerritory(src);
            Territory to = board.getTerritory(dest);
            if (board.getNeighbors(from).contains(to)) {
                return null;
            }
            else {
                return "Spies can only move 1 territory in enemy territory";
            }
        }
    }
}
