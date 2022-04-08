package ece651.riskgame.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable{
  protected int attack;
  protected int HP;
  protected int number;
  protected int level;
  static public int[] COST = new int[] {3, 8, 19, 25, 35, 50};
  static public int[] BONUS = new int[] {0, 1, 3, 5, 8, 11, 15};
  static public String[] NAME = new String[] {"Vale Knights", "North Army", "Second Sons", "Iron Fleet", "Unsullied", "Golden Company", "Dead Army"};
  static public int MAX_LEVEL = 6;

  public Unit(int attack, int hp, int number) {
    this.attack = attack;
    this.HP = hp;
    if(number < 0) throw new IllegalArgumentException("the number is less than 0");
    this.number = number;
    this.level = 0;
  }

  public Unit(int number, int level) {
    this(0, 0, number);
    this.level = level;
  }

  public int getAttack() {
    return attack;
  }

  public int getHP() {
    return HP;
  }

  public int getNum() {
    return number;
  }

  public int getLevel() {
    return level;
  }

  public String getName() {
    return Unit.NAME[level];
  }

  public void addSoldiers(int n) {
    number += n;
  }

  public void decSoldiers(int n) {
    if(n > number) throw new IllegalArgumentException("n is larger than the number of soldiers");
    number -= n;
  }

  @Override
  public String toString() {
    return number + (number <= 1 ? " Unit" : " Units");
  }

  /**
   * Get a random attack number for attack phase
   */
  public abstract int getRandomAttack();

  public void levelUp() {
    if (level == Unit.MAX_LEVEL) {
      throw new IllegalStateException("Exceed the max level");
    }
    level ++;
  }

  static public int getLevelUpCost(int baseLevel, int targetLevel) {
    int cost = 0;
    for (int level = baseLevel; level < targetLevel; level++) {
      cost += Unit.COST[level];
    }
    return cost;
  }
}
