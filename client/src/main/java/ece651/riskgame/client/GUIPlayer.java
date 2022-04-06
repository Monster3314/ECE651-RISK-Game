package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.ActionRuleChecker;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class GUIPlayer {
  private String color;
  private GameInfo theGame;
  private ObjectInputStream input;
  private ObjectOutputStream output;
  //private List<Action> actionsToSend;
  //final HashMap<Class, ActionRuleChecker> actionCheckers;

  public String getColor() {
    return color;
  }
  public GameInfo getGame() {
    return theGame;
  }
  /**
   * GUIPlayer Constructor
   * @param server is the socket of the server
   * @param color is the color of the player
   * @param game is the initialized game
   */
  public GUIPlayer(Socket server) throws IOException {
    this.input = new ObjectInputStream(server.getInputStream());
    this.output = new ObjectOutputStream(server.getOutputStream());
    this.color = null;
    this.theGame = null;
  }
  public void initializeGame() throws ClassNotFoundException, IOException{
    this.color = (String) input.readObject();
    this.theGame = (GameInfo) input.readObject();
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
  
  public String tryPlace(Map<String, Integer> placements) {
    //TODO:get a list of placeActions instead of map
    //Adapter pattern
    List<PlaceAction> placeActions= new ArrayList<>();
    for (Map.Entry<String, Integer> placement: placements.entrySet()) {
      placeActions.add(new PlaceAction(new BasicUnit(placement.getValue()), placement.getKey()));
    }
    //TODO:check placements validity and return errorMsg
    for (PlaceAction placement: placeActions) {
      placement.apply(theGame);
    }
    return null;
  }
  public void sendPlacements(Map<String, Integer> placements) throws IOException{
    //TODO:Send a list of placements instead of map
    //Adapter pattern
    Map<String, List<Unit>> adaptedPlacements = new HashMap<>();
    for (Map.Entry<String, Integer> placement: placements.entrySet()) {
      List<Unit> toPlace = new ArrayList<>(Arrays.asList(new BasicUnit(placement.getValue())));
      adaptedPlacements.put(placement.getKey(), toPlace);
    }
  
    output.writeObject(adaptedPlacements);
    output.flush();
    output.reset();
  }
  /**
   * updateGame will receive the latest game from the server and update the game on client side
   * @throws IOException when latest game is not recieved
   * @throws ClassNotFoundException when case failed
   */
  public void updateGame() throws IOException, ClassNotFoundException {
    theGame = (GameInfo) input.readObject();    
  }
  /*
  public String tryApplyAction(Action toApply) {
    
  }
  */
  public List<Territory> getMyTerritories() {
    Clan myClan = theGame.getClans().get(color);
    return myClan.getOccupies();
  }

  
  

}
