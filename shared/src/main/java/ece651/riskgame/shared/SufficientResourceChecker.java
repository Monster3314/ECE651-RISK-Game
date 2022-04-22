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
        if (action instanceof MigrationAction) {
          MigrationAction ma = (MigrationAction) action;
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
        else if (action.getClass() == MoveSpyAction.class) {
            MoveSpyAction msa = (MoveSpyAction) action;
            int cost;
            if (actable.getTerritoryOwnership(msa.getFromTerritory()).equals(msa.getColor()) && actable.getTerritoryOwnership(msa.getToTerritory()).equals(msa.getColor())) {
                cost = actable.getUnitMoveCost(msa.getFromTerritory(), msa.getColor()).get(msa.getToTerritory());
            }
            else {
                Board board = actable.getBoard();
                cost = board.getTerritory(msa.getFromTerritory()).getSize() + board.getTerritory(msa.getToTerritory()).getSize();
            }
            if (actable.getClans().get(msa.getColor()).getResource().getResourceNum(Resource.FOOD) > cost) {
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
            else if (action.getClass() == UpgradeSpyAction.class) {
                UpgradeSpyAction usa = (UpgradeSpyAction) action;
                color = usa.getColor();
                cost = 20;
            }
            else if(action.getClass() == GetCloakAction.class) {
                GetCloakAction gca = (GetCloakAction) action;
                cost = 100;
                color = gca.getColor();
            }
            else if(action.getClass() == DoCloakAction.class) {
                DoCloakAction dca = (DoCloakAction) action;
                cost = 20;
                color = dca.getColor();
            }
            else {
                UpgradeTechAction uta = (UpgradeTechAction) action;
                cost = Clan.COST[actable.getClans().get(uta.getColor()).getTechLevel()];
                color = uta.getColor();
            }
            if (actable.getClans().get(color).getResource().getResourceNum(Resource.GOLD) >= cost) {
                return null;
            }
            else {
                return "Insufficient Gold!";
            }
        }
    }
}
