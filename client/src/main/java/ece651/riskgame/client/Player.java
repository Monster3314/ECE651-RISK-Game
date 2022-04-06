package ece651.riskgame.client;

import java.util.HashMap;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.ActionRuleChecker;
import ece651.riskgame.shared.AdjacentTerritoryChecker;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.EnemyTerritoryChecker;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.MovePathChecker;
import ece651.riskgame.shared.UnitsRuleChecker;

public abstract class Player {
  protected String color;
  private GameInfo theGame;
  final HashMap<Class, ActionRuleChecker> actionCheckers;
  public Player() {
    color = null;
    theGame = null;
    actionCheckers = null;
    setupActionCheckers();
  }

  protected void setupActionCheckers() {
    actionCheckers.put(Attack.class,
        new UnitsRuleChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(null))));
    actionCheckers.put(Move.class, new MovePathChecker(new UnitsRuleChecker(null)));
  }

  /**
   * try to apply a action on client side
   * @param toAct is the action you want to apply
   * @param checker is the rulechecker used to check the rule of specified action
   * @return error message if action is invalid, otherwise null
   */
  public String tryApplyAction(Action toApply, ActionRuleChecker checker) {
    String errorMsg = checker.checkAction(theGame, toApply);
    if (errorMsg == null) {
      toApply.clientApply(theGame);
    }
    return errorMsg;
  }
}
