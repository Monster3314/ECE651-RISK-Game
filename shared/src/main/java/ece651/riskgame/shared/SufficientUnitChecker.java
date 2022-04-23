package ece651.riskgame.shared;

public class SufficientUnitChecker extends ActionRuleChecker {
    /**
     * @param next rule checker in the chain
     */
    public SufficientUnitChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        if (action.getClass() == UpgradeSpyAction.class) {
            UpgradeSpyAction usa = (UpgradeSpyAction) action;
            String territory = usa.getTerritoryName();
            Unit territoryUnit = actable.getBoard().getTerritory(territory).getUnitByLevel(0);
            if (territoryUnit == null) {
                return "You need to upgrade a spy from a basic unit.";
            }
            return null;
        }
        else {
            UpgradeUnitAction uua = (UpgradeUnitAction) action;
            Territory territory = actable.getBoard().getTerritory(uua.getTerritoryName());
            if (!uua.getColor().equals(actable.getTerritoryOwnership(territory.getName()))) {
                return territory.getName() + " does not belong to " + uua.getColor();
            }
            Unit territoryUnit = territory.getUnitByLevel(uua.getBaseLevel());
            if (territoryUnit == null) {
                return "No specified Unit found.";
            }
            if (uua.getTargetLevel() > actable.getClans().get(uua.getColor()).getTechLevel()) {
                return "Your max tech level does not support this upgrade.";
            }
            if (uua.getNum() > territoryUnit.getNum()) {
                return "No enough number of Unit remaining.";
            } else {
                return null;
            }
        }
    }
}
