package ece651.riskgame.shared;

public interface Action {
    /**
     * @param world the Action applying on
     */
    void apply(Actable world);

    /**
     * @param game apply method used by Client
     */
    void clientApply(Actable game);
}
