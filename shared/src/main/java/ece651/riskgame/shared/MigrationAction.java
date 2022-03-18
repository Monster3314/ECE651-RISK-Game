package ece651.riskgame.shared;

import java.io.Serializable;

public abstract class MigrationAction implements Action, Serializable{
    protected Unit Unit;
    protected String fromTerritory;
    protected String toTerritory;
    protected String color;

    public MigrationAction(Unit moveUnit, String fromTerritory, String toTerritory, String color) {
        this.Unit = moveUnit;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
        this.color = color;
    }

    public Unit getUnit() {
        return Unit;
    }

    public String getFromTerritory() {
        return fromTerritory;
    }

    public String getToTerritory() {
        return toTerritory;
    }

    public String getColor() {
        return color;
    }
}
