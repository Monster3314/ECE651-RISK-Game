package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.List;

/**
 * abstract Action class extended by Action involving two territories (Move and Attack)
 */
public abstract class MigrationAction implements Action, Serializable{
    protected List<Unit> units;
    protected String fromTerritory;
    protected String toTerritory;
    protected String color;

    public MigrationAction(List<Unit> units, String fromTerritory, String toTerritory, String color) {
        this.units = units;
        this.fromTerritory = fromTerritory;
        this.toTerritory = toTerritory;
        this.color = color;
    }

    public List<Unit> getUnit() {
        return units;
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

    public int getTotalUnits() {
        int num = 0;
        for (Unit unit : units) {
            num += unit.getNum();
        }
        return num;
    }
}
