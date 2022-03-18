package ece651.riskgame.shared;

import java.util.Map;

public interface Action {
    public void apply(Actable world);

    public void clientApply(Actable game);
}
