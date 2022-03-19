package ece651.riskgame.shared;

/**
 * Abstract Rule Checker class: chain of responsibility
 */
public abstract class ActionRuleChecker {
    private final ActionRuleChecker next;

    /**
     * @param next rule checker in the chain
     */
    public ActionRuleChecker(ActionRuleChecker next) {
        this.next = next;
    }

    /**
     * @param actable Game state information
     * @param action to check
     * @return null if pass the checker. Or error message.
     */
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

    /**
     * Define check rule of this checker
     */
    protected abstract String checkMyRule(Actable actable, Action action);
}
