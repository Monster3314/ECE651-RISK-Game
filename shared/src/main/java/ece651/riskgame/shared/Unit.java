package ece651.riskgame.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable{
  protected int attack;
  protected int HP;
  protected int number;

  public Unit(int attack, int hp, int number) {
    this.attack = attack;
    this.HP = hp;
    this.number = Math.max(number, 0);
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
}
