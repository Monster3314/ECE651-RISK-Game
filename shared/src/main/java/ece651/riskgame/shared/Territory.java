package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Territory implements Serializable{
  protected String name;
  protected List<Unit> units;

  public Territory(String name) {
    this.name = name;
    units = new ArrayList<Unit>();
  }

  public String getName() {
    return name;
  }

  /**
   * Add a single unit to the terrority
   */
  public void addUnit(Unit u) {
    units.add(u);
  }

  /**
   * Add a list of units
   */
  public void addUnitList(List<Unit> toAdd) {
    units.addAll(toAdd);
  }

  public List<Unit> getUnits() {
    return units;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Territory territory = (Territory) o;

    return name != null ? name.equals(territory.name) : territory.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
