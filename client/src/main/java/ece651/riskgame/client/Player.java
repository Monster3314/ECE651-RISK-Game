package ece651.riskgame.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.ActionRuleChecker;
import ece651.riskgame.shared.AdjacentTerritoryChecker;
import ece651.riskgame.shared.Attack;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.EnemyTerritoryChecker;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.MovePathChecker;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Resource;
import ece651.riskgame.shared.SufficientResourceChecker;
import ece651.riskgame.shared.SufficientUnitChecker;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UnitsRuleChecker;
import ece651.riskgame.shared.UpgradeTechAction;
import ece651.riskgame.shared.UpgradeUnitAction;

public abstract class Player {
  protected String color;
  protected GameInfo theGame;
  protected final Map<Class, ActionRuleChecker> actionCheckers;

  /**
   * Player Constructor
   * @param color is the player's clan
   * @param game is the model of the game
   * @throws IllegalArgumentException when color or game is null, or color is not in the game 
   */  
  public Player(String color, GameInfo game) throws IllegalArgumentException {
    if (color == null) {
      throw new IllegalArgumentException("Color can not be null");
    }
    if (game == null) {
      throw new IllegalArgumentException("Game can not be null");
    }
    if (!game.getClans().containsKey(color)) {
      throw new IllegalArgumentException("Color is not in this game");
    }
    
    this.color = color;
    this.theGame = game;;
    this.actionCheckers = new HashMap<Class, ActionRuleChecker>();
    setupActionCheckers();
  }
  
  protected void setupActionCheckers() {
    actionCheckers.put(Attack.class,
        new UnitsRuleChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(null))));
    actionCheckers.put(Move.class, new MovePathChecker(new UnitsRuleChecker(null)));
    actionCheckers.put(UpgradeUnitAction.class, new SufficientUnitChecker(new SufficientResourceChecker(null)));
    actionCheckers.put(UpgradeTechAction.class, new  SufficientResourceChecker(null));
  }
  
  /**
   * try to apply a action on client side
   * @param toAct is the action you want to apply
   * @return error message if action is invalid, otherwise null
   */
  public String tryApplyAction(Action toApply) {
    ActionRuleChecker checker = actionCheckers.get(toApply.getClass());
    String errorMsg = checker.checkAction(theGame, toApply);
    if (errorMsg == null) {
      toApply.clientApply(theGame);
    }
    return errorMsg;
  }
  
  /**
   * get the color of the player
   * @return the color of this player  
   */  
  public String getColor() {
    return color;
  }
  
  /**
   * get current game model
   * @return the latest game model on server side 
   */  
  public GameInfo getGame() {
    return theGame;
  }

  /**
   * updateGame will receive the latest game from the server and update the game on client side
   */
  public void updateGame(GameInfo latestGame) {
    theGame = latestGame;
    
  }
    //adapting from list of moves to map(territory string to list of placed units)
  public Map<String, List<Unit>> adaptPlacements(List<PlaceAction> placements) {
    Map<String, List<Unit>> serverPlacements = new HashMap<>();
    List<Territory> occupies = getOccupies();
    for (Territory occupy : occupies) {
      serverPlacements.put(occupy.getName(), new ArrayList<Unit>(Arrays.asList(new BasicUnit(0))));
    }
    for (PlaceAction placement: placements) {
      serverPlacements.get(placement.getDestination()).add(placement.getUnit());
    }
    return serverPlacements;
  }
  
  
  /**
   * helper function used to get the player's occupies outside this class
   * this function is used to adapt a list of placements(move) to the map which maps territory name to a list of units  
   * @return a list of territory which is this player's occupies  
   */  
  public List<Territory> getOccupies() {
    return theGame.getClans().get(color).getOccupies();
  }

  /**
   * getEnemyTerritoryNames get the enemy territory names 
   * @return a set of string that represent the territory names of all the enemy  
   */  
  public Set<String> getEnemyTerritoryNames() {
    Map<String, Clan> clans = theGame.getClans();
    Set<String> enemyTerritoryNames = new HashSet<>();
    for (Entry<String, Clan> clan: clans.entrySet()) {
      if (!clan.getKey().equals(color)) {
        for (Territory enemyOccupy: clan.getValue().getOccupies()) {
          enemyTerritoryNames.add(enemyOccupy.getName());
        }
      }
    }
    return enemyTerritoryNames;
  }
  /**
   * get the name of territories in the game
   * @return a set of string which is the name of the territories  
   */  
  public Set<String> getTerritoryNames() {
    Board theBoard = theGame.getBoard();
    Set<Territory> territories = theBoard.getTerritoriesSet();
    Set<String> territoryNames = new HashSet<>();
    for (Territory t: territories) {
      territoryNames.add(t.getName());
    }
    return territoryNames;
  }
    /**
   * Check if the player is lost
   * @return true if player is lost, false if game is not over or player wins 
   */  
  public boolean isLost() {
    return !theGame.getPlayers().get(color).isActive();
  }
  /**
   * Check if the game is over
   * @return true if game is over
   */
  public boolean isGameOver() {
    return !(theGame.getWinner() == null); 
  }

  /**
   * get the Technology Level of this player
   * @return technology level 
   */
  public Integer getTechLevel() {
    return theGame.getClans().get(color).getMaxTechLevel();
  }

  /**
   * get the current Food Resource of this player
   * @return an integer that represents the remained food  
   */
  public Integer getFood() {
    return theGame.getClans().get(color).getResource().getResourceNum(Resource.FOOD);
  }

  /**
   * get the current Gold Resource of this player
   * @return an integer that represents the remained gold  
   */
  public Integer getGold() {
    return theGame.getClans().get(color).getResource().getResourceNum(Resource.GOLD);
  }
  

}
