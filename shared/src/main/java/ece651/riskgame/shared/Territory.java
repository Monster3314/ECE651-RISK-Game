package ece651.riskgame.shared;

import java.util.ArrayList;
import java.util.List;

public abstract class Territory<T> {
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

  /**
   * Get the total unit number, used for battle in version 1
   */
  public int getUnitNumber() {
    return units.size();
  }
}
