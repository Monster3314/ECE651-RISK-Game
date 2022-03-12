package ece651.riskgame.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Unit, Territory> ans = new HashMap<Unit, Territory>();
    String s;
    for (Unit u:units) {
      out.println("Where do you want to put an unit?");
      s = inputReader.readLine();
      if (s == null) {
        throw new EOFException("EOF");
      }
      Clan myClan= theGame.getPlayers().get(color);
      boolean find = false;
      for (Territory occupy:myClan.getOccupies()) {
        if (occupy.getName() == s) {
          ans.put(u, occupy);
          find = true;
          break;
        }
      }
    }
    return ans;
  }
}
