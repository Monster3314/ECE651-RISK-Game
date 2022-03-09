package ece651.riskgame.client;

import ece651.riskgame.shared.Board;

public class TextPlayer {
  //private id;
  //final String color;
  final Board theBoard;
  final BoardTextView view;
  public TextPlayer(Board b) {
    //this.color = color;
    this.theBoard = b;
    this.view = new BoardTextView(theBoard);
  }
}
