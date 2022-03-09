package ece651.riskgame.shared;

import java.io.Serializable;

public class GameInfo implements Serializable{
  private Board board;
  // clan

  public GameInfo(Board b) {
    board = b;
  }
  
  public Board getBoard() {
    return board;
  }

  public void setBoard(Board b) {
    board = b;
  }
}
