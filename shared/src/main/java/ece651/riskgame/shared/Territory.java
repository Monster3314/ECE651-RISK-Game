package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.*;

public abstract class Territory implements Serializable {
  private static final long serialVersionUID = -543339904871157059L;

  protected String name;
  protected List<Unit> units;
  protected Resource production;
  protected int size;
  protected int cloakNum = 0;

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
   * update the status of territory
   * @param  latestTerritory is the latest territory used to update
   * @throws IllegalArgumentException if toUpdate doesn't have the same name  
   */  
  public void update(Territory latestTerritory) {
    if (!latestTerritory.getName().equals(name)) {
      throw new IllegalArgumentException("Territory used to update should share the same name.");
    }
    units = latestTerritory.getUnits();
    production = latestTerritory.getProduction();
    size = latestTerritory.getSize();
    cloakNum = latestTerritory.getCloakNum();
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
        if (i.getNum() == 0) {
          units.remove(i);
        }
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

  public boolean beAttacked(List<Unit> attacker) {
    boolean attackRound = true;
    while (attacker.size() != 0 && units.size() != 0) {
      if (attackRound) {
        combat(attacker, units);
      }
      else {
        combat(units, attacker);
      }
      attackRound = !attackRound;
    }
    if (units.size() == 0) {
      addUnitList(attacker);
      cloakNum = 0;
      return true;
    }
    else {
      return false;
    }
  }

  private void combat(List<Unit> units1, List<Unit> units2) {
    Unit u1 = Collections.max(units1, Comparator.comparingInt(Unit::getLevel));
    Unit u2 = Collections.min(units2, Comparator.comparingInt(Unit::getLevel));
    while (u1.getNum() != 0 && u2.getNum() != 0) {
      int u1Dice = u1.getRandomAttack();
      int u2Dice = u2.getRandomAttack();
      if (u1Dice >= u2Dice)
        u2.decSoldiers(1);
      else
        u1.decSoldiers(1);
    }
    if (u1.getNum() == 0) {
      units1.remove(u1);
    }
    else {
      units2.remove(u2);
    }
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

  public void setCloakNum(int cloakNum) {
    this.cloakNum = cloakNum;
  }

  public void decCloakNum() {
    if(cloakNum != 0) {
      cloakNum -= 1;
    }
  }

  public int getCloakNum() {
    return cloakNum;
  }
}
