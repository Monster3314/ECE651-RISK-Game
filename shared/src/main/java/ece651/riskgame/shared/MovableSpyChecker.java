package ece651.riskgame.shared;

public class MovableSpyChecker extends ActionRuleChecker{
    /**
     * @param next rule checker in the chain
     */
    public MovableSpyChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MoveSpyAction msa = (MoveSpyAction) action;
        Clan clan = actable.getClans().get(msa.getColor());
        Spy spy;
        if (actable.getTerritoryOwnership(msa.getFromTerritory()).equals(msa.getColor()) && actable.getTerritoryOwnership(msa.getToTerritory()).equals(msa.getColor())) {
            spy = clan.getSpy(msa.getFromTerritory(), false);
        }
        else {
            spy = clan.getSpy(msa.getFromTerritory(), true);
        }
        return spy == null ? "You don't have movable Spy on this territory" : null;
    }
}
