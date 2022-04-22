package ece651.riskgame.shared;

import java.io.Serializable;

public class GetCloakAction implements Action, Serializable {

    private final String color;

    public GetCloakAction(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }


    @Override
    public void apply(Actable world) {
        Clan clan = world.getClans().get(color);
        clan.getCloakAbility();
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}
