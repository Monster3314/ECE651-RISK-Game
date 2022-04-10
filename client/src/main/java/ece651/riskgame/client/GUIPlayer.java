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
  public GUIPlayer(String color, GameInfo game) {
    super(color, game);
    actionsToSend = new ArrayList<>();
  }

  //TODO: different levels of units to place
  //TODO: Troop instead of List<Unit>
  /**
   * check if the placement is valid and apply
   * if valid, apply and return null
   * if invalid, return error message
   * @param placements is the placement player specified through GUI
   * @param toAllocate is the allocated units to place
   */
  public String tryPlace(Map<String, List<Unit>> placements, List<Unit> toAllocate) {
    Integer numberToPlace = 0;
    Integer numberToAllocate = 0;
    for (Map.Entry<String, List<Unit>> placement: placements.entrySet()) {
      if (!occupyTerritory(placement.getKey())) {
        return "You can only place units on your occupies.";
      }
      else {
        for (Unit unitToPlace: placement.getValue()) {
          numberToPlace += unitToPlace.getNum();
        }
      }
    }
    for (Unit unitToAllocate: toAllocate) {
      numberToAllocate += unitToAllocate.getNum();
    }
    if (numberToPlace != numberToAllocate) {
      return "Number of units to place does not match.";
    }
    //TODO:placeAction to satisfy atomicity
    //apply
    for (Map.Entry<String, List<Unit>> placement: placements.entrySet()) {
      getTerritory(placement.getKey()).addUnitList(placement.getValue());
    }
    return null;
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
