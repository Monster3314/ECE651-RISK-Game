package ece651.riskgame.shared;

public class BasicUnit extends Unit {

  /**
   * BasicUnit is the weakest unit, with 1 HP and 1 attack
   */
  public BasicUnit(int number) {
    super(1, 1, number);
  }

  /**
   * used for test
   */
  public BasicUnit() {
    super(1,1,1);
  }
}
