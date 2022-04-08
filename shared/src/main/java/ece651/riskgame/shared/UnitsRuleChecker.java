package ece651.riskgame.shared;

import java.util.List;

/**
 * check if departure territories have enough units
 */
public class UnitsRuleChecker extends ActionRuleChecker {
    public UnitsRuleChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        MigrationAction ma = (MigrationAction) action;
        List<Unit> units = ma.getUnit();
        Territory territory = actable.getBoard().getTerritory(ma.getFromTerritory());
        for (Unit unit : units) {
            Unit territoryUnit = territory.getUnitByLevel(unit.getLevel());
            if (territoryUnit == null) {
                return "No specified Unit found.";
            }
              if (unit.getNum() > territoryUnit.getNum()) {
                  return "No enough number of Unit remaining.";
              }
        }
        return null;
    }
}
