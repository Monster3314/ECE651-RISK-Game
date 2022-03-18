package ece651.riskgame.shared;

import java.util.List;

public class UnitsRuleChecker extends ActionRuleChecker {
    public UnitsRuleChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MigrationAction ma = (MigrationAction) action;
        Unit unitToMove = ma.getMoveUnit();
        Territory territory = actable.getBoard().getTerritory(ma.getFromTerritory());
        for (Unit unit : territory.getUnits()) {
            if (unit.getClass().equals(unitToMove.getClass())) {
                if (unit.getNum() >= unitToMove.getNum()) {
                    return null;
                }
                else {
                    return "No enough number of Unit remaining";
                }
            }
        }
        return "No specified Unit found";
    }
}
