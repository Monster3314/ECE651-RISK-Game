package ece651.riskgame.client;

import java.util.Set;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Territory;

public class BoardTextView implements BoardView {
  private final Board toDisplay;

  /**
   * Constructs a BoardTextView, given the board it will display
   * @param toDisplay is the Board to display  
   */
  public BoardTextView(Board toDisplay) {
    this.toDisplay = toDisplay;
  }

  /**
   * Display the board in text
   * @return a string that represents the board  
   */
  public String displayBoard() {
    Set<Territory> territories = toDisplay.getTerritoriesSet();
    StringBuilder ans = new StringBuilder();
    for (Territory t: territories) {
      StringBuilder line = new StringBuilder();
      line.append(t.getName());
      line.append("\n");
      ans.append(line.toString());
    }
    return ans.toString();
  }

}
