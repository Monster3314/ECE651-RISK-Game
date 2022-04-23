package ece651.riskgame.client;

import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class GameTextView implements GameView {
  private final ClientWorld theGame;
  private final Board theBoard;
  private final Map<String, Clan> clans;
  /**
   * Constructs a GameTextView, given the board it will display
   * @param toDisplay is the Board to display  
   */
  public GameTextView(ClientWorld toDisplay) {
    this.theGame = toDisplay;
    this.theBoard = toDisplay.getBoard();
    this.clans = toDisplay.getClans();
  }

  /**
   * Display the winner of the game
   * @return a string that represents the winner
   * precondition: game is over  
   */  
  public String displayWinner() {
    StringBuilder line = new StringBuilder();
    line.append(theGame.getWinner());
    line.append(" player wins!");
    return line.toString();
  }
  
  /**
   * Get Units information in text
   * @return a string that represents the units  
   */
  public String getUnitsInfo(List<Unit> units) {
    StringBuilder infoMsg = new StringBuilder();
    infoMsg.append("Your Troops:");
    if (units.size() == 0) {
      infoMsg.append("\nNo Unit");
    }
    else {
      for (Unit unit : units) {
        infoMsg.append("\n");
        infoMsg.append(getUnitInfo(unit));
      }
    }
    return infoMsg.toString();
  }

  /**
   * Get Unit information in Text:
   * @return a string that represent unit name and number e.g. 4 Second Son
   */  
  public String getUnitInfo(Unit unit) {
    StringBuilder infoMsg = new StringBuilder();
    infoMsg.append(Integer.toString(unit.getNum()));
    infoMsg.append(" ");
    infoMsg.append(unit.getName());
    return infoMsg.toString();
  }
  /**
   * Display a single Territory in text
   * @return a string that represents the territory  
   */  
  protected String displayTerritory(Territory t) {
    StringBuilder line = new StringBuilder();
    //territory units
    List<Unit> units = t.getUnits();
    line.append(getUnitsInfo(units));
    //territory name
    line.append("in ");
    line.append(t.getName());
    //territory neighbours
    line.append(" (next to: ");
    String sep = "";
    for (Territory neighbour: theBoard.getNeighbors(t)) {
      line.append(sep);
      line.append(neighbour.getName());
      sep = ", ";
    }
    line.append(")\n");
    return line.toString();
  }
  /**
   * Display the territories of a clan in text
   * @return a string that represents the clan  
   */
  protected String displayClan(String color) {
    StringBuilder ans = new StringBuilder();
    ans.append(color);
    ans.append(" player:\n");
    ans.append(getDelimiter());
    Clan currentClan = clans.get(color);
    for (Territory occupy: currentClan.getOccupies()) {
      ans.append(displayTerritory(occupy));
    }
    return ans.toString();
  }
  /**
   * Display the game in text
   * @return a string that represents the board  
   */
  public String displayGame() {
    StringBuilder ans = new StringBuilder();
    for (String color: clans.keySet()) {
      ans.append(displayClan(color));
      ans.append("\n");
    }
    return ans.toString();
  }
  
  public static String getDelimiter() {
    return "-------------\n";
  }
}
