package ece651.riskgame.shared;

public abstract class ActionRuleChecker {
    private final ActionRuleChecker next;
    public ActionRuleChecker(ActionRuleChecker next) {
        this.next = next;
    }
    public String checkAction (Actable actable, Action action) {
        String msg = checkMyRule(actable, action);
        if (msg != null) {
            return msg;
        }
        if (next != null) {
            return next.checkAction(actable, action);
        }
        return null;
    }

    protected abstract String checkMyRule(Actable actable, Action action);
}
