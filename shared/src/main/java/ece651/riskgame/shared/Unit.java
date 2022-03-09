package ece651.riskgame.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable{
  protected int attack;
  protected int HP;

  public Unit(int attack, int hp) {
    this.attack = attack;
    this.HP = hp;
  }

  public int getAttack() {
    return attack;
  }

  public int getHP() {
    return HP;
  }
}
