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
    for(Unit i: units) {
      if(i.getClass() == u.getClass()) {
        i.addSoldiers(u.getNum());
        return;
      }
    }
    units.add(u);
  }

  public void decUnit(Unit u) {
    for(Unit i: units) {
      if(i.getClass() == u.getClass()) {
        i.decSoldiers(u.getNum());
        if(i.getNum() == 0) units.remove(i);
        return;
      }
    }
    throw new IllegalArgumentException("there is no such Unit on this territory: " +  name);
  }

  /**
   * Add a list of units
   */
  public void addUnitList(List<Unit> toAdd) {
    for (Unit i: toAdd) {
      addUnit(i);
    }
  }

  public List<Unit> getUnits() {
    return units;
  }

  public boolean beAttacked(Unit attacker) {
    for(Unit i: units) {
      if(i.getClass() == attacker.getClass()) {
        if(i.getNum() > attacker.getNum()) {  //equal?
          i.decSoldiers(attacker.getNum());
          return false;
        } else {
          attacker.decSoldiers(i.getNum());
          units.remove(i);
          addUnit(attacker);
          return true;
        }
      }
    }
    return false;
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
