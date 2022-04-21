package ece651.riskgame.shared;

import java.io.Serializable;

public class GetCloakAction implements Action, Serializable {

    private final String color;

    public GetCloakAction(String color) {
        this.color = color;
    }


    @Override
    public void apply(Actable world) {

    }

    @Override
    public void clientApply(Actable game) {

    }
}
