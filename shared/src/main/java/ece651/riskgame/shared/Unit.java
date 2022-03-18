package ece651.riskgame.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable{
  protected int attack;
  protected int HP;
  protected int number;

  public Unit(int attack, int hp, int number) {
    this.attack = attack;
    this.HP = hp;
    if(number < 0) throw new IllegalArgumentException("the number is less than 0");
    this.number = number;
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

  public void addSoldiers(int n) {
    number += n;
  }

  public void decSoldiers(int n) {
    if(n > number) throw new IllegalArgumentException("n is larger than the number of soldiers");
    number -= n;
  }

  /**
   * Get a random attack number for attack phase
   */
  public abstract int getRandomAttack();
}
