package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.EnemyTerritoryChecker;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.MovePathChecker;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;
import ece651.riskgame.shared.UnitsRuleChecker;

public abstract class Player {
  protected String color;
  protected GameInfo theGame;
  protected ObjectInputStream input;
  protected ObjectOutputStream output;
  protected final Map<Class, ActionRuleChecker> actionCheckers;
  protected List<Action> actionsToSend;
  public Player(Socket server) throws IOException{
    this.input = new ObjectInputStream(server.getInputStream());
    this.output = new ObjectOutputStream(server.getOutputStream());
    this.color = null;
    this.theGame = null;
    this.actionCheckers = new HashMap<Class, ActionRuleChecker>();
    this.actionsToSend = new ArrayList<>();
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
   * initialize the game by recieving player color and initialized game model from server
   * @throws ClassNotFoundException when casting failed
   * @throws IOException when nothing is fetched from server  
   */  
  public void initializeGame() throws ClassNotFoundException, IOException{
    this.color = (String) input.readObject();
    this.theGame = (GameInfo) input.readObject();
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
   * addActionToSend will add an action to the list which will be sent to the server at the end of turn  
   */  
  public void addActionToSend(Action toSend) {
    actionsToSend.add(toSend);
  }
  
  /**
   * sendActions will send the list of actions to the server  
   */  
  public void sendActions(List<Action> toSend) throws IOException {
    output.writeObject(toSend);
    output.flush();
    output.reset();
    actionsToSend.clear();
  }
  /**
   * updateGame will receive the latest game from the server and update the game on client side
   * @throws IOException when latest game is not recieved
   * @throws ClassNotFoundException when case failed
   */
  public void updateGame() throws IOException, ClassNotFoundException {
    theGame = (GameInfo) input.readObject();
    
  }

  /**
   * doEndOfTurn will do the end of the regular turn which consists of send actions and update game
   * @throws IOException when latest game is not recieved
   * @throws ClassNotFoundException when case failed   
   */  
  public void doEndOfTurn() throws IOException, ClassNotFoundException{
    sendActions(actionsToSend);
    updateGame();
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
   * getUnitsToPlace get the number of basic units to place
   * @throws ClassNotFoundException when failed to cast
   * @throws IOException when nothing fetched  
   */  
  @SuppressWarnings("unchecked")
  //TODO:return list of units instead of integer
  public Integer getUnitsToPlace() throws ClassNotFoundException, IOException{
    List<Unit> toPlace = (List<Unit>) input.readObject();
    return toPlace.get(0).getNum();
  }

}
