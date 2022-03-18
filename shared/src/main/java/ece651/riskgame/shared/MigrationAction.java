package ece651.riskgame.shared;

public abstract class MigrationAction implements Action {
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

    public Unit getMoveUnit() {
        return Unit;
    }

    public String getFromTerritory() {
        return fromTerritory;
    }

    public String getToTerritory() {
        return toTerritory;
    }
}
