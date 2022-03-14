package ece651.riskgame.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class TextPlayer {
  final String color;
  private GameInfo theGame;
  private GameView view;
  final BufferedReader inputReader;
  final PrintStream out;

  /**
   * Construct the TextPlayer using Standard I/O
   * @param color is the color of the player, "Red", "Blue", etc
   * @param g is the GameInfo achieved from server  
   */
  public TextPlayer(String color, GameInfo g) {
    this(color, g, new BufferedReader(new InputStreamReader(System.in)), System.out);
  }
  
  /**
   * Construct the TextPlayer using specified I/O
   * @param color is the color of the player, "Red", "Blue", etc
   * @param g is the GameInfo achieved from server
   * @param input is hte reader which is used to fetch instructions from player
   * @param out is to print view and prompt to  
   */
  public TextPlayer(String color, GameInfo g, BufferedReader input, PrintStream out) {
    this.color = color;
    this.theGame = g;
    this.view = new GameTextView(theGame);
    this.inputReader = input;
    this.out = out;
  }
  public void display() {
    out.print(view.displayGame());
  }
  public void update(GameInfo game) {
    theGame = game;
    view = new GameTextView(theGame);
  }
  public Move readMove() throws IOException {
    Territory src = readTerritory("Which territory do you want to move unit from?");
    Unit toMove = readUnit(src, "How many units do you want to move?");
    Territory dst = readTerritory("Which territory do you want to move unit to?");
    return new Move(src.getName(), dst.getName(), toMove);
  }
  public Unit readUnit(Territory src, String prompt) throws IOException, IllegalArgumentException{
    List<Unit> units = src.getUnits();
    if (units.size() == 0) {
      throw new IllegalArgumentException("This Territory has no unit.");      
    }
    String s;
    int moveNumber;
    while (true) {
      //TODO:Support multiple units
      Unit u = units.get(0);
      out.println("You have " + Integer.toString(u.getNum()) + "units. How many of the m do you want to move?");
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      moveNumber = Integer.parseInt(s);
      try {
        u.decSoldiers(moveNumber);
        return new BasicUnit(moveNumber);
      } catch (IllegalArgumentException e) {
        out.println("Not enough units in this territory.");
      }
    }
    
  }
  public Territory readTerritory(String prompt) throws IOException {
    String s;
    List<Territory> territories = theGame.getBoard().getTerritoriesList();
    while (true) {
      out.println(prompt);
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      //TODO:catch KeyNotFoundException instead of find manually
      for (Territory t: territories) {
        if (t.getName().equals(s)) {
          return t;
        }
      }
      out.println(s + " is not one of the existing territories");
    }
  }
  public Map<Territory, List<Unit>> doPlacementPhase(List<Unit> units) throws IOException {
    //TODO:Unit should have name, counter according to type
    Map<Territory, List<Unit>> ans = new HashMap<Territory, List<Unit>>();
    Clan myClan= theGame.getPlayers().get(color);
    for (Territory occupy: myClan.getOccupies()) {
      ans.put(occupy, new ArrayList<Unit>());
    }
    String s;
    for (Unit u : units) {
      //TODO: Unit name
      out.println("You have " + Integer.toString(u.getNum()) + " units");
      out.println("How many and where do you want to put the unit(e.g. 2 Durham)?");
      s = inputReader.readLine();
      if (s == null) {
        throw new  EOFException("EOF");
      }
      //parse placement and rule check
      String[] arr = s.split(" ", 2);
      int placeNum = Integer.parseInt(arr[0]);
      String dest = arr[1];

      //TODO: rule check
      //ReEnter when placement invalid
      while (true) {
        try {
          u.decSoldiers(placeNum);
          break;
        } catch (IllegalArgumentException e) {
          s = inputReader.readLine();
          if (s == null) {
            throw new  EOFException("EOF");
          }
          //parse placement and rule check
          arr = s.split(" ", 2);
          placeNum = Integer.parseInt(arr[0]);
          dest = arr[1];
        }
      }
      for (Territory occupy:myClan.getOccupies()) { 
        if (occupy.getName().equals(dest)) {
          //TODO:change BasicUnit to unit according to unit
          ans.put(occupy, new LinkedList<Unit>(Arrays.asList(new BasicUnit(placeNum))));
          break;
        }
      }
    }
    return ans;
  }
}
