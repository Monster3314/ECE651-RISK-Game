package ece651.riskgame.shared;

import java.io.Serializable;

public class DoCloakAction implements Action, Serializable {
    private String color;
    private String territoryName;

    public DoCloakAction(String color, String territoryName) {
        this.color = color;
        this.territoryName = territoryName;
    }

    @Override
    public void apply(Actable world) {

    }

    @Override
    public void clientApply(Actable game) {

    }
}
