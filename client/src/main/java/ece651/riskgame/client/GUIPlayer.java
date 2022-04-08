package ece651.riskgame.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Unit;

public class GUIPlayer extends Player{
  private List<Action> actionsToSend;

  /**
   * Construct the GUIPlayer
   * @param color is the color that represents player's clan
   * @param game is the game the player is playing in    
   */
  public GUIPlayer(String color, GameInfo game) throws IOException {
    super(color, game);
    actionsToSend = new ArrayList<>();
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
    
  public Map<String, List<Unit>> adaptPlacements(Map<String, Integer> placements){
    //TODO:Send a list of placements instead of map
    //Adapter pattern
    Map<String, List<Unit>> adaptedPlacements = new HashMap<>();
    for (Map.Entry<String, Integer> placement: placements.entrySet()) {
      List<Unit> toPlace = new ArrayList<>(Arrays.asList(new BasicUnit(placement.getValue())));
      adaptedPlacements.put(placement.getKey(), toPlace);
    }
    return adaptedPlacements;
  }

  public List<Action> getActionsToSend() {
    return actionsToSend;
  }
  public void addActionToSend(Action toSend) {
    actionsToSend.add(toSend);
  }
  public void clearActionsToSend() {
    actionsToSend.clear();
  }
  
  

}
