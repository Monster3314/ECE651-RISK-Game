package ece651.riskgame.shared;

public abstract class ActionRuleChecker {
    private final ActionRuleChecker next;
    public ActionRuleChecker(ActionRuleChecker next) {
        this.next = next;
    }
    public String checkAction (GameInfo gameInfo, Action action) {
        String msg = checkMyRule(gameInfo, action);
        if (msg != null) {
            return msg;
        }
        if (next != null) {
            return next.checkAction(gameInfo, action);
        }
        return null;
    }

    protected abstract String checkMyRule(GameInfo gameInfo, Action action);
}
