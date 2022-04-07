package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Territory implements Serializable {
  protected String name;
  protected List<Unit> units;
  protected Resource production;
  protected int size;

  public Territory(String name) {
    this(name, 0, new Resource(new int[]{0, 0}));
  }

  public Territory(String name, int size, Resource production) {
    this.name = name;
    units = new ArrayList<Unit>();
    this.production = production;
    this.size = size;
  }

  public String getName() {
    return name;
  }

  public Resource getProduction() {
    return production;
  }

  public int getSize() {
    return this.size;
  }

  /**
   * Add a single unit to the territory
   */
  public void addUnit(Unit u) {
    for (Unit i : units) {
      if (i.getLevel() == u.getLevel()) {
        i.addSoldiers(u.getNum());
        return;
      }
    }
    units.add(u);
  }
  
  public void decUnit(Unit u) {
    for (Unit i : units) {
      if (i.getLevel() == u.getLevel()) {
        i.decSoldiers(u.getNum());
        return;
      }
    }
    throw new IllegalArgumentException("there is no such Unit on this territory: " + name);
  }

  /**
   * Add a list of units
   */
  public void addUnitList(List<Unit> toAdd) {
    for (Unit i : toAdd) {
      addUnit(i);
    }
  }

  public List<Unit> getUnits() {
    return units;
  }

  public Unit getUnitByLevel(int level) {
    for (Unit unit : units) {
      if (unit.getLevel() == level) {
        return unit;
      }
    }
    return null;
  }

  /**
   * @Return true if territory fallen, false if defender wins
   * TODO: Does not support battle between different units
   */
  public boolean beAttacked(Unit attacker) {
    for (Unit i : units) {
      if (i.getClass() == attacker.getClass()) {
        while (i.getNum() != 0 && attacker.getNum() != 0) {
          int i_dice = i.getRandomAttack();
          int attacker_dice = attacker.getRandomAttack();
          if (i_dice >= attacker_dice)
            attacker.decSoldiers(1);
          else
            i.decSoldiers(1);
          //System.out.println("attacker:" + attacker.getNum() + " | this: " + i.getNum());
        }
        if (i.getNum() == 0) {
          addUnit(attacker);
          return true;
        } else
          return false;
      }
    }
    throw new IllegalArgumentException("there is no such type of unit");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Territory territory = (Territory) o;

    return name != null ? name.equals(territory.name) : territory.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  public boolean isEmpty() {
    for (Unit u : units) {
      if (u.getNum() > 0) {
        return false;
      }
    }
    return true;

  }
}
