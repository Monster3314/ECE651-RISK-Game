package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.ActionRuleChecker;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.PlaceAction;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class GUIPlayer extends Player{
  //private List<Action> actionsToSend;


  /**
   * GUIPlayer Constructor
   * @param server is the socket of the server
   */
  public GUIPlayer(Socket server) throws IOException {
    super(server);
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
  
  

}
