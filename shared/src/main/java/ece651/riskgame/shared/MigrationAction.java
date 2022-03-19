package ece651.riskgame.shared;

import java.io.Serializable;

/**
 * abstract Action class extended by Action involving two territories (Move and Attack)
 */
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

    /**
     * @return departure territory name
     */
    public String getFromTerritory() {
        return fromTerritory;
    }

    /**
     * @return destination territory name
     */
    public String getToTerritory() {
        return toTerritory;
    }

    /**
     * @return Action taker's color
     */
    public String getColor() {
        return color;
    }
}
