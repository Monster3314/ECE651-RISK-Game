package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Territory implements Serializable {
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
    for (Unit i : units) {
      if (i.getClass() == u.getClass()) {
        i.addSoldiers(u.getNum());
        return;
      }
    }
    units.add(u);
  }

  public void decUnit(Unit u) {
    for (Unit i : units) {
      if (i.getClass() == u.getClass()) {
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
          System.out.println("attacker:" + attacker.getNum() + " | this: " + i.getNum());
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
