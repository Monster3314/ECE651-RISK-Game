package ece651.riskgame.shared;

import java.util.Random;

public class BasicUnit extends Unit {

  Random rand;
  
  /**
   * BasicUnit is the weakest unit, with 1 HP and 1 attack
   */
  public BasicUnit(int number) {
    super(1, 1, number);
    rand = new Random();
  }

  /**
   * used for test
   */
  public BasicUnit() {
    super(1, 1, 1);
  }

  /**
   * Then random attack range for basic unit is 0-20
   */
  @Override
  public int getRandomAttack() {    
    return rand.nextInt(20) + 1;
  }
}
