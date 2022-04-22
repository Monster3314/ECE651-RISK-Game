package ece651.riskgame.shared;

public class AbilityChecker extends ActionRuleChecker{

    public AbilityChecker(ActionRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Actable actable, Action action) {
        if(action.getClass() == GetCloakAction.class) {
            GetCloakAction gca = (GetCloakAction) action;
            if(actable.getClans().get(gca.getColor()).getTechLevel() < 3) return "The Tech level must be equal to or higher than 3!";
            else return null;
        } else if(action.getClass() == DoCloakAction.class) {
            DoCloakAction dca = (DoCloakAction) action;
            if(!actable.getClans().get(dca.getColor()).hasCloakAbility()) return "You don't have Cloak Ability!";
            else if (!dca.getColor().equals(actable.getTerritoryOwnership(dca.getTerritoryName()))) {
                return "The territory doesn't belong to you!";
            }
            return null;
        }
        return null;
    }
}
