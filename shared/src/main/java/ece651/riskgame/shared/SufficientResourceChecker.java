package ece651.riskgame.shared;

public class SufficientResourceChecker extends ActionRuleChecker {
    /**
     * @param next rule checker in the chain
     */
    public SufficientResourceChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        if (action instanceof MigrationAction ma) {
            int unitCost = 1;
            if (action.getClass() == Move.class) {
                unitCost = actable.getUnitMoveCost(ma.getFromTerritory(), ma.color).get(ma.toTerritory);
            }
            if (actable.getClans().get(ma.color).getResource().getResourceNum(Resource.FOOD) >= unitCost * ma.getTotalUnits()) {
                return null;
            }
            else {
                return "Insufficient Food!";
            }
        }
        else {
            int cost;
            String color;
            if (action.getClass() == UpgradeUnitAction.class) {
                UpgradeUnitAction uua = (UpgradeUnitAction) action;
                cost = Unit.getLevelUpCost(uua.getBaseLevel(), uua.getTargetLevel()) * uua.getNum();
                color = uua.getColor();
            }
            else {
                UpgradeTechAction uta = (UpgradeTechAction) action;
                cost = Clan.COST[actable.getClans().get(uta.getColor()).getMaxTechLevel()];
                color = uta.getColor();
            }
            if (actable.getClans().get(color).getResource().getResourceNum(Resource.FOOD) >= cost) {
                return null;
            }
            else {
                return "Insufficient Gold!";
            }
        }
    }
}
