package ece651.riskgame.shared;

public abstract class MigrationAction implements Action {
    protected Unit moveUnit;
    protected String fromTerritory;
    protected String toTerritory;
    protected String color;

    public MigrationAction(Unit moveUnit, String fromTerritory, String toTerritory, String color) {
        this.moveUnit = moveUnit;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
        this.color = color;
    }

    public Unit getMoveUnit() {
        return moveUnit;
    }

    public String getFromTerritory() {
        return fromTerritory;
    }

    public String getToTerritory() {
        return toTerritory;
    }
}
