package ece651.riskgame.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class TextPlayer {
  final String color;
  final GameInfo theGame;
  final GameView view;
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

  public Map<Unit, Territory> doPlacementPhase(List<Unit> units) throws IOException {
    //TODO:Unit should have name, counter according to type
    Map<Territory, List<Unit>> ans = new HashMap<Territory, List<Unit>>();
    String s;
    for (Unit u : units) {
      //TODO: Unit name
      out.println("You have %d units", u.getNum());
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
      Clan myClan= theGame.getPlayers().get(color);
      for (Territory occupy:myClan.getOccupies()) {
        if (occupy.getName() == dest) {
          //TODO:change BasicUnit to unit according to unit
          ans.put(occupy, new BasicUnit(placeNum));
          break;
        }
      }
    }
    return ans;
  }
}
